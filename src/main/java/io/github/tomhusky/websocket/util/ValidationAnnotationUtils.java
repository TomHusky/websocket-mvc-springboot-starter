package io.github.tomhusky.websocket.util;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;

/**
 * <p>
 * 参数校验注解工具类  copy from org.springframework.validation.annotation.ValidationAnnotationUtils
 * <p/>
 *
 * @author lwj
 * @version 1.0
 * @since 2021/11/3 15:54
 */
public abstract class ValidationAnnotationUtils {
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    @Nullable
    public static Object[] determineValidationHints(Annotation ann) {
        Class<? extends Annotation> annotationType = ann.annotationType();
        String annotationName = annotationType.getName();
        if ("javax.validation.Valid".equals(annotationName)) {
            return EMPTY_OBJECT_ARRAY;
        } else {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null) {
                Object hints = validatedAnn.value();
                return convertValidationHints(hints);
            } else if (annotationType.getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(ann);
                return convertValidationHints(hints);
            } else {
                return null;
            }
        }
    }

    private static Object[] convertValidationHints(@Nullable Object hints) {
        if (hints == null) {
            return EMPTY_OBJECT_ARRAY;
        } else {
            return hints instanceof Object[] ? (Object[])((Object[])hints) : new Object[]{hints};
        }
    }
}
