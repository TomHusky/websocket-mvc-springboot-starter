package io.github.tomhusky.websocket.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * jackson工具类
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/11/15 15:32
 */
public class JacksonUtil {
    /**
     * 实例化ObjectMapper对象
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        OBJECT_MAPPER.setSerializerFactory(OBJECT_MAPPER.getSerializerFactory()
                .withSerializerModifier(new MyBeanSerializerModifier()));
        SerializerProvider serializerProvider = OBJECT_MAPPER.getSerializerProvider();
        serializerProvider.setNullValueSerializer(new CustomizeNullJsonSerializer
                .NullObjectJsonSerializer());
        OBJECT_MAPPER
                .registerModules(timeModule, javaTimeModule)
                // 设置允许序列化空的实体类（否则会抛出异常）
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // 设置 把java.util.Date, Calendar输出为数字（时间戳）
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 设置在遇到未知属性的时候不抛出异常
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                // 设置数字丢失精度问题
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                // 设置接受只有一个元素的数组的反序列化
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    /**
     * 私有化构造器
     */
    private JacksonUtil() {
    }

    /**
     * 对象转json字符串
     *
     * @param obj obj
     * @return java.lang.String
     */
    public static String toJSONString(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 对象转json字符串
     *
     * @param obj obj
     * @return java.lang.String
     */
    public static String toJSONPrettyString(Object obj) {
        try {
            SerializationConfig config = OBJECT_MAPPER.getSerializationConfig();
            PrettyPrinter prettyPrinter = config.getDefaultPrettyPrinter();
            DefaultPrettyPrinter def = (DefaultPrettyPrinter) prettyPrinter;
            DefaultPrettyPrinter.Indenter indented = new DefaultIndenter("   ", DefaultIndenter.SYS_LF);
            def.indentArraysWith(indented);
            def.indentObjectsWith(indented);
            ObjectWriter objectWriter = OBJECT_MAPPER.writer(def);
            return objectWriter.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * json字符串转换bean
     *
     * @param jsonStr json字符串
     * @param clazz   类
     * @return T
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json字符串转Map
     *
     * @param jsonStr jsonStr
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @SuppressWarnings(value = "unchecked")
    public static Map<String, Object> parseObject(String jsonStr) {
        return OBJECT_MAPPER.convertValue(jsonStr, Map.class);
    }

    /**
     * json字符串转Map
     *
     * @param jsonStr jsonStr
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @SuppressWarnings(value = "unchecked")
    public static Map<String, Object> parseObject(Object jsonStr) {
        return OBJECT_MAPPER.convertValue(jsonStr, Map.class);
    }

    /**
     * 删除key
     *
     * @param jsonStr   json 字符
     * @param removeKey key
     * @return java.lang.String
     */
    public static String removeKey(Object jsonStr, String... removeKey) {
        Map<String, Object> map = parseObject(jsonStr);
        for (String key : removeKey) {
            map.remove(key);
        }
        return toJSONString(map);
    }


    /**
     * json字符串转对象集合
     *
     * @param jsonStr jsonStr
     * @param clazz   clazz
     * @return java.util.List<T>
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return OBJECT_MAPPER.readValue(jsonStr, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 时间序列化时变为时间戳
     */
    private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * 时间戳反序列化时间
     */
    private static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            long timestamp = jsonParser.getLongValue();
            return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
        }
    }

    /**
     * 自定义null值序列化处理器
     */
    public static class CustomizeNullJsonSerializer {

        /**
         * 处理数组集合类型的null值
         */
        public static class NullArrayJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }
        }

        /**
         * 处理字符串类型的null值
         */
        public static class NullStringJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString("");
            }
        }

        /**
         * 处理数值类型的null值
         */
        public static class NullNumberJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(0);
            }
        }

        /**
         * 处理boolean类型的null值
         */
        public static class NullBooleanJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeBoolean(false);
            }
        }

        /**
         * 处理实体对象类型的null值
         */
        public static class NullObjectJsonSerializer extends JsonSerializer<Object> {
            @Override
            public void serialize(Object value, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        }

    }

    /**
     * <pre>
     * 此modifier主要做的事情为：
     * 1.当序列化类型为数组集合时，当值为null时，序列化成[]
     * 2.String类型值序列化为""
     *
     * </pre>
     */
    public static class MyBeanSerializerModifier extends BeanSerializerModifier {

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                         BeanDescription beanDesc,
                                                         List<BeanPropertyWriter> beanProperties) {
            // 循环所有的beanPropertyWriter
            for (BeanPropertyWriter writer : beanProperties) {
                // 判断字段的类型，如果是数组或集合则注册nullSerializer
                if (isArrayType(writer)) {
                    // 给writer注册一个自己的nullSerializer
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullArrayJsonSerializer());
                }
                if (isStringType(writer)) {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullStringJsonSerializer());
                }
                if (isNumberType(writer)) {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullNumberJsonSerializer());
                }
                if (isBooleanType(writer)) {
                    writer.assignNullSerializer(new CustomizeNullJsonSerializer.NullBooleanJsonSerializer());
                }
            }
            return beanProperties;
        }

        /**
         * 是否是数组
         */
        private boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是String
         */
        private boolean isStringType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是数值类型
         */
        private boolean isNumberType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return Number.class.isAssignableFrom(clazz);
        }

        /**
         * 是否是boolean
         */
        private boolean isBooleanType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Boolean.class);
        }
    }
}
