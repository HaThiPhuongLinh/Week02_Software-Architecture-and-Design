package com.edu.fit.example;

import com.edu.fit.example.DirExplorer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import dev.mccue.guava.base.Strings;

import java.io.File;

public class ListClassesExample {
    public static void listClasses(File projectDir){
        new DirExplorer((level, path, file) -> path.endsWith(".java"),(level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=",path.length()));
            try{
                new VoidVisitorAdapter<>(){
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg){
                        super.visit(n, arg);
                        System.out.println(" * " + n.getName());
                    }
                }.visit(StaticJavaParser.parse(file), null);
                System.out.println();
            } catch (Exception e) {
                new RuntimeException(e);
            }
            return true;
        }).explore(projectDir);
    }

    public static void main(String[] args) {
        File projectDir = new File("C:\\Users\\ThanhTu\\Desktop\\DEMO_PT\\GiuaKy_PhanTanJava_2023");
        listClasses(projectDir);
    }
}
