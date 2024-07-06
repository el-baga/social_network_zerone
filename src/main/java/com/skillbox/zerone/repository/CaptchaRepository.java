package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {

    List<Captcha> findBySecretCode(String secretCode);

    List<Captcha> findByTimeBefore(LocalDateTime time);
}
