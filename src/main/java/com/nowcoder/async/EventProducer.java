package com.nowcoder.async;

import com.nowcoder.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuery on 2016/8/25.
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
        return true;
    }


}
