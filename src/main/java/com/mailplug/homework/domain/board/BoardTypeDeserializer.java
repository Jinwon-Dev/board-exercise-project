package com.mailplug.homework.domain.board;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

public class BoardTypeDeserializer extends JsonDeserializer<BoardType> {

    @Override
    public BoardType deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {

        final String value = ((TextNode) parser.getCodec().readTree(parser)).asText();
        return BoardType.fromString(value);
    }
}
