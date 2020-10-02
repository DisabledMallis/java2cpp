package com.disabledmallis;

import com.disabledmallis.Out;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static String cwd = System.getProperty("user.dir");

    public static void genCppFileFor(String classPath){
        File classPathFile = new File(cwd+"/gen/"+classPath);
        File parentDir = classPathFile.getParentFile();
        if(!parentDir.exists()){
            if(!parentDir.mkdirs()){
                Out.Out("Failed to create "+parentDir.getAbsolutePath());
                return;
            }
        }
        try {
            new File(classPathFile+".h").createNewFile();
            new File(classPathFile+".cpp").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleClassLine(String classMapping)
    {
        String mappedName = classMapping.split(" ")[0];
        String mcpName = classMapping.split(" ")[1];
        int isntClean = mcpName.indexOf("$");
        if(isntClean != -1){
            //Out.Out(mcpName + " isnt clean, skipping...");
            return;
        }
        genCppFileFor(mcpName);


        //Out.Out(mcpName);
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        Out.Out("Java2Cpp - Made by DisabledMallis");
        Out.Out("Generate C++ jni code from java mappings");

        Out.Out("CWD: "+cwd);

        Out.Out("Absolute path to SRG: ");
        String pathToSrg = "A:\\Downloads\\mcp-1.8.9-srg\\joined.srg";//input.nextLine();

        String srgContent = "";
        for(String line : Files.readAllLines(Paths.get(pathToSrg))) {
            if(line.startsWith("CL: ")){
                handleClassLine(line.substring(4));
            }
            srgContent += line+System.lineSeparator();
        }

        File outDir = new File(cwd+"/gen");
        if(!outDir.exists()){
            if(!outDir.mkdirs()){
                Out.Out("Failed to create "+outDir.getAbsolutePath());
                return;
            }
        }

        Out.Out("Finished");
        //Out.Out(srgContent);
    }
}
