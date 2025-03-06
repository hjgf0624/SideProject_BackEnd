package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.message.*;
import com.github.hjgf0624.sideproject.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Map;

@Tag(name = "message", description = "메세지와 관련된 API 입니다.")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation
    @PostMapping("/sendMessages")
    public ResponseEntity<BaseResponseDTO<MessageResponseDTO>> saveMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        return ResponseEntity.ok(messageService.saveMessage(messageRequestDTO));
    }

    @Operation
    @PostMapping("/join")
    public ResponseEntity<Map<String, Boolean>> join(@RequestBody JoinMessageDTO dto) {
        return messageService.joinMessage(dto);
    }

    @Operation
    @PostMapping("/getMessages")
    public ResponseEntity<BaseResponseDTO<MessageGetResponseDTO>> getMessages(@RequestBody MessageGetRequestDTO dto) {
        return ResponseEntity.ok(messageService.getMessage(dto));
    }

}
