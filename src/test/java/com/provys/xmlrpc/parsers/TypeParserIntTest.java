package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeParserIntTest {

    @Test
    void getElementTest() {
        assertThat(new TypeParserInt().getElement()).isEqualTo("int");
    }

    @Test
    void convertValueTest() {
        assertThat(new TypeParserInt().convertValue("-12")).isEqualTo(-12);
    }
}