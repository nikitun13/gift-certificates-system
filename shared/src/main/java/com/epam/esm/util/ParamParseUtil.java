package com.epam.esm.util;

public class ParamParseUtil {

    private ParamParseUtil() {
    }

    /**
     * Removes last character of the string if it isn't a letter.
     *
     * @param str string to be edited.
     * @return edited string.
     */
    public static String removeOperationCharacterIfPresent(String str) {
        int lastCharIndex = str.length() - 1;
        int lastCharCodePoint = str.codePointAt(lastCharIndex);
        return Character.isLetter(lastCharCodePoint)
                ? str
                : str.substring(0, lastCharIndex);
    }
}
