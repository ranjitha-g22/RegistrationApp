package com.registrationjwt.service;

import com.registrationjwt.dto.RegDto;
import com.registrationjwt.dto.UpdateRegDto;
import com.registrationjwt.entity.Registration;
import com.registrationjwt.repository.RegistrationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private RegistrationRepository registrationRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthService(RegistrationRepository registrationRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.registrationRepository = registrationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> signup(Registration registration) {
        Optional<Registration> opUser=registrationRepository.findByUserName(registration.getUserName());
        Optional<Registration> opEmail=registrationRepository.findByEmailId(registration.getEmailId());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("email exists!!", HttpStatus.OK);
        }
        if(opUser.isPresent()){
            return new ResponseEntity<>("userName already exists!!", HttpStatus.OK);
        }
        String encodedPassword=passwordEncoder.encode(registration.getPassword());
        registration.setPassword(encodedPassword);
        registrationRepository.save(registration);
        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

    public String login(RegDto dto) {
        Optional<Registration> opUser=registrationRepository.findByUserName(dto.getUserName());
        Optional<Registration> opEmail=registrationRepository.findByEmailId(dto.getEmailId());
        Optional<Registration> opPass=registrationRepository.findByPassword(dto.getPassword());
        if(opUser.isPresent() && opEmail.isPresent()){
            Registration regUser = opUser.get();
            if(passwordEncoder.matches(dto.getPassword(), regUser.getPassword())){
                //return new ResponseEntity<>("login successful",HttpStatus.OK);
                return jwtService.generateToken(regUser.getUserName());
            }
        }
        return null;
    }

    public List<Registration> getall() {
       return  registrationRepository.findAll();
    }

    public ResponseEntity<?> finById(Long id) {
        Optional<Registration> op=registrationRepository.findById(id);
        if(op.isPresent()){
            Registration reg = op.get();
            return ResponseEntity.ok(reg);
        }else{
            return new ResponseEntity<>("id does not exists!!",HttpStatus.OK);
        }
    }

    public ResponseEntity<String> delete(Long id) {
        try{
            Optional<Registration> op=registrationRepository.findById(id);
            if(op.isPresent())
                registrationRepository.deleteById(id);
        }catch(Exception e){
            return new ResponseEntity<>("id doesnot exists!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("deleted!!",HttpStatus.OK);
    }

    public ResponseEntity<?> update(Long id, UpdateRegDto dto) {
        Optional<Registration> op=registrationRepository.findById(id);
        if(op.isPresent()){
            Registration reg = op.get();

            reg.setEmailId(dto.getEmailId());
            reg.setMobile(dto.getMobile());
            reg.setUserName(dto.getUserName());
           reg.setPassword(dto.getPassword());

            String encodedPass=passwordEncoder.encode(reg.getPassword());
            reg.setPassword(encodedPass);
            registrationRepository.save(reg);

            UpdateRegDto dt=new UpdateRegDto();
            dt.setUsername(reg.getUserName());
            dt.setMobile(reg.getMobile());
            dt.setEmailId(reg.getEmailId());
            dt.setPassword(reg.getPassword());
            return ResponseEntity.ok(dt);
        }else{
            return new ResponseEntity<>("id doesnot exits",HttpStatus.OK);
        }
    }
}
