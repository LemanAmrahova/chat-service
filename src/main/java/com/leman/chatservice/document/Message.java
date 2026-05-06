package com.leman.chatservice.document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "message")
public class Message {

    @Id
    private String id;

    @Indexed
    private Long roomId;

    private Long senderId;

    private String encryptedContent;

    @Builder.Default
    private Instant sentAt = Instant.now();

    @Builder.Default
    private List<Long> readBy = new ArrayList<>();

}
