package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.DialogUserShortListRq;
import com.skillbox.zerone.dto.response.ComplexRs;
import com.skillbox.zerone.dto.response.DialogRs;
import com.skillbox.zerone.dto.response.MessageRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.service.DialogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dialogs")
@LoggableInfo
public class DialogController {

    private final DialogService dialogService;

    @EndpointDescription(summary = "Read messages in dialog")
    @PutMapping(value = "/{dialogId}/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> changeMessagesStatus(@PathVariable Integer dialogId) {
        return dialogService.changeMessagesStatus(dialogId);
    }

    @EndpointDescription(summary = "Get dialogs by user")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<DialogRs>> getDialogsByUser() {
        return dialogService. getDialogsByUser();
    }

    @EndpointDescription(summary = "Start dialog with user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> createDialogWithUser(@RequestBody DialogUserShortListRq dialogUserShortListRq) {
        return dialogService.createDialogWithUser(dialogUserShortListRq);
    }

    @EndpointDescription(summary = "Get unread messages from dialog")
    @GetMapping(value = "/{dialogId}/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<MessageRs>> getUnreadMessages(@PathVariable Integer dialogId) {
        return dialogService.getUnreadMessages(dialogId);
    }

    @EndpointDescription(summary = "Get messages from dialog")
    @GetMapping(value = "/{dialogId}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<MessageRs>> getMessagesByDialogId(@PathVariable Integer dialogId, @RequestParam(defaultValue = "0")
                                        Integer offset, @RequestParam(defaultValue = "20") Integer perPage) {
        return dialogService.getMessagesByDialogId(dialogId, offset, perPage);
    }

    @EndpointDescription(summary = "Get count of unread messages")
    @GetMapping(value = "/unreaded", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> getCountUnreadMessages() {
        return dialogService.getCountUnreadMessages();
    }
}
