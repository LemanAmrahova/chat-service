package com.leman.chatservice.dto.request;

import com.leman.chatservice.enums.UserRole;
import jakarta.validation.constraints.Size;
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
public class UserFilterRequest extends PageableRequest {

    @Size(min = 3, max = 20)
    private String username;

    private String email;
    private UserRole role;
    private Boolean enabled;

}
