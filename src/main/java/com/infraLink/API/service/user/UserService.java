package com.infraLink.API.service.user;


import com.infraLink.API.config.TokenConfig;
import com.infraLink.API.dto.request.user.UserLoginRequest;
import com.infraLink.API.dto.request.user.UserRegisterRequest;
import com.infraLink.API.dto.response.user.UserLoginResponse;
import com.infraLink.API.dto.response.user.UserRegisterResponse;
import com.infraLink.API.exception.user.UserAlreadyExistException;
import com.infraLink.API.exception.user.UserNotFoundException;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.repository.user.UserRepository;
import com.infraLink.API.model.roles.user.UserEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }


    public UserRegisterResponse register(UserRegisterRequest request){

        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new UserAlreadyExistException();
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setCpf(request.cpf());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreateAt(LocalDateTime.now());
        user.setUserEnum(UserEnum.CLIENT);

        userRepository.save(user);

        return new UserRegisterResponse(
              user.getName(),
              user.getEmail(),
              user.getCpf(),
              user.getCreateAt()
        );
    }

    public UserLoginResponse login(UserLoginRequest request){
        if(userRepository.findByEmail(request.email()).isEmpty()){
            throw new UserNotFoundException();
        }

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return new UserLoginResponse(token);

    }



}
