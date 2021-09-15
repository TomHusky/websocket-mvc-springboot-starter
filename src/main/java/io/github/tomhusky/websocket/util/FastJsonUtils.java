package io.github.tomhusky.websocket.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collections;
import java.util.List;


/**
 * <p> JSON处理类 <p/>
 *
 * @author lwj
 * @since 2021/8/27 15:01
 */
public class FastJsonUtils {

    private FastJsonUtils() {

    }

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        // 使用和json-lib兼容的日期输出格式
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,
            // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty,
            // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero,
            // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,
            // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty,
            // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * 转换成数值非空的字段
     *
     * @param object 对象
     * @return java.lang.String
     */
    public static String convertObjectToJson(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * 对象转JSON格式字符串
     *
     * @param obj 对象
     * @return java.lang.String
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return JSON.toJSONString(obj, config);
        }
    }

    /**
     * 获取JSON格式字符串的byte
     *
     * @param obj 对象
     * @return byte[]
     */
    public static byte[] toJsonByte(Object obj) {
        if (obj == null) {
            return new byte[]{};
        } else {
            return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue).getBytes();
        }
    }

    /**
     * 获取JSON格式字符串的byte, null的字段 不显示
     *
     * @param obj 对象
     * @return byte[]
     */
    public static byte[] toJsonNotNullByte(Object obj) {
        if (obj == null) {
            return new byte[0];
        } else {
            return JSON.toJSONString(obj).getBytes();
        }
    }

    /**
     * JSON格式字符串转换成目标对象
     *
     * @param jsonStr  JSON格式字符串
     * @param cls 需要转换的对象class
     * @return T
     */
    public static <T> T toObject(String jsonStr, Class<T> cls) {
        return JSON.parseObject(jsonStr, cls);
    }

    /**
     * 把json字符串转换成指定类型的对象
     *
     * @param text json字符串
     * @param type  类型
     * @return T
     */
    public static <T> T toBean(String text, TypeReference<T> type) {
        return JSON.parseObject(text, type);
    }

    /**
     * JSON格式字符串转换成目标集合对象
     *
     * @param jsonStr JSON格式字符串
     * @param cls  目标对象类型
     * @return java.util.List<T>
     */
    public static <T> List<T> toList(String jsonStr, Class<T> cls) {
        if (StrUtil.isBlank(jsonStr)) {
            return Collections.emptyList();
        }
        return JSON.parseArray(jsonStr, cls);
    }
}
