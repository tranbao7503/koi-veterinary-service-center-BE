package org.ftf.koifishveterinaryservicecenter.entity;

import lombok.Data;

@Data
public class EmailRequest {
    private String toEmail;
    private String subject;
    private String message;
}
