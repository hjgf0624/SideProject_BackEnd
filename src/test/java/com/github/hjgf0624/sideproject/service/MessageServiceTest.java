package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.LocationDTO;
import com.github.hjgf0624.sideproject.dto.message.JoinMessageDTO;
import com.github.hjgf0624.sideproject.dto.message.MessageRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MessageServiceTest {
    @Autowired
    private MessageService service;

    @Test
    public void testSaveMessage() throws Exception {
        JoinMessageDTO joinMessageDTO = new JoinMessageDTO();

        joinMessageDTO.setMessageId(3);
        joinMessageDTO.setUserId("test2@example.com");

        // too many connection 에러해결해야함
        service.joinMessage(joinMessageDTO);
    }
}
