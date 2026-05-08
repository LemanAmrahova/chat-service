package com.leman.chatservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstant {

    public static final Long ID = 1L;
    public static final Integer PAGE = 0;
    public static final Integer SIZE = 10;
    public static final String SORT_BY = "id";
    public static final Sort.Direction SORT_DIRECTION = Sort.Direction.ASC;

}
