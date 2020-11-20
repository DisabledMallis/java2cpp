package com.disabledmallis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;

import javax.sound.midi.SysexMessage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

    static String dotMC = System.getenv("APPDATA")+"/.minecraft/";
    static String lastClass = "";

    static Semaphore mutex = new Semaphore(1);

    public static ArrayList<CppClass> classes = new ArrayList<>();

    public static ArrayList<Thread> threads = new ArrayList<>();
    public static ArrayList<Boolean> done = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String mcJarPath = dotMC+"versions/1.8.9/1.8.9.jar";
        JarFile mcJar = new JarFile(mcJarPath);
        Enumeration<JarEntry> entryEnumeration = mcJar.entries();

        ArrayList<String> erroredClass = new ArrayList<>();


        Logger.Log("Reading & decompiling jar...");
        /*
        Reading phase
         */
        while(entryEnumeration.hasMoreElements()){
            JarEntry entry = entryEnumeration.nextElement();
            if(entry.isDirectory() || !entry.getName().endsWith(".class") || entry.getName().contains("$")){
                continue;
            }

            threads.add(new Thread(()  -> {
                lastClass = entry.getName();
                JDLoader loader = new JDLoader(mcJarPath);
                JDPrinter printer = new JDPrinter();
                try{
                    ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();
                    decompiler.decompile(loader, printer, entry.getName());

                    String className = entry.getName().replace(".class", "");
                    CppClass cppClass = new CppClass(className);

                    String source = printer.toString().replace('\u2603', 'x');
                    CompilationUnit compilationUnit = StaticJavaParser.parse(source);

                    NodeList<ImportDeclaration> importDeclarations = compilationUnit.getImports();
                    List<FieldDeclaration> fields = compilationUnit.findAll(FieldDeclaration.class);
                    for(FieldDeclaration decl : fields){
                        String fieldName = decl.getVariable(0).getNameAsString();
                        String fieldType = decl.getVariable(0).getTypeAsString().replace('.','/');;
                        for(ImportDeclaration declaration : importDeclarations){
                            String[] theSplit = declaration.getNameAsString().split("\\.");
                            String importedClass = theSplit[theSplit.length-1];
                            //Logger.Log("Found import "+importedClass);
                            if(importedClass.equals(fieldType)){
                                fieldType = declaration.getNameAsString().replace('.','/');
                            }
                        }
                        cppClass.fields.add(new CppField(fieldType, fieldName));
                        //Logger.Log("Added Field: "+fieldName+" of type: "+fieldType+" to class "+className);
                    }
                    /*List<MethodDeclaration> methods = compilationUnit.findAll(MethodDeclaration.class);
                    for(MethodDeclaration decl : methods){
                        String methodName = decl.getNameAsString();
                        String methodType = decl.getTypeAsString().replace('.','/');
                        cppClass.methods.add(new CppMethod(methodType, methodName));
                        //Logger.Log("Added Method: "+methodName+" of type: "+methodType+" to class "+className);
                    }*/

                    classes.add(cppClass);
                }catch (Exception ex) {
                    //ex.printStackTrace();
                    erroredClass.add(entry.getName());
                    //.Log("Erroring class: " + entry.getName());
                }


                try {
                    mutex.acquire();
                    done.add(true);
                    mutex.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }

        for (Thread t : threads) {
            t.setDaemon(true);
            t.setPriority(10);
            t.start();
        }

        while (done.size() < threads.size() - 1) {
            Thread.sleep(100);
        }; //Logger.Log(done.size() + " " + threads.size());

        for (String s : erroredClass) {
            Logger.Log("Failed to read class " + s);
        }

        /*
        Deobfuscating phase
         */
        Logger.Log("Deobfuscating classes...");
        String srgPath = "A:/Downloads/mcp-1.8.9-srg/joined.srg";
        List<String> lines = Files.readAllLines(Paths.get(srgPath), StandardCharsets.UTF_8);
        lines.forEach(lineStr -> {
            SRGLine srgLine = new SRGLine(lineStr);

            //Map the class names
            if(srgLine.getType().equals(SRG_Type.Class)){
                ClassSRGLine classLine = new ClassSRGLine(lineStr);
                String obfClassName = classLine.obfuscatedName();
                for(CppClass cppClass : classes){
                    if(cppClass.obfuscatedName.equals(obfClassName)){
                        cppClass.setUnmappedName(classLine.deobfuscatedName());
                        cppClass.setMappedName(classLine.deobfuscatedName());
                    }
                }
            }
        });

        /*
        Mapping phase
         */
        Logger.Log("Mapping classes...");


        /*
        Test generation phase
         */
        Logger.Log("Test generating classes...");
        for(CppClass cppClass : classes){
                Logger.Log("GENERATED " + cppClass.mappedName + ".H");
                Logger.Log(cppClass.genClass());
                Logger.Log("END OF " + cppClass.mappedName + ".H");
        }

        /*
        Dumping phase
         */
        Logger.Log("Dumping classes as *.h");
    }
}
