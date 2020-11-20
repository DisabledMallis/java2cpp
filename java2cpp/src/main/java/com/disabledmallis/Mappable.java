package com.disabledmallis;

public abstract class Mappable {
    public String obfuscatedName;
    public String unmappedName;
    public String mappedName;

    public Mappable(String obfuscatedName){
        this.obfuscatedName = obfuscatedName;
        this.unmappedName = obfuscatedName;
        this.mappedName = obfuscatedName;
    }
    public void setObfuscatedName(String unmappedName){
        this.obfuscatedName = obfuscatedName;
        this.unmappedName = obfuscatedName;
        this.mappedName = obfuscatedName;
    }
    public void setUnmappedName(String unmappedName){
        this.unmappedName = unmappedName;
        this.mappedName = unmappedName;
    }
    public void setMappedName(String mappedName){
        this.mappedName = mappedName;
    }
}
