package com.infraLink.API.service.user;


import com.infraLink.API.dto.request.user.UserRegisterRequest;
import com.infraLink.API.dto.response.user.UserRegisterResponse;
import com.infraLink.API.exception.user.UserAlreadyExistException;
import com.infraLink.API.model.entity.user.User;
import com.infraLink.API.model.repository.user.UserRepository;
import com.infraLink.API.model.roles.user.UserEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        user.setUserEnum(UserEnum.USER);

        userRepository.save(user);

        return new UserRegisterResponse(
              user.getName(),
              user.getEmail(),
              user.getCpf(),
              user.getCreateAt()
        );
    }

}
