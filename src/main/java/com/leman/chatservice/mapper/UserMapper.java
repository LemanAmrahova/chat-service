package com.leman.chatservice.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

import com.leman.chatservice.dto.request.RegisterRequest;
import com.leman.chatservice.dto.response.PageableResponse;
import com.leman.chatservice.dto.response.UserResponse;
import com.leman.chatservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = SPRING,
        unmappedSourcePolicy = IGNORE,
        unmappedTargetPolicy = IGNORE,
        injectionStrategy = CONSTRUCTOR
)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);

    PageableResponse<UserResponse> toPageableResponse(Page<User> page);

}
