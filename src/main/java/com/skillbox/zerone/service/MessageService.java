package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.request.MessageCommonWs;
import com.skillbox.zerone.dto.request.MessageWs;
import com.skillbox.zerone.dto.response.MessageRs;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.MessageMapper;
import com.skillbox.zerone.mapper.PersonMapper;
import com.skillbox.zerone.repository.DialogRepository;
import com.skillbox.zerone.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final DialogRepository dialogRepository;
    private final PersonService personService;

    public Message createWelcomeMessage(Person author, Person recipient, Dialog dialog) {
        Message message = Message.builder()
                .author(author)
                .recipient(recipient)
                .dialog(dialog)
                .isDeleted(false)
                .readStatus(MessageStatus.UNREAD.toString())
                .messageText("Welcome to our dialog!")
                .time(LocalDateTime.now())
                .build();
        return messageRepository.save(message);
    }

    @Transactional
    public MessageWs processMessage(Long dialogId, MessageWs messageWs) {

        Dialog dialog = getDialogById(dialogId);
        Person recipient = (Objects.equals(dialog.getFirstPerson().getId(), messageWs.getAuthorId())) ? dialog.getSecondPerson()
                : dialog.getFirstPerson();
        Message message = MessageMapper.INSTANCE.toEntity(messageWs, dialog,personService.getPersonById(
                messageWs.getAuthorId()), recipient);
        Message savedMessage = messageRepository.save(message);
        dialog.setLastMessageId(savedMessage.getId());
        dialogRepository.save(dialog);
        return messageWs;
    }

    public Long editMessage(MessageCommonWs messageCommonWs) {
        Message message = getMessageById(messageCommonWs.getMessageId());
        message.setMessageText(messageCommonWs.getMessageText());
        Message savedMessage = messageRepository.save(message);
        return savedMessage.getId();
    }

    @Transactional
    public void changeIsDeletedMessages(MessageCommonWs messageCommonWs) {
        Dialog dialog = getDialogById(messageCommonWs.getDialogId());
        dialog.getMessageList().sort(Comparator.comparing(Message::getTime));
        for (Long id: messageCommonWs.getMessageIds()){
            Message message = getMessageById(id);
            List<Message> dialogMessageList = dialog.getMessageList();
            if (Objects.equals(message.getId(), dialog.getLastMessageId())) {
                Message lastMessage = dialogMessageList.get(dialogMessageList.size()-2);
                dialog.setLastMessageId(lastMessage.getId());
                dialogRepository.save(dialog);
                dialogMessageList.remove(lastMessage);
            }
            message.setIsDeleted(true);
            messageRepository.save(message);
        }
    }

    public void deleteMessages() {
        List<Message> messageList = messageRepository.findAllByIsDeleted(true);
        messageRepository.deleteAll(messageList);
    }

    public void recoverMessage(MessageCommonWs messageCommonWs) {
        Dialog dialog = getDialogById(messageCommonWs.getDialogId());
        Message message = getMessageById(messageCommonWs.getMessageId());
        Message lastMessage = getMessageById(dialog.getLastMessageId());
        message.setIsDeleted(false);
        messageRepository.save(message);
        if (message.getTime().isAfter(lastMessage.getTime())) {
            dialog.setLastMessageId(message.getId());
            dialogRepository.save(dialog);
        }
    }

    public MessageRs getMessageRsByMessageId(Long id, Person user) {
        Message message = getMessageById(id);
        Person recipient = (Objects.equals(message.getAuthor().getId(), user.getId())) ? message.getRecipient()
                :message.getAuthor();
        return MessageMapper.INSTANCE.toDto(message, isSentByMy(message, user),
                PersonMapper.INSTANCE.personToPersonDTO(recipient, null, null));
    }

    public Boolean isSentByMy(Message message, Person user) {
        return message.getAuthor().equals(user);
    }

    public Message getMessageById(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isEmpty()) {
            throw new BadRequestException("There is no message with id: " + id);
        }
        return message.get();
    }

    public Dialog getDialogById(Long dialogId) {
        Optional<Dialog> dialog = dialogRepository.findDialogById(dialogId);
        if (dialog.isEmpty()) {
            throw new BadRequestException("Dialog with id " + dialogId + " not found");
        }
        return dialog.get();
    }

    public void changeMessageStatusToRead(Message message) {
        message.setReadStatus(MessageStatus.READ.toString());
        messageRepository.save(message);
    }

    public Integer getCountUnreadMessages(Person user) {
        return messageRepository.findMessageByRecipientAndReadStatusAndIsDeleted(user, MessageStatus.UNREAD.toString(),
                false).size();
    }

    public int getCountUnreadMessagesByDialog(Dialog dialog, Person user) {
        return messageRepository.findMessageByDialogAndRecipientAndReadStatusAndIsDeleted(dialog, user,
                                MessageStatus.UNREAD.toString(), false).size();
    }

    public List<MessageRs> getMessageRsListByDialogId(Dialog dialog, Person user) {
       return messageRepository.findMessageByDialogAndIsDeleted(dialog, false).stream()
               .map( m -> getMessageRsByMessageId(m.getId(), user))
               .sorted(Comparator.comparing(MessageRs::getTime)).toList();
    }

    public List<MessageRs> getUnreadMessegeRsList(Dialog dialog, Person user) {
        return getMessageListByDialog(dialog, user).stream().map(
                m -> getMessageRsByMessageId(m.getId(), user)).toList();
    }

    public List<Message> getMessageListByDialog(Dialog dialog, Person user) {
        return messageRepository.findMessageByDialogAndRecipientAndReadStatusAndIsDeleted(dialog, user,
                MessageStatus.UNREAD.toString(), false);
    }
}
