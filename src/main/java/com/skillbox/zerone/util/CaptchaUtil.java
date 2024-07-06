package com.skillbox.zerone.util;

import com.github.cage.Cage;
import com.github.cage.IGenerator;
import com.github.cage.IGeneratorFactory;
import com.github.cage.token.RandomCharacterGeneratorFactory;
import com.github.cage.token.RandomTokenGenerator;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class CaptchaUtil {

    private final char[] numbers = "0123456789".toCharArray();

    private final Cage cage = new Cage(null, null, null, null, 0.5F, createGenerator(), null);

    public String generateCaptcha() {
        return cage.getTokenGenerator().next();
    }

    public byte[] getImage(String captcha) {
        return cage.draw(captcha);
    }

    public String encodeCaptcha(String captcha) {
        return Base64.getEncoder().encodeToString(captcha.getBytes());
    }

    private IGenerator<String> createGenerator() {
        return new RandomTokenGenerator(new Random(), createGeneratorFactory(), 5, 1);
    }

    private IGeneratorFactory<Character> createGeneratorFactory() {
        Map<Character, char[]> characterMap = new HashMap<>();
        characterMap.put('0', numbers);
        characterMap.put('1', numbers);
        characterMap.put('2', numbers);
        characterMap.put('3', numbers);
        characterMap.put('4', numbers);
        characterMap.put('5', numbers);
        characterMap.put('6', numbers);
        characterMap.put('7', numbers);
        characterMap.put('8', numbers);
        characterMap.put('9', numbers);
        return new RandomCharacterGeneratorFactory(numbers, characterMap, new SecureRandom());
    }
}
