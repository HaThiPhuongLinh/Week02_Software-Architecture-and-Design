package vn.com.edu.fit.tool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import dev.mccue.guava.base.Strings;

import java.io.File;

public class CommonOperations {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void listMethodCalls(File projectDir){
        new DirExplorer((level, path, file) -> path.endsWith(".java"),(level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=",path.length()));
            try{
                new VoidVisitorAdapter<>(){
                    @Override
                    public void visit(PackageDeclaration n, Object arg){
                        super.visit(n, arg);
                        System.out.println("Package: "+n.getNameAsString());
                        if (!checkLegalPackage(n.getNameAsString())) {
                            System.out.println(ANSI_RED + "Package [" + n.getNameAsString() + "] is invalid (must follow 'com.companyname.*' (*:anything)) "+ ANSI_RESET);
                        }
                    }
                    @Override
                    public void visit(FieldDeclaration n, Object arg){
                        super.visit(n, arg);
                        System.out.println(" [ " + n.getBegin() + "]" + n);
                    }
                    @Override
                    public void visit(MethodDeclaration n, Object arg){
                        super.visit(n, arg);
                        System.out.println(" [ " + n.getBegin() + "]" + n.getDeclarationAsString());
                    }


                }.visit(StaticJavaParser.parse(file), null);
                System.out.println();
            } catch (Exception e) {
                new RuntimeException(e);
            }
            return true;
        }).explore(projectDir);
    }

    static boolean checkLegalPackage(String pkgName) {
        String regex = "com\\.\\w+\\..*";

        if (pkgName.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    static boolean checkLegalClass(String className) {
        String regex = "^com\\.[\\w]+";

        if (className.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        File projectDir = new File("D:\\DemoParser");
        listMethodCalls(projectDir);
    }
}
