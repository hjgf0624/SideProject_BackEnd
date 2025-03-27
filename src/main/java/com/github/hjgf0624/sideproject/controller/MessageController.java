package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequestDTO;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailResponseDTO;
import com.github.hjgf0624.sideproject.service.MessageBroadcastService;
import com.github.hjgf0624.sideproject.service.MessageService;
import lombok.RequiredArgsConstructor;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.message.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "message", description = "메세지와 관련된 API 입니다.")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageBroadcastService messageBroadcastService;

    @PostMapping("/msgDetail")
    public MsgDetailResponseDTO getMessageDetail(@RequestBody MsgDetailRequestDTO request) {
        return messageService.getMessageDetail(request);
    }

    @Operation
    @PostMapping("/sendMessages")
    public ResponseEntity<BaseResponseDTO<MessageResponseDTO>> saveMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        return ResponseEntity.ok(messageService.saveMessage(messageRequestDTO));
    }

    @Operation
    @PostMapping("/broadcasting")
    public ResponseEntity<Map<String, Object>> sendBroadcastMessage(@RequestBody MessageBroadcastRequestDTO request) {
        return ResponseEntity.ok(messageBroadcastService.broadcastMessage(request));
    }

    @Operation
    @PostMapping("/join")
    public ResponseEntity<Map<String, Boolean>> join(@RequestBody JoinMessageDTO dto) {
        return messageService.joinMessage(dto);
    }

    @Operation
    @PostMapping("/getMessages")
    public ResponseEntity<BaseResponseDTO<List<MessageGetResponseDTO>>> getMessages(@RequestBody MessageGetRequestDTO dto) {
        return ResponseEntity.ok(messageService.getMessage(dto));
    }

}

