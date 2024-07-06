package com.skillbox.zerone.controller;

import com.skillbox.zerone.dto.request.MessageCommonWs;
import com.skillbox.zerone.dto.request.MessageTypingWs;
import com.skillbox.zerone.dto.request.MessageWs;
import com.skillbox.zerone.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private static final String PATH = "/queue/messages";

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/dialogs/send_message")
    public void sendMessage(@Header("dialog_id") Long dialogId, @Payload MessageWs message) {
        message = messageService.processMessage(dialogId, message);
        messagingTemplate.convertAndSendToUser(dialogId.toString(), PATH, message);
    }

    @MessageMapping("/dialogs/start_typing")
    public void startTyping(@Header("dialog_id") Long dialogId, @Payload MessageTypingWs messageTypingWs) {
        messagingTemplate.convertAndSendToUser(dialogId.toString(), PATH, messageTypingWs);
    }

    @MessageMapping("/dialogs/stop_typing")
    public void stopTyping(@Header("dialog_id") Long dialogId, @Payload MessageTypingWs messageTypingWs) {
        messagingTemplate.convertAndSendToUser(dialogId.toString(), PATH, messageTypingWs);
    }

    @MessageMapping("/dialogs/edit_message")
    public void editMessage(@Payload MessageCommonWs messageCommonWs) {
        Long dialogId = messageService.editMessage(messageCommonWs);
        messagingTemplate.convertAndSendToUser(
                dialogId.toString(), PATH, messageCommonWs);
    }

    @MessageMapping("/dialogs/delete_messages")
    public void deleteMessages(@Payload MessageCommonWs messageCommonWs) {
        messageService.changeIsDeletedMessages(messageCommonWs);
        messagingTemplate.convertAndSendToUser(
                messageCommonWs.getDialogId().toString(), PATH, messageCommonWs);
    }

    @MessageMapping("/dialogs/recover_message")
    public void recoverMessage(@Payload MessageCommonWs messageCommonWs) {
        messageService.recoverMessage(messageCommonWs);
        messagingTemplate.convertAndSendToUser(
                messageCommonWs.getDialogId().toString(), PATH, messageCommonWs);
    }

    @MessageMapping("/dialogs/close_dialog")
    public void closeDialog(@Payload MessageCommonWs messageCommonWs) {
        messagingTemplate.convertAndSendToUser(
                messageCommonWs.getDialogId().toString(), PATH, messageCommonWs);
    }
}
