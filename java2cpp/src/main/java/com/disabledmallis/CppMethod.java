package com.disabledmallis;

import java.io.File;
import java.util.ArrayList;

import com.disabledmallis.Utils;

public class CppMethod {
    public String name;
    public String name_unmapped;
    public String returnType;
    public ArrayList<CppField> parameters = new ArrayList<>();

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
        String safeRetType = Utils.jvmToPrimitive(returnType);
        if(safeRetType == null){
            safeRetType = returnType;
        }
        else{
            safeRetType = Utils.primitiveToCpp(safeRetType);
        }
        if(!Utils.isPrimitive(Utils.jvmToPrimitive(returnType))){
            safeRetType = "class "+returnType+"*";
        }
        return ("#RET #NAME(#PARAMS){\n"
        + " //TODO: Auto generated stub\n"
        + "}\n").replace("#RET", safeRetType)
        .replace("#NAME", name)
        .replace("#PARAMS", fullParams);
    }

    public void addParameter(String name, String type) {
        parameters.add(new CppField(new File(type).getName(), type, name));
    }
}
