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

    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phone_number")
    public String phoneNumber;

    @JsonProperty("avatar")
    public String avatar;

    @JsonProperty("address")
    public AddressDTO address;


    public UserDTO(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
}
