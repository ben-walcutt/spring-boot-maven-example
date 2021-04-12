package com.zacharyohearn.sbme.message;

import com.zacharyohearn.sbme.user.User;
import com.zacharyohearn.sbme.user.UserException;
import com.zacharyohearn.sbme.user.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepository messageRepository;
    private UserServiceClient userServiceClient;

    List<MessageDTO> getMessagesForUser(String firstName, String lastName, String dateOfBirth) {
        User user = userServiceClient.getUser(firstName, lastName, dateOfBirth).orElseThrow(() -> new UserException("Error encountered retrieving user details."));
        List<Message> list = messageRepository.findAllByUserIdOrderByCreatedTimestampAsc(user.getUserId());
        List<MessageDTO> mappedMessages = new ArrayList<>();
        for(Message message: list) {
            mappedMessages.add(mapToMessageDTO(message, user));
        }
        return mappedMessages;
    }

    private MessageDTO mapToMessageDTO(Message message, User user) {
        return MessageDTO.builder()
                .messageId(message.getMessageId())
                .messageBody(message.getMessageBody())
                .createdTimeStamp(message.getCreatedTimestamp())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userId(user.getUserId())
                .build();
    }

    Optional<MessageDTO> messageSearch(String firstName, String lastName, String dateOfBirth, String searchText) {
        User user = userServiceClient.getUser(firstName, lastName, dateOfBirth).orElseThrow(() -> new UserException("Error encountered retrieving user details."));
        List<Message> messageMatches = messageRepository.findAllByMessageBodyContainsAndUserIdOrderByCreatedTimestampAsc(searchText, user.getUserId());
        if (CollectionUtils.isEmpty(messageMatches)) {
            return Optional.empty();
        } else {
            return Optional.of(mapToMessageDTO(messageMatches.get(0), user));
        }
    }


    void createNewMessage(NewMessageDTO m) {
        User user = userServiceClient.getUser(m.getFirstName(), m.getLastName(), m.getDateOfBirth()).orElseThrow(() -> new UserException("Error encountered retrieving user details."));
        LocalDateTime currentDateTime = LocalDateTime.now();
        Message newMessage = Message.builder()
                .userId(user.getUserId())
                .messageBody(m.getBody())
                .createdTimestamp(currentDateTime)
                .lastUpdatedTimestamp(currentDateTime)
                .build();
        messageRepository.save(newMessage);
    }

    MessageDTO editMessage(MessageDTO editMessage) {
        Message message = messageRepository.getOne(editMessage.getMessageId());
        if (message.getUserId() != editMessage.getUserId()) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        message.setMessageBody(editMessage.getMessageBody());
        message.setLastUpdatedTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return editMessage;
    }
}
