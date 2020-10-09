package com.disabledmallis;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JDLoader implements Loader {

    String jarPath = "";
    public void setJarPath(String path){
        jarPath = path;
    }
    @Override
    public byte[] load(String internalName) throws LoaderException {
        try{
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while(entries.hasMoreElements()){
                JarEntry next = entries.nextElement();
                if(next.isDirectory() || !next.getName().endsWith(".class")){
                    continue;
                }
                
            }
            InputStream is = new FileInputStream();

            if (is == null) {
                return null;
            } else {
                try (InputStream in=is; ByteArrayOutputStream out=new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int read = in.read(buffer);

                    while (read > 0) {
                        out.write(buffer, 0, read);
                        read = in.read(buffer);
                    }

                    return out.toByteArray();
                } catch (IOException e) {
                    throw new LoaderException(e);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canLoad(String internalName) {
        return true;//this.getClass().getResource("/" + internalName + ".class") != null;
    }
}
