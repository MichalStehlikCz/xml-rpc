package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeParserDoubleTest {

    @Test
    void getElementTest() {
        assertThat(new TypeParserDouble().getElement()).isEqualTo("double");
    }

    @Test
    void convertValueTest() {
        assertThat(new TypeParserDouble().convertValue("-12.214")).isEqualTo(-12.214);
    }
}