package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserRecordDto.UserView.RegistrationPost.class)
                                               @JsonView(UserRecordDto.UserView.RegistrationPost.class)
                                               UserRecordDto userRecordDto) {
        log.debug("POST registerUser userRecordDto received {}", userRecordDto);

        if(userService.existsByUsername(userRecordDto.username())) {
            log.warn("Username {} is already taken", userRecordDto.username());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken!");
        }
        if(userService.existsByEmail(userRecordDto.email())){
            log.warn("Email {} is already taken", userRecordDto.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDto));
    }

    @GetMapping("/logs")
    public String index(){
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");

        return "Logging Spring Boot...";
    }
}
