package com.intuit.demo.listener;

import com.intuit.demo.repository.DriverBackgroundVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackgroundVerificationKafkaListener {

    @Autowired
    private DriverBackgroundVerificationRepository driverBackgroundVerificationRepository;

//    @KafkaListener(topics = "${kafka.topic.background.verification}",
//            containerFactory = "driverKafkaListenerContainerFactory")
    public void processMessage(String content) {
        // todo add to driverBackgroundVerificationRepository repo
    }

}

