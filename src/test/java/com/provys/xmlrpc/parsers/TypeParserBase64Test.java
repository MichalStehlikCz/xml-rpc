package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeParserBase64Test {

    @Test
    void getElementTest() {
        assertThat(new TypeParserBase64().getElement()).isEqualTo("base64");
    }

    @Test
    void convertValueTest() {
        assertThat(new String(new TypeParserBase64().convertValue("eW91IGNhbid0IHJlYWQgdGhpcyE=")))
                .isEqualTo("you can't read this!");
    }
}