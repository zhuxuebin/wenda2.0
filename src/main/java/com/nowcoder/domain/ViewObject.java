package com.nowcoder.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuery on 2016/8/22.
 */
public class ViewObject {
    private Map<String,Object> objs = new HashMap<>();

    public void set(String key,Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }
}
