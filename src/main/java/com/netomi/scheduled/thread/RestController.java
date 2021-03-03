package com.netomi.scheduled.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1/scheduled/thread")
public class RestController {
    @Autowired
    private ThreadSchedulerService threadSchedulerService;
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> conversationEngineAsync(@RequestHeader HttpHeaders headers) throws Throwable {
        return new ResponseEntity<>(threadSchedulerService.waitTime(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> shopifyResponse(@RequestBody String json,
                                                          @RequestHeader HttpHeaders headers) throws Throwable {
        System.out.println(String.format("Received API call with Headers %s", headers.toString()));
        System.out.println(json);
        return new ResponseEntity<>("Successfully processed the request", HttpStatus.ACCEPTED);
    }
}
