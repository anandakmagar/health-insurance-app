package com.notifcationservice.service;

import com.notifcationservice.dto.NotificationDto;
import com.notifcationservice.dto.PolicyDto;
import com.notifcationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    public void sendPolicyGeneratedNotication(NotificationDto notificationDto);
}
