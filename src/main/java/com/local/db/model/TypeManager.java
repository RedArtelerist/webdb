package com.local.db.model;

import java.util.regex.Pattern;

public class TypeManager {
    public static Object parseObjectByType(String object, Type dataType) throws NumberFormatException {
        String rgbRegex = "^rgb\\((0|255|25[0-4]|2[0-4]\\d|1\\d\\d|0?\\d?\\d),(0|255|25[0-4]|2[0-4]\\d|1\\d\\d|0?\\d?\\d),(0|255|25[0-4]|2[0-4]\\d|1\\d\\d|0?\\d?\\d)\\)$";
        switch (dataType) {
            case INTEGER:
                return Integer.valueOf(object);
            case CHAR:
                if(Pattern.matches(".?", object))
                    return object.charAt(0);
                else
                    throw new NumberFormatException("error");
            case REAL:
                return Double.valueOf(object);
            case STRING:
                return object;
            case COLOR:
                if(Pattern.matches(rgbRegex, object))
                    return object;
                else
                    throw new NumberFormatException("error");
            case COLORInvl:
                String[] parts = object.split("-");
                if(parts.length == 2){
                    if(Pattern.matches(rgbRegex, parts[0]) && Pattern.matches(rgbRegex, parts[1]))
                        return object;
                }
                throw new NumberFormatException("error");

            default:
                throw new NumberFormatException("Unknown data format");
        }
    }
}

