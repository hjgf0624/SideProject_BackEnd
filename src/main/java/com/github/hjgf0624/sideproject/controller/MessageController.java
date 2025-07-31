package com.github.hjgf0624.sideproject.controller;

import com.github.hjgf0624.sideproject.config.security.domain.CustomUser;
import com.github.hjgf0624.sideproject.dto.CategorySimpleDTO;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequestDTO;
import com.github.hjgf0624.sideproject.dto.msg.MsgDetailResponseDTO;
import com.github.hjgf0624.sideproject.entity.MessageEntity;
import com.github.hjgf0624.sideproject.service.CategoryService;
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
    private final CategoryService categoryService;

    @Operation(summary = "메시지 세부사항 조회", description = "메시지의 세부사항을 조회합니다.")
    @PostMapping("/msgDetail")
    public MsgDetailResponseDTO getMessageDetail(@RequestBody MsgDetailRequestDTO request) {
        return messageService.getMessageDetail(request);
    }

    @Operation(summary = "메시지 작성", description = "사용자가 메시지를 작성합니다.")
    @PostMapping("/sendMessages")
    public ResponseEntity<BaseResponseDTO<Long>> saveMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        return ResponseEntity.ok(messageService.saveMessage(messageRequestDTO));
    }

    @Operation(summary = "광대역 푸시 메시지 전송", description = "주변 사용자에게 푸시 알림을 전송합니다")
    @PostMapping("/broadcasting")
    public ResponseEntity<Map<String, Object>> sendBroadcastMessage(@RequestBody MessageBroadcastRequestDTO request) {
        return ResponseEntity.ok(messageBroadcastService.broadcastMessage(request));
    }

    @Operation(summary = "참여", description = "사용자가 메시지에 참여합니다.")
    @PostMapping("/join")
    public ResponseEntity<Map<String, Boolean>> join(@RequestBody JoinMessageDTO dto) {
        return messageService.joinMessage(dto);
    }

    @Operation(summary = "날짜로 메세지 조회", description = "날짜로 사용자가 참여한 메세지를 조회합니다.")
    @PostMapping("/getMessagesByDate")
    public ResponseEntity<BaseResponseDTO<List<MessageGetResponseDTO>>> getMessagesByDate(@AuthenticationPrincipal CustomUser user, @RequestBody MessageGetRequestDTO dto) {
        String userId = user.getUserId();
        return ResponseEntity.ok(messageService.getMessagesByDate(userId, dto));
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
        String userId = user.getUserId();

        return ResponseEntity.ok(messageService.getMessageDate(userId));
    }

    @Operation(summary = "카테고리 일괄 조회", description = "서버에 저장된 카테고리를 가져옵니다.")
    @PostMapping("/getCategories")
    public ResponseEntity<BaseResponseDTO<List<CategorySimpleDTO>>> getCategories(@AuthenticationPrincipal CustomUser user) {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @Operation(summary = "카테고리 조회(이름)", description = "카테고리 이름을 통해 서버에 저장된 카테고리를 가져옵니다.")
    @PostMapping("/getCategoryByCategoryName")
    public ResponseEntity<BaseResponseDTO<CategorySimpleDTO>> getCategoryByCategoryName(@AuthenticationPrincipal CustomUser user, @RequestBody String categoryName) {
        return ResponseEntity.ok(categoryService.getCategoryByCategoryName(categoryName));
    }

//    @Operation
//    @PostMapping("/getMe")
//    public String getMe(@AuthenticationPrincipal CustomUser user) {
//        return user.getUsername();
//    }
}

