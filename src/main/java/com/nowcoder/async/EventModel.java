package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuery on 2016/8/25.
 */
public class EventModel {
    private EventType type;
    private int actorId;     //谁触发的
    private int entityType;
    private int entityId;
    private int entityOwnerId; //触发的事件是谁的


    private Map<String,String> exts = new HashMap<>();

    public EventModel(){

    }

    public EventModel(EventType type){
        this.type = type;
    }

    public String getExts(String key){
        return exts.get(key);
    }

    public EventModel setExts(String key,String value){
        exts.put(key,value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    //链式设值
    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
