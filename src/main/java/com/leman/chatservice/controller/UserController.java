package com.leman.chatservice.controller;

import com.leman.chatservice.dto.request.PasswordChangeRequest;
import com.leman.chatservice.dto.request.UserSearchRequest;
import com.leman.chatservice.dto.request.UserUpdateRequest;
import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.UserResponse;
import com.leman.chatservice.dto.response.UserSearchResponse;
import com.leman.chatservice.security.UserPrincipal;
import com.leman.chatservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageableResponse<UserSearchResponse>> searchUsers(@Valid UserSearchRequest request) {
        return ResponseEntity.ok(userService.searchUsers(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userService.findUserById(userPrincipal.getUser().getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(userPrincipal.getUser().getId(), request));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody @Valid PasswordChangeRequest request) {
        userService.changePassword(userPrincipal.getUser().getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteUser(userPrincipal.getUser().getId());
        return ResponseEntity.noContent().build();
    }

}
