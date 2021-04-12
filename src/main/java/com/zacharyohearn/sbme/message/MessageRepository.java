package com.zacharyohearn.sbme.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByUserIdOrderByCreatedTimestampAsc(int userId);
    List<Message> findAllByMessageBodyContainsAndUserIdOrderByCreatedTimestampAsc(String text, int userId);
    Message findTopByCreatedTimestampAfter(LocalDateTime dateTime);
}
