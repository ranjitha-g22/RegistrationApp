package com.registrationjwt.repository;

import com.registrationjwt.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByUserName(String userName);

    Optional<Registration> findByEmailId(String emailId);

    Optional<Registration> findByPassword(String password);
}
