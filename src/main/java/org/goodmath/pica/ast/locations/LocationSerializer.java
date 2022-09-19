package org.goodmath.pica.ast.locations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class LocationSerializer extends StdSerializer<Location> {
    public LocationSerializer() {
        this(null);
    }

    public LocationSerializer(Class<Location> t) {
        super(t);
    }

    @Override
    public void serialize(
            Location value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        if (value instanceof SourceFileLocation) {
            var sfl = (SourceFileLocation)value;
            jgen.writeStartObject();
            jgen.writeStringField("file", sfl.getFilename());
            jgen.writeNumberField("line", sfl.getLine());
            jgen.writeNumberField("column", sfl.getCol());
            jgen.writeEndObject();
        } else if (value instanceof SystemLocation) {
            jgen.writeStartObject();
            jgen.writeStringField("description", ((SystemLocation) value).getDescription());
            jgen.writeEndObject();
        } else {
            jgen.writeStartObject();
            jgen.writeStringField("location", "unknown");
            jgen.writeEndObject();
        }
    }
}

