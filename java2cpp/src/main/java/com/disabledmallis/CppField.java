package com.disabledmallis;

public class CppField {
    public String type;
    public String name;

    public CppField(String type, String name){
        this.type = type;
        this.name = name;
    }
    public String toString(){
        return type + " " + name;
    }
    public String genGetSet(){
        return type + " get_" + name + "() {}" + "\n"
                + "void set_" + name + "("+type+" obj) {}";
    }
}
