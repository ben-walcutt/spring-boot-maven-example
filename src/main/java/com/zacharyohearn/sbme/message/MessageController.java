package com.zacharyohearn.sbme.message;

import com.zacharyohearn.sbme.user.UserException;
import io.restassured.path.json.JsonPath;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.action.internal.CollectionUpdateAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity getMessagesForUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String dateOfBirth) {
        return ResponseEntity.ok(messageService.getMessagesForUser(firstName, lastName, dateOfBirth));
    }


    @GetMapping("/messages/first")
    public ResponseEntity getFirstMessage(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String dateOfBirth, @RequestParam int age) {
        List<MessageDTO> messages = messageService.getMessagesForUser(firstName, lastName, dateOfBirth);
        if (CollectionUtils.isEmpty(messages)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(messages.get(0));
        }
    }

    @GetMapping("/messages/search")
    public ResponseEntity searchMessagesByFirstName(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String dateOfBirth, @RequestParam String searchText) {
        return ResponseEntity.ok(messageService.messageSearch(firstName, lastName, dateOfBirth, searchText));
    }

    @PostMapping("/createnewmessage")
    public ResponseEntity createNewMessage(@RequestBody NewMessageDTO newMessage) {
        messageService.createNewMessage(newMessage);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/editMessage")
    public ResponseEntity editMessage(@RequestBody MessageDTO editMessage) {
        return ResponseEntity.ok(messageService.editMessage(editMessage));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity handleUserNotFound(UserException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}








