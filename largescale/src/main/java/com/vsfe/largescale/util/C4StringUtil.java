package com.vsfe.largescale.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.helpers.MessageFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class C4StringUtil {

    /**
     * 형식과 객체가 주어졌을 때, 해당 형식에 맞춰 객체를 배치한다.
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        return MessageFormatter.arrayFormat(format, args).getMessage();
    }
}
