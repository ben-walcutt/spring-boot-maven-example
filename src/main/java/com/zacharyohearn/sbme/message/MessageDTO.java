package com.zacharyohearn.sbme.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {
    private int messageId;
    private String messageBody;
    // TODO convert to something not local, ZonedDateTime GMT ish
    private LocalDateTime createdTimeStamp;
    private int userId;
    private String firstName;
    private String lastName;
}
