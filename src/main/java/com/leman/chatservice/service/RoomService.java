package com.leman.chatservice.service;

import com.leman.chatservice.dto.request.RoomCreateRequest;
import com.leman.chatservice.dto.request.RoomFilterRequest;
import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.RoomResponse;
import com.leman.chatservice.entity.Room;
import com.leman.chatservice.entity.User;
import com.leman.chatservice.enums.MemberRole;
import com.leman.chatservice.exception.BadRequestException;
import com.leman.chatservice.mapper.RoomMapper;
import com.leman.chatservice.repository.RoomRepository;
import com.leman.chatservice.util.PaginationUtil;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final UserService userService;

    @Transactional
    public RoomResponse createRoom(RoomCreateRequest request, User creator) {
        if (request.getMemberIds().contains(creator.getId())) {
            throw BadRequestException.of("Creator cannot be added as a member");
        }

        Room room = buildRoom(request, creator);
        addMembers(room, request.getMemberIds());
        Room saved = roomRepository.save(room);

        log.info("Room created successfully with ID: {}", saved.getId());
        return roomMapper.toResponse(saved, creator.getId());
    }

    public PageableResponse<RoomResponse> findAllRooms(RoomFilterRequest request, Long userId) {
        Pageable pageable = PaginationUtil.createPageable(request);

        return roomMapper.toPageableResponse(
                roomRepository.findAllByMember(userId, request.getName(), request.getType(), pageable)
                        .map(room -> roomMapper.toResponse(room, userId))
        );
    }

    private Room buildRoom(RoomCreateRequest request, User creator) {
        Room room = roomMapper.toEntity(request);
        room.setCreatedBy(creator);
        room.getMembers().add(roomMapper.toMemberEntity(room, creator, MemberRole.OWNER));
        return room;
    }

    private void addMembers(Room room, Set<Long> memberIds) {
        userService.findUsersByIds(memberIds)
                .forEach(member -> room.getMembers().add(roomMapper.toMemberEntity(room, member, MemberRole.MEMBER)));
    }

}
