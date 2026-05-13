package com.leman.chatservice.dto.request;

import com.leman.chatservice.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoomFilterRequest extends PageableRequest {

    private String name;
    private RoomType type;

}
