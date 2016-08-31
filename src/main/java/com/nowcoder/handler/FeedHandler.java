package com.nowcoder.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.domain.EntityType;
import com.nowcoder.domain.Feed;
import com.nowcoder.domain.Question;
import com.nowcoder.domain.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by xuery on 2016/8/31.
 *
 * 产生新鲜事 这个才是关键
 */
@Component
public class FeedHandler implements EventHandler{
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    /**
     * 产生问题相关的新鲜事
     * @param model
     * @return
     */
    private String buildFeedData(EventModel model){
        Map<String,String> map = new HashMap<>();
        //触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if(actor == null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());

        //这里question相关的 新鲜事
        if(model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.getById(model.getEntityId());
            if(question==null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandler(EventModel model) {
        //为了测试，把model的userId随机一下
        Random r = new Random();
        model.setActorId(1+r.nextInt(10));

        //产生一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if(feed.getData()==null){
            //不支持当前类型feed
            return;
        }
        feedService.addFeed(feed);

        //获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        //系统队列
        followers.add(0);
        //给所有粉丝推事件
        for(int follower:followers){
            //获取每个粉丝的timelineKey
            String timelinekey = RedisKeyUtil.getTimelineKey(follower);
            //从左插入，所以左边的是最新的
            jedisAdapter.lpush(timelinekey,String.valueOf(feed.getId()));

            //限制最长长度，如果timelineKey的长度过大，则删除后面的新鲜事  这里只保存10条
            while(jedisAdapter.llen(timelinekey) > 10){
                jedisAdapter.brpop(0,timelinekey);
            }
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
