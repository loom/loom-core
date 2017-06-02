package io.loom.core.messaging;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

import java.io.IOException;

public class JacksonMessageSerializer implements MessageSerializer {
    private class InternalTypeResolverBuilder extends ObjectMapper.DefaultTypeResolverBuilder {

        InternalTypeResolverBuilder() {
            super(ObjectMapper.DefaultTyping.NON_FINAL);
        }

        @Override
        public boolean useForType(JavaType t) {
            return !t.isPrimitive();
        }
    }

    private final ObjectMapper mapper;

    {
        TypeResolverBuilder<?> typer = new InternalTypeResolverBuilder();
        typer = typer.init(JsonTypeInfo.Id.CLASS, null);
        typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
        typer = typer.typeProperty("$type");
        mapper = new ObjectMapper().setDefaultTyping(typer);
    }

    @Override
    public String serialize(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("The parameter 'message' cannot be null.");
        }

        Class<?> type = message.getClass();
        if (type.equals(Boolean.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Boolean.");
        } else if (type.equals(Byte.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Byte.");
        } else if (type.equals(Character.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Character.");
        } else if (type.equals(Float.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Float.");
        } else if (type.equals(Integer.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Integer.");
        } else if (type.equals(Long.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Long.");
        } else if (type.equals(Short.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Short.");
        } else if (type.equals(Double.class)) {
            throw new IllegalArgumentException("The parameter 'message' cannot be of java.lang.Double.");
        }

        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            String errorMessage = "Could not serialize message. See the cause for details.";
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public Object deserialize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("The parameter 'value' cannot be null.");
        }

        try {
            return mapper.readValue(value, Object.class);
        } catch (IOException e) {
            String errorMessage = "Could not deserialize message. See the cause for details.";
            throw new RuntimeException(errorMessage, e);
        }
    }
}
