package com.skillbox.zerone.message;

public enum MessageType {
    RESET_EMAIL_SUBJECT("Zerone.Ссылка на изменение почты"),
    RESET_EMAIL_FORMAT_LINK("<a href=\"%s/shift-email?token=%s \"target=\"_blank\">Reset email</a>"),
    RESET_PASSWORD_SUBJECT("Zerone.Ссылка на изменение пароля"),
    RESET_PASSWORD_FORMAT_LINK("<a href=\"%s/change-password?token=%s \"target=\"_blank\">Change password</a>"),
    EMAIL_UUID_IS_NOT_FOUND_ERROR("Используемый секретный код не соответствует ранее сгенерированному! Перейдите по ссылке, находящейся в самом раннем письме восстановления почты."),
    EMAIL_UUID_EXPIRED_ERROR("Истек срок действия секретного кода! Перейдите в аккаунт для повторной отправки письма с ссылкой восстановления почты."),
    EMAIL_IS_ALREADY_TAKEN_ERROR("Указанная почта уже занята! Попробуйте ввести другую почту."),
    PASSWORD_TOKEN_IS_NOT_FOUND_ERROR("Используемый секретный код не соответствует ранее сгенерированному! Перейдите по ссылке, находящейся в самом раннем письме восстановления пароля."),
    PASSWORD_TOKEN_EXPIRED_ERROR("Истек срок действия секретного кода! Перейдите в аккаунт для повторной отправки письма с ссылкой восстановления пароля.");

    private final String text;

    MessageType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
