package ru.skqwk.kicksharingservice.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage {
    private String from;
    private String to;
    private String content;
}


