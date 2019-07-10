package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcFault;
import com.provys.xmlrpc.XmlRpcResponse;
import com.provys.xmlrpc.XmlRpcResponseBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class XmlRpcResponseReaderTest {

    @Nonnull
    static Stream<Object[]> readTest() {
        return Stream.of(
                new Object[]{"<?xml version=\"1.0\"?>\n" +
                        "<methodResponse>\n" +
                        "   <params>\n" +
                        "      <param>\n" +
                        "         <value><string>South Dakota</string></value>\n" +
                        "      </param>\n" +
                        "   </params>\n" +
                        "</methodResponse>",
                        new XmlRpcResponse("South Dakota")}
                , new Object[]{"<?xml version=\"1.0\"?>\n" +
                        "<methodResponse>\n" +
                        "   <fault>\n" +
                        "      <value>\n" +
                        "         <struct>\n" +
                        "            <member>\n" +
                        "               <name>faultCode</name>\n" +
                        "               <value><int>4</int></value>\n" +
                        "            </member>\n" +
                        "            <member>\n" +
                        "               <name>faultString</name>\n" +
                        "               <value><string>Too many parameters.</string></value>\n" +
                        "            </member>\n" +
                        "         </struct>\n" +
                        "      </value>\n" +
                        "   </fault>\n" +
                        "</methodResponse>\n",
                        new XmlRpcFault(4, "Too many parameters.")}
        );
    }

    @ParameterizedTest
    @MethodSource
    void readTest(String xml, XmlRpcResponseBase result) {
        var reader = new XmlRpcResponseReader();
        try (var inputStream = new ByteArrayInputStream(xml.getBytes())) {
            assertThat(reader.read(inputStream)).isEqualTo(result);
        } catch (IOException e) {
            throw new RuntimeException("Error reading XML stream", e);
        }
    }
}