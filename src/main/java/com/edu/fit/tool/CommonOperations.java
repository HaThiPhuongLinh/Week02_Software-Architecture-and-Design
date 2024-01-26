package com.edu.fit.tool;

import com.edu.fit.example.DirExplorer;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import dev.mccue.guava.base.Strings;

import java.io.File;

public class CommonOperations {
    private static LegalChecking legalChecking = new LegalChecking();

    public static void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<>() {
                    @Override
                    public void visit(PackageDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println("(Package) " + n.getNameAsString());
                        legalChecking.checkLegalPackage(n.getNameAsString());
                    }

                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [ " + n.getBegin() + "] (Class) " + n.getNameAsString());
                        legalChecking.checkLegalClass(n.getNameAsString(), n);
                    }

                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [ " + n.getBegin() + "] (Method) " + n.getDeclarationAsString());
                        legalChecking.checkLegalMethod(n.getNameAsString(), n);
                    }


                    @Override
                    public void visit(FieldDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [ " + n.getBegin() + "] (Field) " + n);
                        legalChecking.checkLegalField(n.getVariable(0).getNameAsString(), n);
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
        File projectDir = new File("D:\\DemoParser");
        listMethodCalls(projectDir);
    }
}
