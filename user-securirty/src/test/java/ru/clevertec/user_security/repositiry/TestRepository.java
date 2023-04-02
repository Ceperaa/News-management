package ru.clevertec.user_security.repositiry;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.user_security.controllers.TestConfig;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(classes = TestConfig.class)
public class TestRepository {

}