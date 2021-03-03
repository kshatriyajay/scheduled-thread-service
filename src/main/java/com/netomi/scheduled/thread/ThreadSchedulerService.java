package com.netomi.scheduled.thread;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Service
public class ThreadSchedulerService {
    private static int count = 0;
    private static int sequence = 0;
    int low = 100;
    int high = 1500;
    Random r = new Random();
    int[] visitorId = {0,1,2,3,4,5,6,7,8,9,10};
    ScheduledExecutorService threadPoolExecutor =  Executors.newScheduledThreadPool(1);
    SchedulesThreadExecutorHelper schedulesThreadExecutorHelper = new SchedulesThreadExecutorHelper(10, new ConcurrentHashMap<>(), 1000, 10);
    public String waitTime() {
        int currentCount = ++count;
        int result = r.nextInt(high-low) + low;
        //threadPoolExecutor.schedule(new ThreadLocal(currentCount, result), result, TimeUnit.MILLISECONDS);
        schedulesThreadExecutorHelper.scheduleTask(String.valueOf(visitorId[result % 11]), new ThreadLocal(currentCount, String.valueOf(visitorId[result % 11]), result),result,-1 );
        return String.format("Request submited and callback to your endpoint in %d milli seconds and for number: %d", result, count);
    }

    public String waitingTimeNew() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(8);
        ++sequence;
        List<String> responses = Arrays.asList("ABC" + sequence, "XYS" + sequence, "Testing" + sequence);
        int result = r.nextInt(high-low) + low;
        Runnable runnable = () -> {
            for (String response : responses) {
                try {

                    //System.out.println(String.format("ZenDesk Chat: Sending response for RequestId: %s", response));
                    Thread.sleep(result);
                    //zenDeskHelper.sendTypingIndicatorOFF(session, channelId, botRefId, requestId);
                    ++count;
                    System.out.println("Running Code " + response + " and current count = " + count + " Wait Time is " + result);


                } catch (Exception e) {
                    System.out.println("Error occurred for response" + response);
                    e.printStackTrace();
                }
            }
        };
        scheduledExecutorService.schedule(runnable, result, TimeUnit.MILLISECONDS);
        scheduledExecutorService.shutdown();
        return "Running it";
    }

}
