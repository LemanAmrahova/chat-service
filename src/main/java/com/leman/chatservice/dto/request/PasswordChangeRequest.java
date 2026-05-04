package com.leman.chatservice.dto.request;

import com.leman.chatservice.annotation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

    @NotBlank
    @Password
    private String currentPassword;

    @NotBlank
    @Password
    private String newPassword;

}
