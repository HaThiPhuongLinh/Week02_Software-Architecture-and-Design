package com.edu.fit.example;


import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class DemoParser {
    private void getPackage(File filePath) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(filePath);
        List<PackageDeclaration> packages = cu.findAll(PackageDeclaration.class);
        packages.forEach(System.out::println);
    }

    private void getAllMethods(File filePath) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(filePath);
        List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration method: methods){
            System.out.println(
                    method.getType() + " " + method.getName()
            );
        }
    }

    private void getAllField(File filePath) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(filePath);
        List<FieldDeclaration> fields = cu.findAll(FieldDeclaration.class);
        fields.forEach(System.out::println);
        }


    public static void main(String[] args) throws FileNotFoundException {
        DemoParser parserToolES = new DemoParser();
        File file = new File("D:\\Architecture (JavaParser)\\ParserTool\\src\\main\\java\\vn\\com\\edu\\fit\\models\\Car.java");
        parserToolES.getAllField(file);
        parserToolES.getAllMethods(file);
        parserToolES.getPackage(file);
    }
}
