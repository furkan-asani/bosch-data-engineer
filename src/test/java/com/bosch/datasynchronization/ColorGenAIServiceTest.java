package com.bosch.datasynchronization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@SpringBootTest
class ColorGenAIServiceTest {

    @Autowired
    private ColorGenAIService _colorGenAIService;

    @Test
    void testHappyPath() {
        //given

        //when
         _colorGenAIService.getColorForProducts();

        //then
    }
}