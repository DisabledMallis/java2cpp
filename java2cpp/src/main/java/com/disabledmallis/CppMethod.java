package com.disabledmallis;

import java.util.ArrayList;

public class CppMethod {
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
    public String mappedMethodType;
    public String unmappedMethodType;
    public String mappedMethodName;
    public String unmappedMethodName;
    public String mappedMethodNode;
    public ArrayList<Parameter> params = new ArrayList<>();
    public CppMethod(String unmappedMethodType, String unmappedMethodName) {
        this.unmappedMethodType = unmappedMethodType;
        this.unmappedMethodName = unmappedMethodName;
    }

    public void setMappedName(String mappedMethodName) {
        this.mappedMethodName = mappedMethodName;
    }
    public void setMappedType(String mappedMethodType) {
        this.mappedMethodType = mappedMethodType;
    }
    public void setMethodNode(String mappedMethodNode){
        this.mappedMethodNode = mappedMethodNode;
    }

    public String genMethod() {
        String totalType = unmappedMethodType;
        if(!Utils.isPrimitive(totalType)){
            totalType = "class "+totalType+"*";
        }
        String theCalltype = "Object";
        if(Utils.isPrimitive(mappedMethodType)){
            theCalltype=mappedMethodType.toUpperCase();
        }
        return ("\t#TYPE# #MAPPEDNAME#(#PARAMS#) {" +
                "\t\tJNIEnv* env = Utils::getJNI();\n" +
                "\t\tjmethodID method = env->GetMethodID(env->GetObjectClass(this), \"#UNMAPPEDNAME#\", \"#METHODNODE#\");\n" +
                "\t\t#RETURN#env->Call#CALLTYPE#Method(this, method);" +
                "\t}")
                .replace("#RETURN#", Utils.isPrimitive(unmappedMethodType) ? "return " : "")
                .replace("#UNMAPPEDNAME#", unmappedMethodName)
                //.replace("#MAPPEDNAME#", mappedMethodName)
                //.replace("#METHODNODE#", mappedMethodNode)
                .replace("#TYPE#", totalType)
                .replace("#PARAMS", "")//cStyleParams)
                .replace("#CALLTYPE#", theCalltype);
    }
}
