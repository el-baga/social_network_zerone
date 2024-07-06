package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.request.DialogUserShortListRq;
import com.skillbox.zerone.dto.response.*;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.DialogMapper;
import com.skillbox.zerone.repository.DialogRepository;
import com.skillbox.zerone.specification.Specs;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DialogService {
    private final PersonService personService;
    private final DialogRepository dialogRepository;
    private final MessageService messageService;

    public PageRs<ComplexRs> getCountUnreadMessages() {
        Person user = personService.getPersonById(CommonUtil.getCurrentUserId());
            return PageRs.<ComplexRs>builder()
                    .data(ComplexRs.builder().count(messageService.getCountUnreadMessages(user)).build())
                    .build();
    }

    public PageRs<List<DialogRs>> getDialogsByUser() {
        Person user = personService.getPersonById(CommonUtil.getCurrentUserId());
        List<DialogRs> dialogRsList = dialogRepository.findDialogByFirstPersonOrSecondPerson(user, user).stream()
            .map(d -> {
                    int unreadCount = messageService.getCountUnreadMessagesByDialog(d, user);
                    String readStatus = (unreadCount > 0) ? MessageStatus.UNREAD.toString()
                                                            : MessageStatus.READ.toString();
                    MessageRs lastMessage = messageService.getMessageRsByMessageId(d.getLastMessageId(), user);
                    return DialogMapper.INSTANCE.toDto(d, lastMessage, readStatus, unreadCount);
                }
            ).toList();
        return PageRs.<List<DialogRs>>builder()
                .data(dialogRsList)
                .total((long) dialogRsList.size())
                .build();
    }

    public PageRs<ComplexRs> createDialogWithUser(DialogUserShortListRq dialogUserShortListRq) {
        Person user = personService.getPersonById(CommonUtil.getCurrentUserId());
        for(Integer userIds :dialogUserShortListRq.getUserIds()) {
            Person recipient = personService.getPersonById(userIds);
            List<Dialog> dialogs = dialogRepository.findAll(Specification
                    .where(Specs.eq(Dialog_.firstPerson, user).or(Specs.eq(Dialog_.firstPerson, recipient)))
                    .and(Specs.eq(Dialog_.secondPerson, user).or(Specs.eq(Dialog_.secondPerson, recipient))));
            if(dialogs.isEmpty()) {
                Dialog dialog = Dialog.builder()
                        .firstPerson(user)
                        .secondPerson(recipient)
                        .lastActiveTime(LocalDateTime.now())
                        .build();
                Dialog savedDialog = dialogRepository.save(dialog);
                List<Message> messageList = new ArrayList<>();
                Message message = messageService.createWelcomeMessage(user, recipient, savedDialog);
                messageList.add(message);
                savedDialog.setMessageList(messageList);
                savedDialog.setLastMessageId(message.getId());
                dialogRepository.save(savedDialog);
            }
        }
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().build())
                .build();
    }

    public PageRs<List<MessageRs>> getMessagesByDialogId(Integer dialogId, Integer offset, Integer perPage) {
        Person user = personService.getPersonById(CommonUtil.getCurrentUserId());
        List<MessageRs> messageRsList = messageService.getMessageRsListByDialogId(getDialogById(Long.valueOf(dialogId)),
                user);
        return PageRs.<List<MessageRs>>builder()
                .data(messageRsList)
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total((long) messageRsList.size())
                .build();
    }

    public PageRs<List<MessageRs>> getUnreadMessages(Integer dialogId) {
        Person user = personService.getPersonById(CommonUtil.getCurrentUserId());
        List<MessageRs> messageRsList = messageService.getUnreadMessegeRsList(getDialogById(Long.valueOf
                        (dialogId)), user);
        return PageRs.<List<MessageRs>>builder()
                .data(messageRsList)
                .total((long) messageRsList.size())
                .build();
    }

    public PageRs<ComplexRs> changeMessagesStatus(Integer dialogId) {
        Person user = personService.getPersonById(CommonUtil.getCurrentUserId());
        List<Message> messageList = messageService.getMessageListByDialog(getDialogById(Long.valueOf
                (dialogId)),user);
        for (Message message: messageList) {
            messageService.changeMessageStatusToRead(message);
        }
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().message("Message status change to READ in dialog with id " + dialogId).build())
                .build();
    }

    public Dialog getDialogById(Long dialogId) {
        Optional<Dialog> dialog = dialogRepository.findDialogById(dialogId);
        if (dialog.isEmpty()) {
            throw new BadRequestException("Dialog with id " + dialogId + " not found");
        }
        return dialog.get();
    }
}
