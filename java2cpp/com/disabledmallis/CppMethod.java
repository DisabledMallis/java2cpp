package com.disabledmallis;

import java.util.ArrayList;

public class CppMethod {
    public String name;
    public String returnType;
    public ArrayList<CppField> parameters;

    public CppMethod(String name, String returnType){
        this.name = name;
        this.returnType = returnType;
    }
    public String toString(){
        String fullParams = "";
        int count = 0;
        for(String param : parameters){
            count++;
            if(count >= parameters.size()){
                fullParams += param;
            }
            else{
                fullParams += param+", ";
            }
        }
        return "class #RET* #NAME(#PARAMS){\n"
        + " //TODO: Auto generated stub\n"
        + "};".replace("#RET", returnType)
        .replace("#NAME", name)
        .replace("#PARAMS", fullParams);
    }
}
