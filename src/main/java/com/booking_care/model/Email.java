package com.booking_care.model;

import lombok.Data;

import javax.mail.internet.InternetAddress;
import java.util.Map;

@Data
public class Email {
    String to;
    InternetAddress from;
    String subject;
    String text;
    String template;
    Map<String, Object> properties;
}