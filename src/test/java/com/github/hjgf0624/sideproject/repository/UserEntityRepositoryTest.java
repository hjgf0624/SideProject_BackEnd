package com.github.hjgf0624.sideproject.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserEntityRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ DB 연결 성공: " + connection.getMetaData().getURL());
        }
    }
}
