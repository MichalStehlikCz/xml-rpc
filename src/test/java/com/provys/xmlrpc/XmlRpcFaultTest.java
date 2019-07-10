package com.provys.xmlrpc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class XmlRpcFaultTest {

    @Test
    void mapConstructorTest() {
        var map = new HashMap<String, Object>(2);
        map.put("faultCode", -1);
        map.put("faultString", "Fault");
        var fault = new XmlRpcFault(map);
        assertThat(fault.getFaultCode()).isEqualTo(-1);
        assertThat(fault.getFaultString()).isEqualTo("Fault");
    }

    @Test
    void getFaultCodeTest() {
        assertThat(new XmlRpcFault(10, "Test").getFaultCode()).isEqualTo(10);
    }

    @Test
    void getFaultStringTest() {
        assertThat(new XmlRpcFault(10, "Test").getFaultString()).isEqualTo("Test");
    }

    @Test
    void getStructTest() {
        assertThat(new XmlRpcFault(4, "My fault").getStruct()).containsExactly(
                Map.entry("faultCode", 4), Map.entry("faultString", "My fault"));
    }

    @Test
    void getExceptionTest() {
        assertThat(new XmlRpcFault(7, "Exception").getException()).
                isEqualTo(new XmlRpcFaultException(7, "Exception"));
    }
}