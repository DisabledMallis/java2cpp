package com.disabledmallis;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    public static void loadJar(String jarPath) throws IOException {
        JarFile theJar = new JarFile(jarPath);
        Enumeration<JarEntry> e = theJar.entries();

        URL[] urls = { new URL("jar:file:" + jarPath +"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            try{
                Class c = cl.loadClass(className);
            }catch(NoClassDefFoundError | ClassNotFoundException ex){
                if(className.equals("ave")){
                    ex.printStackTrace();
                    Out.Out("Failed to load class "+className);
                }
            }
        }
    }
    public static String reverseString(String toReverse){
        StringBuilder sb = new StringBuilder(toReverse);
        return sb.reverse().toString();
    }
    public static CppField[] getFields(String sourceCode){
        ArrayList<CppField> list = new ArrayList<>();
        String[] sourceLines = sourceCode.split("\\r\\n|\\r|\\n");
        for(String sourceLine : sourceLines){
            if(sourceLine.endsWith(";") && (sourceLine.startsWith("  ") && !sourceLine.startsWith("   "))) {
                for(String accessModifier : javaAccessModifiers){
                    sourceLine = sourceLine.replace(accessModifier, "");
                }
                int firstCharPos = 9999;
                for(char alphabet = 'A'; alphabet <= 'z'; alphabet++){
                    int charAt = sourceLine.indexOf(alphabet);
                    //Out.Out("" + charAt + alphabet);
                    if(charAt > 0){
                        if(charAt < firstCharPos){
                            firstCharPos = charAt;
                        }
                    }
                }
                sourceLine = sourceLine.substring(firstCharPos);
                if(sourceLine.contains("=")){
                    sourceLine = sourceLine.substring(0, sourceLine.indexOf("="));
                }
                String[] split = sourceLine.split(" ");
                String type = split[0];
                String obf_name = split[1];
                if(isPrimitive(type))
                    list.add(new CppField(type, null, obf_name, obf_name));
                else
                    list.add(new CppField(type, type, obf_name, obf_name));
                Out.Out(sourceLine);
            }
        }
        return ((CppField[]) list.toArray());
    }
}
