package com.disabledmallis;

public class Utils {
    public static boolean isPrimitive(String type){
        String[] primitives = {"byte","short","int","long","float","double","boolean","char"};
        for(String primitive : primitives){
            if(primitive.equalsIgnoreCase(type)){
                return true;
            }
        }
        return false;
    }
}
