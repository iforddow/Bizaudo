package com.iforddow.bizaudo.util;

/**
* A utility class to provide helpful methods
* throughout the application.
*
* @author IFD
* @since 2025-07-17
* */
public class BizUtils {

    /**
     * A method to check if a string is not null and not empty.
     *
     * @param str the string to check
     *
     * @return true if the string is not null and not empty, false otherwise
     *
     * @author IFD
     * @since 2025-06-14
     * */
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * A method to check if a string is null or empty.
     *
     * @param str the string to check
     *
     * @return true if the string is null or empty, false otherwise
     *
     * @author IFD
     * @since 2025-06-14
     * */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
    * A method to capitalize the first letter
    * of a String.
    *
    * @author IFD
    * @since 2025-07-18
    * */
    public static String capitalizeFirstLetter(String str) {

        if(isNullOrEmpty(str)) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
