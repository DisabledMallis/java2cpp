package com.disabledmallis;

public class FieldSRGLine extends SRGLine{
    public FieldSRGLine(String line) {
        super(line);
    }

    private String obfHalf(){
        return getLine().split(" ")[1];
    }
    private String deobfHalf(){
        return getLine().split(" ")[2];
    }

    public String deobfName(){
        String deobfHalf = deobfHalf();
        return Utils.getChildFromPath(deobfHalf);
    }
    public String obfName(){
        String obfHalf = obfHalf();
        return Utils.getChildFromPath(obfHalf);
    }

    public ClassSRGLine getClassSRG(){
        String obfSub = obfClassName();
        String deobfSub = deobfClassPath();
        return new ClassSRGLine("CL: #OBF #MAP".replace("#OBF", obfSub).replace("#MAP", deobfSub));
    }
    public String obfClassName(){
        String obfHalf = obfHalf();
        String obfClass = Utils.removeChildFromPath(obfHalf);
        return obfClass;
    }
    public String deobfClassName(){
        String deobfHalf = deobfHalf();
        String deobfClass = Utils.getChildFromPath(Utils.removeChildFromPath(deobfHalf));
        return deobfClass;
    }
    public String deobfClassPath(){
        String deobfHalf = deobfHalf();
        String deobfClass = Utils.removeChildFromPath(deobfHalf);
        return deobfClass;
    }
}
