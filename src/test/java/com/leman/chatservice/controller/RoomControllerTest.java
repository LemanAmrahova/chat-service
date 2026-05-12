package com.leman.chatservice.controller;

import static com.leman.chatservice.constant.RoomTestConstant.PAGEABLE_RESPONSE;
import static com.leman.chatservice.constant.RoomTestConstant.ROOM_CREATE_REQUEST;
import static com.leman.chatservice.constant.RoomTestConstant.ROOM_RESPONSE;
import static com.leman.chatservice.constant.UserTestConstant.USER_PRINCIPAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leman.chatservice.dto.request.RoomCreateRequest;
import com.leman.chatservice.dto.request.RoomFilterRequest;
import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.RoomResponse;
import com.leman.chatservice.entity.User;
import com.leman.chatservice.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RoomController.class)
@AutoConfigureJsonTesters
class RoomControllerTest extends BaseControllerTest {

    private static final String BASE_PATH = "/api/v1/rooms";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Autowired
    private JacksonTester<RoomCreateRequest> requestTester;

    @Autowired
    private JacksonTester<RoomResponse> responseTester;

    @Autowired
    private JacksonTester<PageableResponse<RoomResponse>> pageableResponseTester;

    @Test
    void create_ShouldReturn_Success() throws Exception {
        given(roomService.createRoom(any(RoomCreateRequest.class), any(User.class))).willReturn(ROOM_RESPONSE);

        mockMvc.perform(post(BASE_PATH)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities())))
                        .content(requestTester.write(ROOM_CREATE_REQUEST).getJson())
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseTester.write(ROOM_RESPONSE).getJson()));

        then(roomService).should(times(1)).createRoom(any(RoomCreateRequest.class), any(User.class));
    }

    @Test
    void findAll_ShouldReturn_Success() throws Exception {
        given(roomService.findAllRooms(any(RoomFilterRequest.class), any(Long.class))).willReturn(PAGEABLE_RESPONSE);

        mockMvc.perform(get(BASE_PATH)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                USER_PRINCIPAL, null, USER_PRINCIPAL.getAuthorities()))))
                .andExpect(status().isOk())
                .andExpect(content().json(pageableResponseTester.write(PAGEABLE_RESPONSE).getJson()));

        then(roomService).should(times(1)).findAllRooms(any(RoomFilterRequest.class), any(Long.class));
    }

}
