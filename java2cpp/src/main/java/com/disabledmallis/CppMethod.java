package com.disabledmallis;

import java.util.ArrayList;

import com.disabledmallis.Utils;

public class CppMethod {
    public String name;
    public String name_unmapped;
    public String returnType;
    public ArrayList<CppField> parameters;

    public CppMethod(String name, String name_unmapped, String returnType){
        this.name = name;
        this.returnType = returnType;
        this.name_unmapped = name_unmapped;
    }
    public String toString(){
        String fullParams = "";
        int count = 0;
        for(CppField param : parameters){
            count++;
            if(count >= parameters.size()){
                fullParams += param;
            }
            else{
                fullParams += param+", ";
            }
        }
        String safeRetType = returnType;
        if(!Utils.isPrimitive(returnType)){
            safeRetType = "class "+returnType+"*";
        }
        return ("#RET #NAME(#PARAMS){\n"
        + " //TODO: Auto generated stub\n"
        + "};").replace("#RET", safeRetType)
        .replace("#NAME", name)
        .replace("#PARAMS", fullParams);
    }
}
