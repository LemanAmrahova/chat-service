package com.leman.chatservice.dto.response;

import com.leman.chatservice.enums.RoomType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;
    private String name;
    private RoomType type;
    private boolean inviteLinkEnabled;
    private String inviteLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserSearchResponse> members;

}
