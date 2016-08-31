package com.nowcoder.util;

/**
 * Created by nowcoder on 2016/7/30.
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    //粉丝                谁关注了我    我可以是人，也可以是问题
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //我关注的对象         我关注了哪些问题
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**
     * 粉丝列表
     *
     * 如果是人与人 entityId就是userId
     *
     * 要和getFolloweeKey区分开
     * 对于同一个用户的粉丝和他关注的人，如果public static String getFolloweeKey(int userId, int entityType)
     * 改为public static String getFolloweeKey(int entityType, int userId)则会和follower重合
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    /**
     * 我关注了对象，则需要将我的信息放到被关注者的粉丝列表
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }


    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

}
