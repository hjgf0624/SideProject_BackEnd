package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.domain.CustomUser;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequestDTO;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailResponseDTO;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.service.MessageBroadcastService;
import com.github.hjgf0624.sideproject.service.MessageService;
import lombok.RequiredArgsConstructor;

import com.github.hjgf0624.sideproject.dto.BaseResponseDTO;
import com.github.hjgf0624.sideproject.dto.message.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/getMessagesByDate")
    public ResponseEntity<BaseResponseDTO<List<MessageGetResponseDTO>>> getMessagesByDate(@RequestBody MessageGetRequestDTO dto) {
        return ResponseEntity.ok(messageService.getMessagesByDate(dto));
    }

    @Operation(summary = "메세지 가져오기", description = "사용자 기준 주변에서 발급된 전체 메세지를 가져옵니다.")
    @PostMapping("/getMessages")
    public ResponseEntity<BaseResponseDTO<List<MessageGetResponseDTO>>> getMessages(@AuthenticationPrincipal CustomUser user) {
        String userId = user.getUserId();
        Double longitude = user.getUser().getLongitude();
        Double latitude = user.getUser().getLatitude();

        return ResponseEntity.ok(messageService.getMessages(userId, longitude, latitude));
    }

    @Operation(summary = "메세지 날짜 일괄 조회", description = "사용자 가입되어 있는 메세지 날짜를 일괄적으로 가져옵니다.")
    @PostMapping("/getMessageDate")
    public ResponseEntity<BaseResponseDTO<List<String>>> getMessageDate(@AuthenticationPrincipal CustomUser user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        System.out.println(auth.getPrincipal());

        String userId = user.getUserId();

        return ResponseEntity.ok(messageService.getMessageDate(userId));
    }

//    @Operation
//    @PostMapping("/getMe")
//    public String getMe(@AuthenticationPrincipal CustomUser user) {
//        return user.getUsername();
//    }
}

