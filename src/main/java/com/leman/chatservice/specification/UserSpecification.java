package com.leman.chatservice.specification;

import com.leman.chatservice.dto.request.UserFilterRequest;
import com.leman.chatservice.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.leman.chatservice.specification.BaseSpecification.combineWithAnd;
import static com.leman.chatservice.specification.BaseSpecification.equalIfNotNull;
import static com.leman.chatservice.specification.BaseSpecification.likeIfNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {

    public static Specification<User> getSpecification(UserFilterRequest request) {
        return (root, query, cb) -> combineWithAnd(cb,
                equalIfNotNull(cb, root.get("role"), request.getRole()),
                equalIfNotNull(cb, root.get("enabled"), request.getEnabled()),
                likeIfNotBlank(cb, root.get("username"), request.getUsername()),
                likeIfNotBlank(cb, root.get("email"), request.getEmail())
        );
    }

}
