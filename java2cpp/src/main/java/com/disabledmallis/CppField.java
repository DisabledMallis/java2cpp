package com.disabledmallis;

public class CppField {
    public String typeClassPath;
    public String type;
    public String name;

    public CppField(String type, String typeClassPath, String name){
        this.type = type;
        this.typeClassPath = typeClassPath;
        this.name = name;
    }
    public String toString(){
        if(typeClassPath == null || Utils.isPrimitive(typeClassPath))
            return type + " " + name;
        return "class "+type+"* "+name;
    }
    public String genGetSet(){
        return "\t"+type + " get_" + name + "() {\n" +
                "\t\t //TODO: Auto generated stub\n" +
                "\t}" + "\n" +
                "\tvoid set_" + name + "("+type+" obj) {\n" +
                "\t\t //TODO: Auto generated stub\n" +
                "\t}\n";
    }
}
