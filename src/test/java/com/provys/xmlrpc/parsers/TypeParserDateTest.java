package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TypeParserDateTest {

    @Test
    void getElementTest() {
        assertThat(new TypeParserDate().getElement()).isEqualTo("dateTime.iso8601");
    }

    @Test
    void convertValueTest() {
        assertThat(new TypeParserDate().convertValue("19980717T14:08:55"))
                .isEqualTo(LocalDateTime.of(1998, 7, 17, 14, 8, 55));
    }

    @Test
    void convertValueTZTest() {
        assertThat(new TypeParserDate().convertValue("2019-06-27T18:15:48+0000"))
                .isEqualTo(LocalDateTime.of(2019, 6, 27, 18, 15, 48));
    }
}