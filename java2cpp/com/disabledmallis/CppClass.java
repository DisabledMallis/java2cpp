package com.disabledmallis;

import java.util.ArrayList;

public class CppClass {
    public String className;
    public ArrayList<CppMethod> methods;

    public CppClass(String className){
        this.className = className;
    }
    public void addMethod(CppMethod methodToAdd){
        methods.add(methodToAdd);
    }
}
