package com.infraLink.API.controller.user;


import com.infraLink.API.dto.request.user.UserLoginRequest;
import com.infraLink.API.dto.request.user.UserRegisterRequest;
import com.infraLink.API.dto.response.user.UserLoginResponse;
import com.infraLink.API.dto.response.user.UserRegisterResponse;
import com.infraLink.API.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public UserRegisterResponse register(@RequestBody UserRegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request);
    }
mit

}
