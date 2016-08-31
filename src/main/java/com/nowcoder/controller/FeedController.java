package com.nowcoder.controller;

import com.nowcoder.domain.EntityType;
import com.nowcoder.domain.Feed;
import com.nowcoder.domain.HostHolder;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuery on 2016/8/30.
 */
@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 推
     * 大V是主动方，粉丝被动接受，直接从自己对应的redis中timelineKey中取出即可
     *
     * 粉丝直接从redis中取出feedId，再从mysql中根据feedId取出实体
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getPushFeeds(Model model){
        int localUserId= hostHolder.getUsers()!=null?hostHolder.getUsers().getId():0;
        //直接从localUserId对应的timelineKey中取出新鲜事id即可
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0,10); //获取localUserId的10条新鲜事
        List<Feed> feeds = new ArrayList<>();
        for(String feedId:feedIds){
            if(feedId == null || "".equals(feedId)){
                continue;
            }else {
                Feed feed = feedService.getById(Integer.parseInt(feedId));
                if (feed != null) {
                    feeds.add(feed);
                }
            }
        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    /**
     *粉丝根据关注者列表，通过List<userId>主动去mysql数据库中取新鲜事实体
     * @param model
     * @return
     */
    @RequestMapping(value="/pullfeeds",method={RequestMethod.GET,RequestMethod.POST})
    public String getPullFeeds(Model model){
        int localUserId = hostHolder.getUsers()!=null?hostHolder.getUsers().getId():0;
        List<Integer> followees = new ArrayList<>();
        if(localUserId!=0){
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }
}
