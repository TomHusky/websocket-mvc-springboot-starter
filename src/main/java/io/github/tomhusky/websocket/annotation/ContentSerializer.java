package io.github.tomhusky.websocket.annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * <p>
 * 自定义反序列化
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2023/1/31 11:44
 */
public class ContentSerializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        return node.toString();
    }
}
