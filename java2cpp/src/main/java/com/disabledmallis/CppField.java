package com.disabledmallis;

public class CppField {
    public String typeClassPath;
    public String type;
    public String name;
    public String obfuscatedName;

    public CppField(String type, String typeClassPath, String name, String obfuscatedName){
        this.type = type;
        this.typeClassPath = typeClassPath;
        this.name = name;
        this.obfuscatedName = obfuscatedName;
    }
    public String toString(){
        if(typeClassPath == null || Utils.isPrimitive(typeClassPath))
            return type + " " + name;
        return "class "+type+"* "+name;
    }
    public String genGetSet(){
        String cppType = type;
        if(!Utils.isPrimitive(cppType)){
            cppType = "class "+cppType+"*";
        }
        return "\t"+cppType + " get_" + name + "() {\n" +
                "\t\t //TODO: Auto generated stub\n" +
                "\t}" + "\n" +
                "\tvoid set_" + name + "("+cppType+" obj) {\n" +
                "\t\t //TODO: Auto generated stub\n" +
                "\t}\n";
    }
}
