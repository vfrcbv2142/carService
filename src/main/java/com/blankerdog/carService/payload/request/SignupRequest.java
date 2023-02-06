package com.blankerdog.carService.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String login;

  @NotBlank
      @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,40}",
            message = "Must be minimum 6 characters, at least one letter and one number")
  private String password;
}
