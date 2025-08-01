package com.github.hjgf0624.sideproject.service;

import com.github.hjgf0624.sideproject.dto.LocationDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AlarmServiceTest {

    @Autowired
    private AlarmService alarmService;

    @Test
    public void alarmTest() {
        LocationDTO dto = new LocationDTO();
        dto.setLatitude(37.123);
        dto.setLongitude(-122.456);

    }
}
