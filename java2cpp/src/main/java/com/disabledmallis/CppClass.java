package com.disabledmallis;

import java.util.ArrayList;

public class CppClass {
    public ArrayList<CppField> fields = new ArrayList<>();
    public ArrayList<CppMethod> methods = new ArrayList<>();
    public String className;
    public CppClass(String className){
        this.className = className;
    }

    public String genClass(){
        StringBuilder gennedFields = new StringBuilder();
        for(CppField field : fields){
            gennedFields.append(field.genGet());
        }
        return ("#pragma once\n" +
                "#include <jni.h>\n" +
                "struct %CLASSNAME% : public _jobject\n" +
                "{\n" +
                "%FIELDS%\n" +
                "%METHODS%\n" +
                "}\n").replace("%CLASSNAME%", className).replace("%FIELDS%", gennedFields.toString());
    }
}
