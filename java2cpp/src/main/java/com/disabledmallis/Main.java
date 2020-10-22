package com.disabledmallis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

    static String dotMC = System.getenv("APPDATA")+"/.minecraft/";
    static String lastClass = "";

    public static ArrayList<CppClass> classes = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String mcJarPath = dotMC+"versions/1.8.9/1.8.9.jar";
        JarFile mcJar = new JarFile(mcJarPath);
        Enumeration<JarEntry> entryEnumeration = mcJar.entries();

        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Boolean> done = new ArrayList<>();
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

            threads.add(new Thread(() -> {
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

                    List<FieldDeclaration> fields = compilationUnit.findAll(FieldDeclaration.class);
                    for(FieldDeclaration decl : fields){
                        String fieldName = decl.getVariable(0).getNameAsString();
                        String fieldType = decl.getVariable(0).getTypeAsString();
                        cppClass.fields.add(new CppField(fieldType, fieldName));
                        //Logger.Log("Added Field: "+fieldName+" of type: "+fieldType+" to class "+className);
                    }
                    List<MethodDeclaration> methods = compilationUnit.findAll(MethodDeclaration.class);
                    for(MethodDeclaration decl : methods){
                        String methodName = decl.getNameAsString();
                        String methodType = decl.getTypeAsString();
                        cppClass.methods.add(new CppMethod(methodType, methodName));
                        //Logger.Log("Added Method: "+methodName+" of type: "+methodType+" to class "+className);
                    }

                    classes.add(cppClass);
                }catch (Exception ex) {
                    erroredClass.add(entry.getName());
                    //.Log("Erroring class: " + entry.getName());
                }

                done.add(true);
            }));
        }

        for (Thread t : threads) {
            t.setDaemon(true);
            t.setPriority(10);
            t.start();
        }

        /*
        Deobfuscating phase
         */
        Logger.Log("Deobfuscating classes...");

        /*
        Mapping phase
         */
        Logger.Log("Mapping classes...");

        while (done.size() < threads.size() - 1) Thread.sleep(100); //Logger.Log(done.size() + " " + threads.size());

        for (String s : erroredClass) {
            Logger.Log("Failed to generate class " + s);
        }

        for(CppClass cppClass : classes){
            if(cppClass.unmappedClassName.equals("ave")){
                Logger.Log("GENERATED MINECRAFT.H");
                Logger.Log(cppClass.genClass());
                Logger.Log("END OF MINECRAFT.H");
            }
        }

        /*
        Dumping phase
         */
        Logger.Log("Dumping classes as *.h");
    }
}
