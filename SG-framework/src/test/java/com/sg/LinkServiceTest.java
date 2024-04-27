package com.sg;

import com.sg.domain.dto.LinkTestDate;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author qi_coding
 * @version 1.00
 * @time 2024/4/5 20:56
 */
/*
java.lang.IllegalStateException: Unable to find a
 @SpringBootConfiguration, you need to use @ContextConfiguration or @SpringBootTest(classes=...) with your test
 */
//@SpringBootTest
public class LinkServiceTest {



    @Test
    public void testLinkDate(){
        LinkTestDate linkTestDate = new LinkTestDate();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        System.out.println(format);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Instant instant = date.toInstant();
//        System.out.println(instant); //2024-04-05T13:14:57.489Z
//        dateTimeFormatter.format(date.toInstant());//Unsupported field: YearOfEra
        System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
        linkTestDate.setCreateTime(new Date());

    }

}
