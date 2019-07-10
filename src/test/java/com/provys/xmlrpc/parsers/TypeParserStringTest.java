package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeParserStringTest {

    @Test
    void getElementTest() {
        assertThat(new TypeParserString().getElement()).isEqualTo("string");
    }

    @Test
    void convertValueTest() {
        assertThat(new TypeParserString().convertValue("abcd")).isEqualTo("abcd");
    }
}