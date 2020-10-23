package com.disabledmallis;

public class CppField {
    public String unmappedType;
    public String unmappedName;
    public String mappedName;
    public String mappedType;
    public CppField(String unmappedType, String unmappedName){
        this.unmappedType = unmappedType;
        this.mappedType = unmappedType;
        this.unmappedName = unmappedName;
        this.mappedName = unmappedName;
    }
    public String genGet(){
        String totalType = Utils.getClassNameFromPath(mappedType);
        if(!Utils.isPrimitive(totalType)){
            totalType = "class "+totalType+"*";
        }

        return ("\n\t#TOTALTYPE# #NAME#()\n" +
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
