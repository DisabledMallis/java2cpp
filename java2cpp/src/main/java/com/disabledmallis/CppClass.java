package com.disabledmallis;

import java.util.ArrayList;

public class CppClass extends Mappable {
    public ArrayList<CppField> fields = new ArrayList<>();
    public ArrayList<CppMethod> methods = new ArrayList<>();
    public CppClass(String obfuscatedName){
        super(obfuscatedName);
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
                .replace("%CLASSNAME%", mappedName)
                .replace("%FIELDS%", gennedFields.toString())
                .replace("%METHODS%", gennedMethods.toString());
    }
}
