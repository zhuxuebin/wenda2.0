package com.nowcoder.service;

import com.nowcoder.domain.Feed;

import java.util.List;

/**
 * Created by xuery on 2016/8/30.
 */
public interface FeedService {

    /**
     * 取count个满足id < maxId,且user_id in userIds
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);

    boolean addFeed(Feed feed);

    Feed getById(int id);
}
