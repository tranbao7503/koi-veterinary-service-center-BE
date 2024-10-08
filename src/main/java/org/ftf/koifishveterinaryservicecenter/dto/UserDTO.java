package org.ftf.koifishveterinaryservicecenter.dto;

<<<<<<< HEAD

=======
import com.fasterxml.jackson.annotation.JsonIgnore;
>>>>>>> 6dc07be6fd1a294ec1075a5198e06e35682c81e3
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("username")
    public String username;

    @JsonProperty("email")
    public String email;

    @JsonProperty("phone_number")
    public String phoneNumber;

    @JsonProperty("avatar")
    public String avatar;

    @JsonProperty("address")
    public AddressDTO address;

<<<<<<< HEAD
=======

    public UserDTO(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
>>>>>>> 6dc07be6fd1a294ec1075a5198e06e35682c81e3
}
