package com.disabledmallis;

import com.disabledmallis.Out;
import com.disabledmallis.CppClass;
import com.disabledmallis.CppField;
import com.disabledmallis.CppMethod;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static void genCppFileFor(CppClass a_class){
        File classPathFile = new File(cwd+"/gen/"+a_class.classPath);
        File parentDir = classPathFile.getParentFile();
        if(!parentDir.exists()){
            if(!parentDir.mkdirs()){
                Out.Out("Failed to create "+parentDir.getAbsolutePath());
                return;
            }
        }
        try {
            File header = new File(classPathFile+".h");
            if(header.exists()){
                header.delete();
            }
            header.createNewFile();
            Files.writeString(Paths.get(header.getPath()), a_class.genHeader(), StandardOpenOption.WRITE);
            //new File(classPathFile+".cpp").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CppClass> classes = new ArrayList<>();
    public static String cwd = System.getProperty("user.dir");

    public static void handleClassLine(String classMapping)
    {
        String unmappedName = classMapping.split(" ")[0];
        String mcpPath = classMapping.split(" ")[1];
        String mcpName = new File(mcpPath).getName();

        int isntClean = mcpName.indexOf("$");
        int isActuallyClean = mcpName.indexOf("$1");
        if(isActuallyClean == -1 || mcpName.length()-2 != isActuallyClean){
            if(isntClean != -1){
                for(CppClass cppClass : classes){
                    if(cppClass.className_unmapped.equals(unmappedName)){
                        classes.remove(cppClass);
                        break;
                    }
                }
                Out.Out(mcpName + " isnt clean, skipping...");
                return;
            }
        }
        mcpPath = mcpPath.replace("$1","");
        mcpName = mcpName.replace("$1","");

        for(CppClass cppClass : classes){
            if(cppClass.className_unmapped.equalsIgnoreCase(unmappedName)){
                cppClass.classPath = mcpPath;
                cppClass.className = mcpName;
                Out.Out("Replaced "+unmappedName+" as "+mcpName);
            }
        }



    }

    public static void handleFieldLine(String fieldLine)
    {
        String obfuscatedName = fieldLine.split(" ")[0];
        String unmappedFieldPath = fieldLine.split(" ")[1];
        File fieldAsFile = new File(unmappedFieldPath);
        String unmappedFieldName = fieldAsFile.getName();
        String className = fieldAsFile.getParentFile().getPath().replace("\\","/");

        for(CppClass cppClass : classes){
            if(cppClass.classPath.equalsIgnoreCase(className)){
                /*cppClass.addField(new CppField("%AWAITTYPE%", null, unmappedFieldName));
                Out.Out("Read field "+unmappedFieldName+" from obfuscation "+obfuscatedName+ " in class "+className);*/
            }
        }
    }

    public static void handleMethodLine(String methodLine) {
        String obfuscatedName = methodLine.split(" ")[0];
        String obfuscatedParams = methodLine.split(" ")[1];
        String unmappedMethodPath = methodLine.split(" ")[2];
        String deobfMethodParams = methodLine.split(" ")[3];

        String params = deobfMethodParams
                .substring(deobfMethodParams.indexOf("(")+1)
                .substring(0, deobfMethodParams.indexOf(")"));
        String returnType = deobfMethodParams
                .substring(deobfMethodParams.indexOf(")")+1);
        if(returnType.startsWith("L"))
            returnType = new File(returnType.substring(1, returnType.length()-1)).getName();

        File methodAsFile = new File(unmappedMethodPath);
        String unmappedFuncName = methodAsFile.getName();
        String className = methodAsFile.getParentFile().getPath().replace("\\","/");

        for(CppClass cppClass : classes){
            if(cppClass.classPath.equalsIgnoreCase(className)){
                CppMethod method = new CppMethod(unmappedFuncName, unmappedFuncName, returnType);

                boolean readingClass = false;
                ArrayList<String> paramTypes = new ArrayList<>();
                StringBuilder buildBuf = null;
                for(int i  = 0; i<params.length(); i++){
                    char paramType = params.charAt(i);
                    if(!readingClass){
                        if(paramType == 'L'){
                            readingClass=true;
                            buildBuf = new StringBuilder();
                            continue;
                        }
                        else{
                            String primStr = Utils.jvmToPrimitive((paramType+"").toUpperCase());
                            if(primStr != null){
                                paramTypes.add(primStr);
                            }
                        }
                    }
                    else{
                        if(paramType==';'){
                            readingClass=false;
                            String nextClassPath = buildBuf.toString();
                            paramTypes.add(nextClassPath);
                            continue;
                        }
                        else{
                            buildBuf.append(paramType);
                        }
                    }
                }


                int paramNum = 0;
                for(String param : paramTypes){
                    method.addParameter("param"+paramNum, param);
                    paramNum++;
                }


                Out.Out("Found func "+unmappedFuncName+" in class "+className);
                Out.Out("With parameter types:");
                for(String param : paramTypes){
                    Out.Out(param);
                }
                cppClass.addMethod(method);
            }
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner input = new Scanner(System.in);
        Out.Out("Java2Cpp - Made by DisabledMallis");
        Out.Out("Generate C++ jni code from java mappings");

        Out.Out("CWD: "+cwd);

        Out.Out("Absolute path to SRG: ");
        String pathToSrg = "A:\\Downloads\\mcp-1.8.9-srg\\joined.srg";//input.nextLine();
        String pathToFieldsCSV = "A:\\Downloads\\mcp_stable-22-1.8.9\\fields.csv";//input.nextLine();
        String pathToMethodsCSV = "A:\\Downloads\\mcp_stable-22-1.8.9\\methods.csv";//input.nextLine();
        String pathToParamsCSV = "A:\\Downloads\\mcp_stable-22-1.8.9\\params.csv";//input.nextLine();

        String mcJarPath = System.getenv("APPDATA")+"/.minecraft/versions/1.8.9/1.8.9.jar";
        JarFile mcJar = new JarFile(mcJarPath);
        Enumeration<JarEntry> e = mcJar.entries();

        URL[] urls = { new URL("jar:file:" + mcJarPath +"!/") };
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
                CppClass newClass = new CppClass(c.getName(), c.getName().replace('.','/'), c.getName());
                Field[] classFields = c.getFields();
                for(Field field : classFields){
                    newClass.addField(new CppField(field.getType().getTypeName(), c.getName(), field.getName()));
                }
                Method[] classMethods = c.getMethods();
                for(Method method : classMethods){
                    newClass.addMethod(new CppMethod("%AWAIT%", method.getName(), method.getReturnType().getTypeName()));
                }
                classes.add(newClass);
                Out.Out("Loaded class "+className);
            }catch(ClassNotFoundException ex){
                ex.printStackTrace();
                Out.Out("Failed to load class "+className);
            }catch (NoClassDefFoundError ex){
                ex.printStackTrace();
                Out.Out("Failed to load class "+className);
            }
        }

        String srgContent = "";
        for(String line : Files.readAllLines(Paths.get(pathToSrg))) {
            if(line.startsWith("CL: ")){
                handleClassLine(line.substring(4));
            }
            if(line.startsWith("FD: ")){
                handleFieldLine(line.substring(4));
            }
            if(line.startsWith("MD: ")){
                handleMethodLine(line.substring(4));
            }
            srgContent += line+System.lineSeparator();
        }

        for(String fieldName : Files.readAllLines(Paths.get(pathToFieldsCSV))){
            String unmappedName = fieldName.split(",")[0];
            String mappedName = fieldName.split(",")[1];
            for(CppClass cppClass : classes){
                for(CppField cppField : cppClass.fields){
                    if(cppField.name.equals(unmappedName)){
                        cppField.name = mappedName;
                    }
                }
            }
        }

        File outDir = new File(cwd+"/gen");
        if(!outDir.exists()){
            if(!outDir.mkdirs()){
                Out.Out("Failed to create "+outDir.getAbsolutePath());
                return;
            }
        }

        for(CppClass cpp_class : classes){
            genCppFileFor(cpp_class);
        }

        Out.Out("Finished");
        //Out.Out(srgContent);
    }
}
