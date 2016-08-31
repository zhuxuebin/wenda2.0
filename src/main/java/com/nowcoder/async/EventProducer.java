package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuery on 2016/8/25.
 *
 * 生产者
 * 将任务进队列并标识任务的类型，这样consumer才知道如何去处理
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 进队列
     * 先序列化再进相应的队列
     * @param eventModel
     * @return
     */
    public boolean fireEvent(EventModel eventModel){
        try{
            String key = RedisKeyUtil.getEventQueueKey();
            String json = JSONObject.toJSONString(eventModel);  //对象序列化
            jedisAdapter.lpush(key,json);   //以list存储  队列先进先出
            return true;
        }catch(Exception e){
            return false;
        }
    }


}
