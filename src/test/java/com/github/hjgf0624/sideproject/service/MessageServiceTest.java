//package com.github.hjgf0624.sideproject.service;
//
//import com.github.hjgf0624.sideproject.dto.LocationDTO;
//import com.github.hjgf0624.sideproject.dto.message.JoinMessageDTO;
//import com.github.hjgf0624.sideproject.dto.message.MessageGetRequestDTO;
//import com.github.hjgf0624.sideproject.dto.message.MessageRequestDTO;
//import com.github.hjgf0624.sideproject.dto.msg.MsgDetailRequest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@SpringBootTest
//@ExtendWith(SpringExtension.class)
//public class MessageServiceTest {
//    @Autowired
//    private MessageService service;
//
//    @Test
//    public void testGetMessageDetail() throws Exception {
//        MessageGetRequestDTO messageGetRequestDTO = new MessageGetRequestDTO();
//        messageGetRequestDTO.setMessageId(16L);
//        messageGetRequestDTO.setDate(LocalDate.parse("2024-04-21"));
//
//        service.getMessage(messageGetRequestDTO);
//    }
//}
