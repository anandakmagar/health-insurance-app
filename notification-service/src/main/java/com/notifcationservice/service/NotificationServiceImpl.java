package com.notifcationservice.service;

import com.notifcationservice.dto.NotificationDto;
import com.notifcationservice.model.Notification;
import com.notifcationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public Notification mapToEntity(NotificationDto notificationDto){
        Notification notification = new Notification();
        notification.setId(notificationDto.getId());
        notification.setSubject(notificationDto.getSubject());
        notification.setRecipient(notification.getRecipient());
        notification.setBody(notificationDto.getBody());
        return notification;
    }

    public void saveNotification(NotificationDto notificationDto){
        Notification notification = mapToEntity(notificationDto);
        notificationRepository.save(notification);
    }

    @Override
    public void sendPolicyGeneratedNotication(NotificationDto notificationDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notificationDto.getRecipient());
        message.setSubject(notificationDto.getSubject());
        message.setText(notificationDto.getBody());

        javaMailSender.send(message);
        System.out.println("Email notification has been sent to " + notificationDto.getRecipient());

        saveNotification(notificationDto);
    }
}
