package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeParserI4Test {

    @Test
    void getElementTest() {
        assertThat(new TypeParserI4().getElement()).isEqualTo("i4");
    }

    @Test
    void convertValueTest() {
        assertThat(new TypeParserI4().convertValue("-12")).isEqualTo(-12);
    }
}