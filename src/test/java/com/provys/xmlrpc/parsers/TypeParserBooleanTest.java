package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeParserBooleanTest {

    @Test
    void getElementTest() {
        assertThat(new TypeParserBoolean().getElement()).isEqualTo("boolean");
    }

    @Test
    void convertValueTest() {
        assertThat(new TypeParserBoolean().convertValue("0")).isFalse();
        assertThat(new TypeParserBoolean().convertValue("1")).isTrue();
    }
}