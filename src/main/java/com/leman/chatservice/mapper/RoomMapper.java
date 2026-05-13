package com.leman.chatservice.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

import com.leman.chatservice.dto.request.RoomCreateRequest;
import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.RoomResponse;
import com.leman.chatservice.dto.response.UserSearchResponse;
import com.leman.chatservice.entity.Room;
import com.leman.chatservice.entity.RoomMember;
import com.leman.chatservice.entity.User;
import com.leman.chatservice.enums.MemberRole;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = IGNORE,
        injectionStrategy = CONSTRUCTOR
)
public interface RoomMapper {

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "type", constant = "GROUP")
    Room toEntity(RoomCreateRequest request);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "inviteLink", ignore = true)
    RoomResponse toBaseResponse(Room room);

    default RoomResponse toResponse(Room room, Long requesterId) {
        RoomResponse response = toBaseResponse(room);
        response.setInviteLink(isOwner(room, requesterId) ? room.getInviteLink() : null);
        response.setMembers(mapMembers(room));
        return response;
    }

    default PageableResponse<RoomResponse> toPageableResponse(Page<RoomResponse> page) {
        return PageableResponse.<RoomResponse>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }

    default RoomMember toMemberEntity(Room room, User user, MemberRole role) {
        return RoomMember.builder()
                .room(room)
                .user(user)
                .role(role)
                .build();
    }

    private boolean isOwner(Room room, Long requesterId) {
        return room.getMembers().stream()
                .anyMatch(m -> m.getUser().getId().equals(requesterId)
                        && m.getRole() == MemberRole.OWNER);
    }

    private List<UserSearchResponse> mapMembers(Room room) {
        return room.getMembers().stream()
                .map(m -> UserSearchResponse.builder()
                        .id(m.getUser().getId())
                        .username(m.getUser().getUsername())
                        .build())
                .toList();
    }

}
