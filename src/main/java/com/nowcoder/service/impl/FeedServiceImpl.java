package com.nowcoder.service.impl;

import com.nowcoder.dao.FeedMapper;
import com.nowcoder.domain.Feed;
import com.nowcoder.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuery on 2016/8/30.
 */
@Service
public class FeedServiceImpl implements FeedService{

    @Autowired
    FeedMapper feedMapper;

    @Override
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedMapper.getUserFeeds(maxId,userIds,count);
    }

    @Override
    public boolean addFeed(Feed feed) {
        feedMapper.addFeed(feed);
        return feed.getId() > 0;
//        return feedMapper.insert(feed) > 0;
    }

    @Override
    public Feed getById(int id) {
        return feedMapper.selectByPrimaryKey(id);
    }
}
