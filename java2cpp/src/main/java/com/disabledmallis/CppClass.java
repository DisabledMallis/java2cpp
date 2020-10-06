package com.disabledmallis;

import java.util.ArrayList;

public class CppClass {
    public String className;
    public String classPath;
    public String className_unmapped;
    public ArrayList<CppMethod> methods;
    public ArrayList<CppField> fields;

    public CppClass(String className, String classPath, String className_unmapped){
        this.className = className;
        this.classPath = classPath;
        this.className_unmapped = className_unmapped;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
    }
    public void addMethod(CppMethod methodToAdd){
        methods.add(methodToAdd);
    }
    public void addField(CppField fieldToAdd){
        fields.add(fieldToAdd);
    }
    public String genHeader(){
        StringBuilder fields_str = new StringBuilder();
        for(CppField field : fields){
            fields_str.append(field.genGetSet()).append("\n");
        }
        StringBuilder methods_str = new StringBuilder();
        for(CppMethod method : methods){
            methods_str.append(method.toString());
        }
        return ("//Class generated with java2cpp by DisabledMallis: https://github.com/DisabledMallis/java2cpp\n"
        +"#include <jni.h>\n"
        +"class #MAPPEDNAME : public _jobject\n"
        +"{\n"
        +"public:\n"
        + fields_str
        + methods_str
        +"}").replace("#MAPPEDNAME", className);
    }
    public String genCpp(){
        return "";
    }
}
