package com.leman.chatservice.constant;

import com.leman.chatservice.dto.request.RoomCreateRequest;
import com.leman.chatservice.dto.request.RoomFilterRequest;
import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.RoomResponse;
import com.leman.chatservice.entity.User;
import com.leman.chatservice.enums.RoomType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomTestConstant {

    public static final Long ROOM_ID = 1L;
    public static final Long CREATOR_ID = 1L;
    public static final Long MEMBER_ID = 2L;
    public static final String ROOM_NAME = "Test Room";
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);

    public static final RoomCreateRequest ROOM_CREATE_REQUEST = RoomCreateRequest.builder()
            .name(ROOM_NAME)
            .memberIds(Set.of(MEMBER_ID))
            .build();

    public static final RoomCreateRequest ROOM_CREATE_REQUEST_WITH_CREATOR = RoomCreateRequest.builder()
            .name(ROOM_NAME)
            .memberIds(Set.of(CREATOR_ID))
            .build();

    public static final RoomFilterRequest ROOM_FILTER_REQUEST = RoomFilterRequest.builder()
            .page(0)
            .size(10)
            .build();

    public static final RoomResponse ROOM_RESPONSE = RoomResponse.builder()
            .id(ROOM_ID)
            .name(ROOM_NAME)
            .type(RoomType.GROUP)
            .inviteLinkEnabled(false)
            .members(List.of())
            .createdAt(CREATED_AT)
            .build();

    public static final PageableResponse<RoomResponse> PAGEABLE_RESPONSE = PageableResponse.<RoomResponse>builder()
            .content(List.of(ROOM_RESPONSE))
            .page(0)
            .size(10)
            .totalElements(1)
            .totalPages(1)
            .first(true)
            .last(true)
            .empty(false)
            .build();

    public static User creatorEntity() {
        return User.builder()
                .id(CREATOR_ID)
                .username("creator")
                .email("creator@test.com")
                .enabled(true)
                .build();
    }

    public static User memberEntity() {
        return User.builder()
                .id(MEMBER_ID)
                .username("member")
                .email("member@test.com")
                .enabled(true)
                .build();
    }

}
