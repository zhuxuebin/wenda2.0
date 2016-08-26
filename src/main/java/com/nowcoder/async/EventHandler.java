package com.nowcoder.async;

import java.util.List;

/**
 * Created by xuery on 2016/8/25.
 */
public interface EventHandler {
    void doHandler(EventModel model);

    List<EventType> getSupportEventTypes();
}
