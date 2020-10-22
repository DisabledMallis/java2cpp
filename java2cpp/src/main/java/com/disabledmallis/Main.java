package com.disabledmallis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

    static String dotMC = System.getenv("APPDATA")+"/.minecraft/";

    public static void main(String[] args) throws Exception {

        String mcJarPath = dotMC+"versions/1.8.9/1.8.9.jar";
        JarFile mcJar = new JarFile(mcJarPath);
        Enumeration<JarEntry> entryEnumeration = mcJar.entries();

        ArrayList<CppClass> classes = new ArrayList<>();

        while(entryEnumeration.hasMoreElements()){
            JarEntry entry = entryEnumeration.nextElement();
            if(entry.isDirectory() || !entry.getName().endsWith(".class") || entry.getName().contains("$")){
                continue;
            }

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
                    //System.out.println("Added Field: "+fieldName+" of type: "+fieldType+" to class "+className);
                }
                List<MethodDeclaration> methods = compilationUnit.findAll(MethodDeclaration.class);
                for(MethodDeclaration decl : methods){
                    String methodName = decl.getNameAsString();
                    String methodType = decl.getTypeAsString();
                    cppClass.methods.add(new CppMethod(methodType, methodName));
                    //System.out.println("Added Method: "+methodName+" of type: "+methodType+" to class "+className);
                }

                classes.add(cppClass);
            }catch (Exception ex){
                ex.printStackTrace();
                System.out.println("Erroring class: "+entry.getName());
            }
        }

        for(CppClass cppClass : classes){
            if(cppClass.className.equals("ave")){
                System.out.println("GENERATED MINECRAFT.H");
                System.out.println(cppClass.genClass());
                System.out.println("END OF MINECRAFT.H");
            }
        }
    }
}
