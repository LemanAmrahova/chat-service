package com.leman.chatservice.specification;

import static com.leman.chatservice.specification.BaseSpecification.combineWithAnd;
import static com.leman.chatservice.specification.BaseSpecification.likeIfNotBlank;

import com.leman.chatservice.dto.request.UserSearchRequest;
import com.leman.chatservice.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {

    public static Specification<User> getSpecification(UserSearchRequest request) {
        return (root, query, cb) -> combineWithAnd(cb,
                likeIfNotBlank(cb, root.get("username"), request.getUsername())
        );
    }

}
