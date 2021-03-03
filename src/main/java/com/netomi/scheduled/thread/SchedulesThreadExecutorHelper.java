package com.netomi.scheduled.thread;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulesThreadExecutorHelper {
    private ScheduledExecutorService scheduledExecutorService;
    private Map<String, Object> scheduledRedisTemplate;
    private int cacheTTLInSeconds;
    private int defaultMinDelayInMillis;
    private static final String SCHEDULED_TASK_KEY = "SCHEDULED_TASK_KEY_";
    private static final int OVERRIDE_DEFAULT_MIN_DELAYS = -1;

    public SchedulesThreadExecutorHelper(int numberOfThreads, Map<String, Object> scheduledRedisTemplate, int cacheTTLInSeconds, int defaultMinDelayInMillis) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(numberOfThreads);
        this.scheduledRedisTemplate = scheduledRedisTemplate;
        this.cacheTTLInSeconds = cacheTTLInSeconds;
        this.defaultMinDelayInMillis = defaultMinDelayInMillis;
    }

    public void scheduleTask(String scheduleTaskKey, Runnable scheduleTask, int delayInMillis, int minDelayInMillis) {
        Long lastScheduledTaskKeyTimestamp = (Long)scheduledRedisTemplate.get(SCHEDULED_TASK_KEY + scheduleTaskKey);
        if(minDelayInMillis != OVERRIDE_DEFAULT_MIN_DELAYS) {
            minDelayInMillis = this.defaultMinDelayInMillis;
        }
        Long newTimestampDelay = (long)(delayInMillis + minDelayInMillis);
        Long timestamp = new Long((new DateTime(DateTimeZone.UTC)).getMillis());
        if(lastScheduledTaskKeyTimestamp != null) {
          if(timestamp.compareTo(new Long(lastScheduledTaskKeyTimestamp + newTimestampDelay)) < 0) {
              System.out.println(String.format("Relative Schedule delay %d as currentTimestamp = %s and recentPlannedExecution %s", newTimestampDelay, (new DateTime(timestamp, DateTimeZone.UTC)).toString(), (new DateTime(lastScheduledTaskKeyTimestamp+newTimestampDelay, DateTimeZone.UTC)).toString()));
              newTimestampDelay += lastScheduledTaskKeyTimestamp - timestamp;
              System.out.println(String.format("Absolute Schedule delay %d", newTimestampDelay));
          }
        } else {
            System.out.println(String.format("Not able to find key %s", scheduleTaskKey));
        }
        this.scheduledExecutorService.schedule(scheduleTask, newTimestampDelay, TimeUnit.MILLISECONDS);
        scheduledRedisTemplate.put(SCHEDULED_TASK_KEY + scheduleTaskKey,timestamp+newTimestampDelay);
    }
}
