package com.disabledmallis;

import java.util.ArrayList;

public class CppClass {
    public ArrayList<CppField> fields = new ArrayList<>();
    public ArrayList<CppMethod> methods = new ArrayList<>();
    public String unmappedClassName;
    public String mappedClassName;
    public CppClass(String unmappedClassName){
        this.unmappedClassName = unmappedClassName;
        this.mappedClassName = unmappedClassName;
    }

    public void setMappedClassName(String mappedClassName){
        this.mappedClassName = mappedClassName;
    }

    public String genClass(){
        StringBuilder gennedFields = new StringBuilder();
        for(CppField field : fields){
            gennedFields.append(field.genGet());
        }
        StringBuilder gennedMethods = new StringBuilder();
        for(CppMethod method : methods){
            gennedFields.append(method.genMethod());
        }
        return ("#pragma once\n" +
                "#include <jni.h>\n" +
                "struct %CLASSNAME% : public _jobject\n" +
                "{\n" +
                "%FIELDS%\n" +
                "%METHODS%\n" +
                "}\n")
                .replace("%CLASSNAME%", "ave")//mappedClassName)
                .replace("%FIELDS%", gennedFields.toString())
                .replace("%METHODS%", gennedMethods.toString());
    }
}
