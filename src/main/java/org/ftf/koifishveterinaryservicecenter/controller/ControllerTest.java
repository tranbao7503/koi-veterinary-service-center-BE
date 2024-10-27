package org.ftf.koifishveterinaryservicecenter.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {

    @GetMapping("/hello")
    public String home() {
        return "Hello,name";
    }

    @GetMapping("secured")
    public String secured() {
        return "Ok";
    }

    @GetMapping("cc")
    public String cc() {
        return "cc";
    }

    @GetMapping("login/oauth2/code/google")
    public String googleLogin(OAuth2AuthenticationToken token) {
        // Lấy thông tin người dùng từ token
        String email = token.getPrincipal().getAttribute("email");
        String name = token.getPrincipal().getAttribute("name");

        // Tiến hành tạo hoặc xác thực người dùng trong cơ sở dữ liệu

        return "Welcome " + name;
    }

}
