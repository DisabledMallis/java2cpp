package com.disabledmallis;

public class Utils {
    static String[] primitives = {"byte","char","short","int","long","float","double","boolean"};
    static String[] jvmPrims = {"B","C","S","I","J","F","D","Z"};
    public static boolean isPrimitive(String type){
        for(String primitive : primitives){
            if(primitive.equalsIgnoreCase(type)){
                return true;
            }
        }
        return false;
    }
    public static String jvmToPrimitive(String jvmType){
        int i = 0;
        for(String jvmPrim : jvmPrims){
            if(jvmPrim.equalsIgnoreCase(jvmType)){
                return primitives[i];
            }
            i++;
        }
        return null;
    }
}
