package com.disabledmallis;

import java.io.File;

public class Utils {
    static String[] primitives = {"byte","char","short","int","long","float","double","boolean","void"};
    static String[] jvmPrims = {"B","C","S","I","J","F","D","Z","V"};
    static String[] cppPrims = {"char","char","short","int","long long","float","double","bool","void"};
    static String[] javaAccessModifiers = {"public", "private", "protected", "final"};
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

    public static String deobfuscate(String mapped, String[] info, String type) {
        String result = "";

        Task:
            for (String line : info) {
                if (line.contains(mapped)) {
                    String parts = line.split(type)[1].trim();
                    result = parts.split(mapped)[1];
                    break Task;
                }
            }

        return result;
    }

    public static String dotToSlash(String fuck) {
        return fuck.replace(".", "/");
    }

    public static String getChildFromPath(String path){
        return path.split("/")[path.split("/").length-1];
    }
    public static String removeChildFromPath(String path){
        int index = path.lastIndexOf('/');
        return path.substring(0, index);
    }
}
