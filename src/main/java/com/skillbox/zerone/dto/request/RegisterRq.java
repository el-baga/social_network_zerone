package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.skillbox.zerone.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRq {

    @NotBlank(message = "Поле не может быть пустым")
    @Length(message = "Длина имени должна быть от 2 до 20 символов", min = 2, max = 20)
    private String firstName;

    @NotBlank(message = "Поле не может быть пустым")
    @Length(message = "Длина фамилии должна быть от 2 до 20 символов", min = 2, max = 20)
    private String lastName;

    @Email(message = "Неверный формат email", regexp = "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,6}$")
    private String email;

    @Password(message = "Неверный формат пароля. Пароль должен состоять из букв, цифр и символов. Обязательно содержать заглавную латинскую букву, цифру и иметь длину не менее 8 символов")
    @NotBlank(message = "Поле не может быть пустым")
    @JsonAlias("passwd1")
    private String password;

    @NotBlank(message = "Поле не может быть пустым")
    @JsonAlias("passwd2")
    private String confirmPassword;

    private String code;

    private String codeSecret;

}
