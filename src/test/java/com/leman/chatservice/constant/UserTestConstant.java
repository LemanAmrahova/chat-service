package com.leman.chatservice.constant;

import static com.leman.chatservice.constant.AuthTestConstant.PASSWORD;
import static com.leman.chatservice.constant.TestConstant.ID;
import static com.leman.chatservice.constant.TestConstant.PAGE;
import static com.leman.chatservice.constant.TestConstant.SIZE;
import static com.leman.chatservice.constant.TestConstant.SORT_BY;
import static com.leman.chatservice.constant.TestConstant.SORT_DIRECTION;

import com.leman.chatservice.dto.request.PasswordChangeRequest;
import com.leman.chatservice.dto.request.UserFilterRequest;
import com.leman.chatservice.dto.request.UserUpdateRequest;
import com.leman.chatservice.dto.response.UserResponse;
import com.leman.chatservice.entity.User;
import com.leman.chatservice.enums.Role;
import com.leman.chatservice.security.UserPrincipal;
import java.time.LocalDateTime;

public final class UserTestConstant {

    private UserTestConstant() {
    }

    public static final Long USER_ID = 1L;
    public static final String USERNAME = "testuser";
    public static final String EMAIL = "test@example.com";
    public static final String ENCODED_PASSWORD = "$2a$10$encodedPassword";
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.of(2024, 1, 1, 10, 0);
    public static final String CURRENT_PASSWORD = "CurrentPass@123";
    public static final String NEW_PASSWORD = "NewPass@123";

    public static final UserUpdateRequest USER_UPDATE_REQUEST = UserUpdateRequest.builder()
            .username(USERNAME)
            .email(EMAIL)
            .build();

    public static final UserFilterRequest USER_FILTER_REQUEST = UserFilterRequest.builder()
            .page(PAGE)
            .size(SIZE)
            .sortBy(SORT_BY)
            .sortDirection(SORT_DIRECTION)
            .build();

    public static final PasswordChangeRequest PASSWORD_CHANGE_REQUEST = PasswordChangeRequest.builder()
            .currentPassword(CURRENT_PASSWORD)
            .newPassword(NEW_PASSWORD)
            .build();

    public static final PasswordChangeRequest SAME_PASSWORD_REQUEST = PasswordChangeRequest.builder()
            .currentPassword(CURRENT_PASSWORD)
            .newPassword(CURRENT_PASSWORD)
            .build();

    public static User userEntity() {
        return User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .role(Role.USER)
                .enabled(true)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    public static User adminEntity() {
        return User.builder()
                .id(ID)
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(Role.ADMIN)
                .enabled(true)
                .build();
    }

    public static final UserResponse USER_RESPONSE = UserResponse.builder()
            .id(USER_ID)
            .username(USERNAME)
            .email(EMAIL)
            .role(Role.USER)
            .enabled(true)
            .createdAt(CREATED_AT)
            .updatedAt(UPDATED_AT)
            .build();

    public static final UserPrincipal USER_PRINCIPAL = new UserPrincipal(userEntity());

}
