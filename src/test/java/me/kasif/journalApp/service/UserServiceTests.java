package me.kasif.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

//    @Disabled
    @Test
    public void testAdd() {
        assertEquals(4, 2 + 2);
        assertTrue(5 > 2);
        // assertNotNull(userService.findByUserName (("username")));
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "3,1,2",
            "5,2,3",
            "5,7,9" // fail
    })
    public void test(int expected, int a, int b) {
        assertEquals(expected, a + b );
    }

}
