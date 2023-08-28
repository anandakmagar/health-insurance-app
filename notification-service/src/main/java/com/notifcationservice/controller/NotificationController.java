package com.notifcationservice.controller;

import com.notifcationservice.dto.NotificationDto;
import com.notifcationservice.dto.PolicyDto;
import com.notifcationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @PostMapping
    public void sendPolicyGeneratedNotification(@RequestBody NotificationDto notificationDto){
        notificationService.sendPolicyGeneratedNotication(notificationDto);
    }
}
