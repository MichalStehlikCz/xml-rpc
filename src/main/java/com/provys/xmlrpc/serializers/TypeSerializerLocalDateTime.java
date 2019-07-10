package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcValue;

import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;

import static java.time.temporal.ChronoField.*;

public class TypeSerializerLocalDateTime extends TypeSerializerSimple<LocalDateTime> {

    private static final DateTimeFormatter XMLRPC_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 4, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

    @Override
    public Class<LocalDateTime> getBaseClass() {
        return LocalDateTime.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.DATE;
    }

    @Override
    protected String convertValue(LocalDateTime object) {
        return XMLRPC_DATE_FORMATTER.format(object);
    }
}
