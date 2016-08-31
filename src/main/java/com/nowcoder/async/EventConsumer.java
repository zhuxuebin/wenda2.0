package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by xuery on 2016/8/25.
 * 消费者 从队列头取出任务完成操作
 *
 *
 * 总体思路：
 * 1，先初始化：获取所有实现EventHandler的类，每个具体的实现类可以处理不同type的任务
 * 先整理成一个Map，key为type值，value为一个list是可以处理当前type的EventHandler类集合
 *之所以要将type作为key是因为当接到任务判断type之后就知道要交给哪个EventHandler处理了
 ,2，开启线程，阻塞等待任务，来了就处理，从而实现异步
 */
@Service
public class EventConsumer  implements InitializingBean,ApplicationContextAware{

    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private ApplicationContext applicationContext;  //通过上下文拦截想拦截的注解类
    Map<EventType,List<EventHandler>> config = new HashMap<EventType,List<EventHandler>>();

    /***
     * 先进行一些初始化
     * 获取所有实现了EventHandler接口的类并分类
     * 然后开启线程用相应的EventHandler实现类的doHandler完成操作
     *
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取所有实现了EventHandler接口的类,当然这些类都是注解过的
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans != null){
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                //先得到当前EventHandler实现类可以处理的类型，然后分类
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for(EventType type:eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        //开大小为2的线程池进行处理
        ExecutorService service = Executors.newFixedThreadPool(2);
        for(int i=0;i<2;i++) {
            service.submit(new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String key = RedisKeyUtil.getEventQueueKey();  //只处理EVENT_QUEUE事件
                        List<String> events = jedisAdapter.brpop(0, key);

                        for (String message : events) {
                            //brpop有点特殊，它第一个String是key，之后才是key对应的值
                            if (message.equals(key)) {
                                continue;
                            }
                            EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
                            if (!config.containsKey(eventModel.getType())) {
                                logger.error("不能识别的事件");
                                continue;
                            }
                            /**用相应的handler去处理当前类型即可，为什么是for循环呢，因为对同一个事件可能进行多种处理也是有可能的
                             *最简单的就是需要将当前数据存入某几张表中
                             * 不过讲道理对同一个type的处理在一个handler中处理就可以了
                             */
                            for (EventHandler handler : config.get(eventModel.getType())) {
                                handler.doHandler(eventModel);
                            }
                        }
                    }
                }
            }));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
