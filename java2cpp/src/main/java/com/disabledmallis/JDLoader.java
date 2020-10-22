package com.disabledmallis;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JDLoader implements Loader {

    String jarPath = "";
    public JDLoader(String jarPath){
        this.jarPath = jarPath;
    }

    @Override
    public boolean canLoad(String s) {
        return true;
    }

    @Override
    public byte[] load(String internalName) throws LoaderException {
        try{
            if(!internalName.endsWith(".class")){
                internalName += ".class";
            }
            URL url = new URL("jar:file:"+jarPath+"!/"+internalName);
            InputStream in = url.openStream();
            ByteArrayOutputStream out=new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int read = in.read(buffer);

            while (read > 0) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }

            return out.toByteArray();
        }
        catch (IOException ex){
            throw new LoaderException(ex);
        }
    }
}
