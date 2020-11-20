package com.disabledmallis;

public class SRGLine {
    private String line = "";
    public SRGLine(String line){
        this.line = line;
    }

    public String getLine(){
        return this.line;
    }
    public SRG_Type getType(){
        String prefix = line.split(" ")[0];
        if(prefix.startsWith("PK:")){
            return SRG_Type.Package;
        }
        if(prefix.startsWith("CL:")){
            return SRG_Type.Class;
        }
        if(prefix.startsWith("FD:")){
            return SRG_Type.Field;
        }
        if(prefix.startsWith("MD:")){
            return SRG_Type.Method;
        }
        return SRG_Type.Unknown;
    }
}
