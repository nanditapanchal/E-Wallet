package com.example.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.constants.AppConstants;
import com.example.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationConsumerService {
	@Autowired
	private SimpleMailMessage mailMailMsg;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
   private ObjectMapper objMapper;
	//listen some topic (Subscribe the topic)
	@KafkaListener(topics = AppConstants.NEW_USER,groupId = "group-id")
	public void consumeMessages(ConsumerRecord<String, String> consumer) {
		System.out.println("Received the msg");
		String k=consumer.key();
		String jsonText=consumer.value();
		System.out.println("Received JSON "+jsonText);
		
	try {
		UserDto userDto=objMapper.readValue(jsonText, UserDto.class);
		
		mailMailMsg.setTo(userDto.getEmail());
		mailMailMsg.setSubject("Registration Success");
		String msg="Hi "+userDto.getUserName()+", congratulations !!!!";
		mailMailMsg.setText(msg);
		mailSender.send(mailMailMsg);
		System.out.println("Mail send ...");
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
