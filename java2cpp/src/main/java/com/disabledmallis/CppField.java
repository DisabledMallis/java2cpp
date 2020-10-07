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
        return type + " get_" + name + "() {}" + "\n"
                + "void set_" + name + "("+type+" obj) {}\n";
    }
}
