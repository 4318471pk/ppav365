package com.live.fox.manager;


/**
 * User: lushuang
 * Date: 16/6/25
 * Time: 下午3:33
 */
public class TaskManager {
    private static final class TaskManagerHolder {
        static final TaskManager taskManager = new TaskManager();
    }

    public static TaskManager getInstance() {
        return TaskManagerHolder.taskManager;
    }
}
