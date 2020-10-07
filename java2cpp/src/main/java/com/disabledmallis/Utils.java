package com.disabledmallis;

public class Utils {
    static String[] primitives = {"byte","char","short","int","long","float","double","boolean","void"};
    static String[] jvmPrims = {"B","C","S","I","J","F","D","Z","V"};
    static String[] cppPrims = {"char","char","short","int","long long","float","double","bool","void"};
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
    public static String primitiveToCpp(String primitive){
        int i = 0;
        for(String jvmPrim : primitives){
            if(jvmPrim.equalsIgnoreCase(primitive)){
                return cppPrims[i];
            }
            i++;
        }
        return primitive;
    }
}
