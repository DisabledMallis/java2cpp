package com.disabledmallis;

import java.util.ArrayList;

public class CppMethod extends Mappable {
    class Parameter {
        public String paramType;
        public String paramName;

        public boolean isPrimitive(){
            return Utils.isPrimitive(paramType);
        }
        public String genParam(boolean isLast){
            return "";
        }
    }
    public String mappedMethodNode;
    public String mappedMethodType;
    public String unmappedMethodType;
    public String obfuscatedMethodType;

    public ArrayList<Parameter> params = new ArrayList<>();
    public CppMethod(String obfuscatedMethodType, String obfuscatedMethodName) {
        super(obfuscatedMethodName);
        this.obfuscatedMethodType = obfuscatedMethodType;
    }

    public void setMappedName(String mappedMethodName) {
        this.mappedName = mappedName;
    }
    public void setMappedType(String mappedMethodType) {
        this.mappedMethodType = mappedMethodType;
    }
    public void setMethodNode(String mappedMethodNode){
        this.mappedMethodNode = mappedMethodNode;
    }

    public String genMethod() {
        String totalType = mappedMethodType;
        if(!Utils.isPrimitive(totalType)){
            totalType = "class "+totalType+"*";
        }
        String theCalltype = "Object";
        if(Utils.isPrimitive(mappedMethodType)){
            theCalltype=mappedMethodType.toUpperCase();
        }
        return ("\t#TYPE# #MAPPEDNAME#(#PARAMS#) {\n" +
                "\t\tJNIEnv* env = Utils::getJNI();\n" +
                "\t\tjmethodID method = env->GetMethodID(env->GetObjectClass(this), \"#UNMAPPEDNAME#\", \"#METHODNODE#\");\n" +
                "\t\t#RETURN#env->Call#CALLTYPE#Method(this, method);\n" +
                "\t}\n")
                .replace("#RETURN#", Utils.isPrimitive(unmappedMethodType) ? "return " : "")
                .replace("#UNMAPPEDNAME#", unmappedName)
                .replace("#MAPPEDNAME#", mappedName)
                .replace("#METHODNODE#", mappedMethodNode)
                .replace("#TYPE#", totalType)
                .replace("#PARAMS#", "")//cStyleParams)
                .replace("#CALLTYPE#", theCalltype);
    }
}
