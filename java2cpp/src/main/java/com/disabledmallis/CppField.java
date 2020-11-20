package com.disabledmallis;

public class CppField extends Mappable{
    public String obfuscatedType;
    public String unmappedType;
    public String mappedType;
    public CppField(String obfuscatedType, String obfuscatedName){
        super(obfuscatedName);
        this.obfuscatedType = obfuscatedType;
        this.unmappedType = obfuscatedType;
        this.mappedType = obfuscatedType;
    }

    public void setObfuscatedType(String obfuscatedType) {
        this.obfuscatedType = obfuscatedType;
    }
    public void setUnmappedType(String unmappedType) {
        this.unmappedType = unmappedType;
    }
    public void setMappedType(String mappedType) {
        this.mappedType = mappedType;
    }

    public String genGet(){
        String totalType = Utils.getChildFromPath(mappedType);
        if(!Utils.isPrimitive(totalType)){
            totalType = "class "+totalType+"*";
        }

        return ("\t#TOTALTYPE# get_#NAME#()\n" +
                "\t{\n" +
                "\t\tJNIEnv* env = Utils::getJNI();\n" +
                "\t\tjfieldID field = env->GetFieldID(env->GetObjectClass(this), \"#UM_FIELDNAME#\", \"#TYPE#\");\n" +
                "\t\treturn (#TYPENAME#*)env->GetObjectField(this, field);\n" +
                "\t}\n")
                .replace("#TOTALTYPE#", totalType)
                .replace("#UM_FIELDNAME#", unmappedName)
                .replace("#NAME#", mappedName)
                .replace("#TYPE#", mappedType);
    }
}
