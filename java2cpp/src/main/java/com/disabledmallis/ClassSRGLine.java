package com.disabledmallis;

public class ClassSRGLine extends SRGLine {
    public ClassSRGLine(String line) {
        super(line);
    }

    private String obfHalf(){
        return getLine().split(" ")[1];
    }
    private String deobfHalf(){
        return getLine().split(" ")[2];
    }

    public String obfuscatedName(){
        int end = obfHalf().length();
        if(hasSubClass() || isSplitClass()){
            end = obfHalf().indexOf('$');
        }
        return obfHalf().substring(0, end);
    }
    public String deobfuscatedName(){
        int end = deobfHalf().length();
        if(hasSubClass() || isSplitClass()){
            end = deobfHalf().indexOf('$');
        }
        String deobfPath = deobfHalf().substring(0, end);
        String deobfName = Utils.getChildFromPath(deobfPath);
        return deobfName;
    }

    public ClassSRGLine getSubclass(){

        String obfHalf = obfHalf();
        int obfEnd = obfHalf.indexOf('$');
        String obfSub = obfHalf.substring(obfEnd+1);


        String deobfHalf = deobfHalf();
        int deobfEnd = deobfHalf.indexOf('$');
        String deobfSub = deobfHalf.substring(deobfEnd+1);

        return new ClassSRGLine("CL: #OBF #MAP".replace("#OBF", obfSub).replace("#MAP", deobfSub));
    }

    public boolean isSplitClass(){
        String obfName = obfHalf();
        if(obfName.contains("$")){
            int splitterLoc = obfName.indexOf("$");
            char subClassName = obfName.charAt(splitterLoc+1);
            if(Character.isDigit(subClassName)){
                return true;
            }
        }
        return false;
    }
    public boolean hasSubClass(){
        String obfName = obfHalf();
        if(obfName.contains("$")){
            int splitterLoc = obfName.indexOf("$");
            char subClassName = obfName.charAt(splitterLoc+1);
            if(Character.isLetter(subClassName)){
                return true;
            }
        }
        return false;
    }
}
