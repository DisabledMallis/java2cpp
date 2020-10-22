package com.disabledmallis;

public class CppField {
    public String type;
    public String name;
    public CppField(String type, String name){
        this.type = type;
        this.name = name;
    }
    public String genGet(){
        String totalType = type;
        if(!Utils.isPrimitive(totalType)){
            totalType = "class "+totalType+"*";
        }

        return ("\t#TYPE# #NAME#(){\n" +
                "\t\t//TODO: Generate getter\n" +
                "\t}\n").replace("#TYPE#", totalType).replace("#NAME#", name);
    }
}
