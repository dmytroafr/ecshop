package com.echem.ecshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    
    @NotBlank(message = "Поточний пароль обов'язковий")
    private String currentPassword;
    
    @NotBlank(message = "Новий пароль обов'язковий")
    @Size(min = 6, message = "Пароль повинен містити не менше 6 символів")
    private String newPassword;
    
    @NotBlank(message = "Підтвердження паролю обов'язкове")
    private String confirmPassword;
}
