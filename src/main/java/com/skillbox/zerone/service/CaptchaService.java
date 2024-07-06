package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.response.CaptchaRs;
import com.skillbox.zerone.dto.request.RegisterRq;
import com.skillbox.zerone.entity.Captcha;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.repository.CaptchaRepository;
import com.skillbox.zerone.util.CaptchaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    private final CaptchaUtil captchaUtil;

    public CaptchaRs sendCaptchaDto() {
        String captcha = captchaUtil.generateCaptcha();
        String code = captchaUtil.encodeCaptcha(captcha);
        createCaptcha(captcha, code);
        byte[] bytes = captchaUtil.getImage(captcha);
        String image = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        return CaptchaRs.builder()
                .code(code)
                .image(image)
                .build();
    }

    private void createCaptcha(String captcha, String code) {
        Captcha captchaEntity = new Captcha();
        captchaEntity.setTime(LocalDateTime.now());
        captchaEntity.setCode(captcha);
        captchaEntity.setSecretCode(code);
        captchaRepository.save(captchaEntity);
    }

    public void checkCaptcha(RegisterRq registerRq) {
        String encode = Base64.getEncoder().encodeToString(registerRq.getCode().getBytes());
        if (!encode.equals(registerRq.getCodeSecret())) {
            throw new BadRequestException("Введенный код не совпадает с кодом картинки");
        }
        if (!checkCaptchaExpirationTime(registerRq)) {
            throw new BadRequestException("Капча устарела, обновите капчу и повторите ввод");
        }
    }

    public boolean checkCaptchaExpirationTime(RegisterRq registerRq) {
        LocalDateTime currentTime = LocalDateTime.now();
        String secretCode = registerRq.getCodeSecret();
        List<Captcha> captchaList = captchaRepository.findBySecretCode(secretCode);
        for (Captcha captcha : captchaList) {
            if (currentTime.isAfter(captcha.getTime().plusMinutes(2))) {
                return false;
            }
        }
        return true;
    }
}
