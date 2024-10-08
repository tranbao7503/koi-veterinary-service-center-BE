package org.ftf.koifishveterinaryservicecenter.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("password")
    public String password;


    @JsonProperty("username")
    public String username;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phone_number")
    public String phoneNumber;


    @JsonProperty("enable")
    public boolean enable;

    @JsonProperty("address")
    public AddressDTO address;

    @JsonProperty("avatar")
    public String avatar;


    public boolean isEnabled() {
        return enable;
    }
}
