package com.gaurav.domain;

import java.util.function.Function;

public class Utils {

    public static <T, R> R switchConstruct(Object o, Function<T, R>... functions) {
        R r = null;
        for (Function<T, R> function : functions) {
            r = function.apply((T) o);
            if (r != null) {
                break;
            }
        }
        return r;
    }

    public static <T, R> Function<T, R> caseConstruct(Class<T> cls, Function<T, R> function) {
        return t -> cls.isInstance(t) ? function.apply(t) : null;
    }
}
