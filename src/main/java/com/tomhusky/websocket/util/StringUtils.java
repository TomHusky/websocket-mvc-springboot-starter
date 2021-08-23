package com.tomhusky.websocket.util;

/**
 * @Author: lwj
 * @Package: com.lwj.jwt
 * @ClassName: StringUtils
 * @CreateDate: 2019/7/24 12:58
 * @Version: 1.0
 * @Description: string工具类
 */
public class StringUtils {

    private StringUtils() {

    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str);
    }

    /**
     * 截断字符串两侧的逗号
     *
     * @param str 字符串
     * @return 字符串
     */
    public static String trimComma(String str) {
        if (str.startsWith(",")) {
            str = str.substring(1);
        }
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 补全两位数字
     *
     * @param str
     * @return
     */
    public static String fulfuill(String str) {
        if (str.length() == 2) {
            return str;
        } else {
            return "0" + str;
        }
    }

    /**
     * 从拼接的字符串中提取字段
     *
     * @param str       字符串
     * @param delimiter 分隔符
     * @param field     字段
     * @return 字段值
     */
    public static String getFieldFromConcatString(String str, String delimiter, String field) {
        String[] fields = str.split(delimiter);
        for (String concatField : fields) {
            String fieldName = concatField.split("-")[0];
            String fieldValue = concatField.split("-")[1];
            if (fieldName.equals(field)) {
                return fieldValue;
            }
        }
        return null;
    }

    /**
     * 从拼接的字符串中给字段设置值
     *
     * @param str           字符串
     * @param delimiter     分隔符
     * @param field         字段名
     * @param newFieldValue 新的field值
     * @return 字段值
     */
    public static String setFieldInConcatString(String str, String delimiter, String field, String newFieldValue) {
        String[] fields = str.split(delimiter);

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].split("=")[0];
            if (fieldName.equals(field)) {
                String concatField = fieldName + "=" + newFieldValue;
                fields[i] = concatField;
                break;
            }
        }

        StringBuilder buffer = new StringBuilder("");
        for (int i = 0; i < fields.length; i++) {
            buffer.append(fields[i]);
            if (i < fields.length - 1) {
                buffer.append("|");
            }
        }

        return buffer.toString();
    }

    /**
     * description: 判断字符串是不是数值型
     *
     * @param s
     * @return boolean
     */
    public static boolean isNumber(String s) {

        String regex = "^[1-9][0-9]*\\.[0-9]+$|^[1-9][0-9]*$|^0+\\.[0-9]+$";
        char c = s.charAt(0);
        boolean bool;
        if (c == '+' || c == '-') {
            bool = s.substring(1).matches(regex);
        } else {
            bool = s.matches(regex);
        }
        return bool;
    }
}