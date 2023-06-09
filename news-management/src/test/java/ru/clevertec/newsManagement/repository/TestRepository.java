package ru.clevertec.newsManagement.repository;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.newsManagement.TestConfig;

@ActiveProfiles("repository_test")
@Testcontainers
@SpringBootTest(classes = TestConfig.class)
public class TestRepository {

}