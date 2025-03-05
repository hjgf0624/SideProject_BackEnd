package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequest;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailResponse;
import com.github.hjgf0624.sideproject.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/msgDetail")
    public MsgDetailResponse getMessageDetail(@RequestBody MsgDetailRequest request) {
        return messageService.getMessageDetail(request);
    }
}