package com.disabledmallis;

public class CppField {
    public String unmappedType;
    public String unmappedName;
    public CppField(String unmappedType, String unmappedName){
        this.unmappedType = unmappedType;
        this.unmappedName = unmappedName;
    }
    public String genGet(){
        String totalType = unmappedType;
        if(!Utils.isPrimitive(totalType)){
            totalType = "class "+totalType+"*";
        }

        return ("\t#TOTALTYPE# #NAME#()\n" +
                "{\n" +
                "\tJNIEnv* env = Utils::getJNI();\n" +
                "\tjfieldID settingsField = env->GetFieldID(env->GetObjectClass(this), \"field_71474_y\", \"Lnet/minecraft/client/settings/GameSettings;\");\n" +
                "\treturn (GameSettings*)env->GetObjectField(this, settingsField);\n" +
                "}").replace("#TYPE#", totalType).replace("#NAME#", unmappedName);
    }
}
