package com.registrationjwt.controller;

import com.registrationjwt.dto.JwtTokenDto;
import com.registrationjwt.dto.RegDto;
import com.registrationjwt.dto.UpdateRegDto;
import com.registrationjwt.entity.Registration;
import com.registrationjwt.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regis")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //http://localhost:8080/api/regis/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody Registration registration
            ){
        return authService.signup(registration);
    }
    //http://localhost:8080/api/regis/login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody RegDto dto
    ){
        String jwtToken=  authService.login(dto);
        if(jwtToken!=null){
            JwtTokenDto tokendto=new JwtTokenDto();
            tokendto.setToken(jwtToken);
            tokendto.setTokenType("JWT");
            return new ResponseEntity<>(tokendto, HttpStatus.CREATED);

        }
        return  new ResponseEntity<>("invalid token",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //http://localhost:8080/api/regis/all
    @GetMapping("/all")
    public List<Registration> getall(){
        List<Registration> reg=authService.getall();
        return reg;
    }
    //http://localhost:8080/api/regis/byid?id=1
    @GetMapping("/byid")
    public ResponseEntity<?> getById(
            @RequestParam Long id
    ){
       return authService.finById(id);
    }
    //http://localhost:8080/api/regis/del/2
    @DeleteMapping("/del/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id
    ){
        return authService.delete(id);
    }
    //http://localhost:8080/api/regis/update?id=1
    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody UpdateRegDto dto,
            @RequestParam Long id
    ){
        return  authService.update(id,dto);
    }

}
