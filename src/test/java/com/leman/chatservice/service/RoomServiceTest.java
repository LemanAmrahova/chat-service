package com.leman.chatservice.service;

import static com.leman.chatservice.constant.RoomTestConstant.CREATOR_ID;
import static com.leman.chatservice.constant.RoomTestConstant.MEMBER_ID;
import static com.leman.chatservice.constant.RoomTestConstant.ROOM_CREATE_REQUEST;
import static com.leman.chatservice.constant.RoomTestConstant.ROOM_CREATE_REQUEST_WITH_CREATOR;
import static com.leman.chatservice.constant.RoomTestConstant.ROOM_FILTER_REQUEST;
import static com.leman.chatservice.constant.RoomTestConstant.creatorEntity;
import static com.leman.chatservice.constant.RoomTestConstant.memberEntity;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.RoomResponse;
import com.leman.chatservice.entity.Room;
import com.leman.chatservice.exception.BadRequestException;
import com.leman.chatservice.exception.ResourceNotFoundException;
import com.leman.chatservice.mapper.RoomMapper;
import com.leman.chatservice.repository.RoomRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Spy
    private RoomMapper roomMapper = Mappers.getMapper(RoomMapper.class);

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_Should_Return_Success() {
        given(userService.findUsersByIds(Set.of(MEMBER_ID))).willReturn(List.of(memberEntity()));
        given(roomRepository.save(any(Room.class))).willAnswer(inv -> inv.getArgument(0));

        RoomResponse result = roomService.createRoom(ROOM_CREATE_REQUEST, creatorEntity());
        assertNotNull(result);

        then(userService).should(times(1)).findUsersByIds(Set.of(MEMBER_ID));
        then(roomRepository).should(times(1)).save(any(Room.class));
    }

    @Test
    void createRoom_Should_Throw_BadRequestException_WhenCreatorInMemberIds() {
        assertThrows(BadRequestException.class,
                () -> roomService.createRoom(ROOM_CREATE_REQUEST_WITH_CREATOR, creatorEntity()));

        then(userService).shouldHaveNoInteractions();
        then(roomRepository).shouldHaveNoInteractions();
    }

    @Test
    void createRoom_Should_Throw_ResourceNotFoundException_WhenMemberNotFound() {
        given(userService.findUsersByIds(Set.of(MEMBER_ID)))
                .willThrow(ResourceNotFoundException.of("User", "id", MEMBER_ID));

        assertThrows(ResourceNotFoundException.class,
                () -> roomService.createRoom(ROOM_CREATE_REQUEST, creatorEntity()));

        then(roomRepository).shouldHaveNoInteractions();
    }

    @Test
    void findAllRooms_Should_Return_Success() {
        Page<Room> roomPage = mock(Page.class);
        given(roomPage.map(any())).willReturn(Page.empty());
        given(roomRepository.findAllByMember(any(), any(), any(), any())).willReturn(roomPage);

        PageableResponse<RoomResponse> result = roomService.findAllRooms(ROOM_FILTER_REQUEST, CREATOR_ID);
        assertNotNull(result);

        then(roomRepository).should(times(1)).findAllByMember(any(), any(), any(), any());
    }

}
