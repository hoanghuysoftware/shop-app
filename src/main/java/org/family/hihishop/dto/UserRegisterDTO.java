package org.family.hihishop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required for registration !")
    private String phoneNumber;

    @NotBlank(message = "Address is required for registration !")
    private String address;

    @NotBlank(message = "Password cannot be empty !")
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;

    @JsonProperty("role_id")
    @NotNull(message = "role_id is required for registration !")
    private Long roleId;
}
