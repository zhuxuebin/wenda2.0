package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuery on 2016/8/26.
 */
public interface LikeService {
    long getLikeCount(int entityType, int entityId);

    int getLikeStatus(int userId, int entityType, int entityId);

    long like(int userId, int entityType, int entityId);

    long disLike(int userId, int entityType, int entityId);


}
