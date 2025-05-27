package ru.practicum.shareit.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class OffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

    private final DateTimeFormatter formatter;
    private final ZoneId zoneId;

    protected OffsetDateTimeDeserializer(
            @Value("${shareit.api.datetime.format}") String dateTimeFormat,
            @Value("${shareit.api.datetime.timezone}") String timezone
    ) {
        super(OffsetDateTime.class);
        this.formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        this.zoneId = ZoneId.of(timezone);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String date = jsonParser.getText();
        return LocalDateTime.parse(date, formatter).atZone(zoneId).toOffsetDateTime();
    }

}
