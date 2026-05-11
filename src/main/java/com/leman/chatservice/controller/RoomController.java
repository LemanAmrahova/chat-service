package com.leman.chatservice.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.leman.chatservice.dto.request.RoomCreateRequest;
import com.leman.chatservice.dto.response.RoomResponse;
import com.leman.chatservice.security.UserPrincipal;
import com.leman.chatservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody @Valid RoomCreateRequest request) {
        return ResponseEntity.status(CREATED).body(roomService.createRoom(request, userPrincipal.getUser()));
    }

}
