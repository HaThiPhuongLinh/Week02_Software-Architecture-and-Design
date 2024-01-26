package com.edu.fit.tool;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

public class LegalChecking {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private static final POSTagging pos = new POSTagging();

    public void checkLegalPackage(String pkgName) {
        String regex = "com\\.edu\\.fit.*";

        if (!pkgName.matches(regex)) {
            System.out.println(ANSI_RED + "Package [" + pkgName + "] is invalid (must follow 'com.edu.fit.*' (*:anything)) " + ANSI_RESET);
        }
    }

    public void checkLegalClass(String className, ClassOrInterfaceDeclaration classDeclaration) {
        boolean startsWithUpperCase = Character.isUpperCase(className.charAt(0));
        boolean isNoun = pos.isNoun(className);
        boolean hasClassDesc = hasClassDescription(classDeclaration);

        if (!startsWithUpperCase || !isNoun || !hasClassDesc) {
            System.out.print(ANSI_RED + "Class [" + className + "] ");

            if (!startsWithUpperCase) {
                System.out.print("must start with an uppercase letter");
                if (!isNoun || !hasClassDesc) {
                    System.out.print(" and ");
                }
            }

            if (!isNoun) {
                System.out.print("must be a noun or noun phrase");
                if (!hasClassDesc) {
                    System.out.print(" and ");
                }
            }

            if (!hasClassDesc) {
                System.out.print("must have a class description");
            }

            System.out.println(ANSI_RESET);
        }
    }

    public boolean hasClassDescription(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        List<String> requiredComments = List.of("author", "created-date");
        String comments = classOrInterfaceDeclaration.getComment().toString();

        for (String re : requiredComments) {
            if (comments.contains(re)) {
                return true;
            }
        }

        return false;
    }

    public void checkLegalField(String fieldName, FieldDeclaration fieldDeclaration) {
        boolean isConstant = fieldDeclaration.isFinal();
        boolean startsWithLowerCase = Character.isLowerCase(fieldName.charAt(0));
        boolean isNoun = pos.isNoun(fieldName);

        if (isConstant) {
            if (!isInInterface(fieldDeclaration) || !isUpperCase(fieldDeclaration.getVariable(0).getNameAsString())) {
                System.out.println(ANSI_RED + "Constant [" + fieldDeclaration.getVariable(0).getNameAsString() + "] is invalid" + ANSI_RESET);
            }
        } else {
            if (!startsWithLowerCase || !isNoun) {
                System.out.print(ANSI_RED + "Field [" + fieldDeclaration.getVariable(0).getNameAsString() + "]");
                if (!startsWithLowerCase) {
                    System.out.print(" must start with a lowercase letter");
                }
                if (!startsWithLowerCase && !isNoun) {
                    System.out.print(" and");
                }
                if (!isNoun) {
                    System.out.print(" must be a noun or noun phrase");
                }
                System.out.println(ANSI_RESET);
            }
        }
    }

    public boolean isUpperCase(String str) {
        return str.equals(str.toUpperCase());
    }

    public boolean isInInterface(FieldDeclaration fieldDeclaration) {
        Node parentNode = fieldDeclaration.getParentNode().get();
        if (parentNode instanceof ClassOrInterfaceDeclaration c) {
            return c.isInterface();
        }
        return false;
    }

    public void checkLegalMethod(String methodName, MethodDeclaration methodDeclaration) {
        boolean startsWithLowerCase = Character.isLowerCase(methodName.charAt(0));
        String[] tokens = methodName.split("(?=[A-Z])");
        boolean isVerb = pos.isVerb(tokens[0]);
        boolean hasMethodDesc = hasMethodDescription(methodDeclaration);

        if (!methodName.equals("hashCode") && !methodName.equals("equals") && !methodName.equals("toString") && !methodName.startsWith("get") && !methodName.startsWith("set") && !methodName.equals("main")) {

            if (!startsWithLowerCase || !isVerb || !hasMethodDesc) {
                System.out.print(ANSI_RED + "Method [" + methodName + "]");

                if (!startsWithLowerCase) {
                    System.out.print(" must start with a lowercase letter");
                    if (!isVerb || !hasMethodDesc) {
                        System.out.print(" and ");
                    }
                }

                if (!startsWithLowerCase && !isVerb) {
                    System.out.print(" and");
                }

                if (!isVerb) {
                    System.out.print(" must be a verb");
                    if (!hasMethodDesc) {
                        System.out.print(" and ");
                    }
                }

                if (!hasMethodDesc) {
                    System.out.print("must have a method description");
                }

                System.out.println(ANSI_RESET);
            }
        }
    }

    public boolean hasMethodDescription(MethodDeclaration methodDeclaration) {
        String comments = methodDeclaration.getComment().toString();
        return comments.isEmpty();
    }
}
