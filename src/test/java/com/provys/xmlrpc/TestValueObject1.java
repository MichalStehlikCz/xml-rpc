package com.provys.xmlrpc;

/**
 * Class used to test serialization
 */
@SuppressWarnings("unused") // class is used to test object serializer, that uses reflection
public class TestValueObject1 {
    private final int property1;
    private final String property2;

    public TestValueObject1(int property1, String property2) {
        this.property1 = property1;
        this.property2 = property2;
    }

    public int getProperty1() {
        return property1;
    }

    public String getProperty2() {
        return property2;
    }
}
