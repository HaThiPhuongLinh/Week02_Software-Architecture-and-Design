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
    private ErrorReport errorReport = new ErrorReport();

    public void printErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    public void checkLegalPackage(String pkgName) {
        String regex = "com\\.edu\\.fit.*";

//        if (!pkgName.matches(regex)) {
//            System.out.println(ANSI_RED + "Package [" + pkgName + "] is invalid (must follow 'com.edu.fit.*' (*:anything)) " + ANSI_RESET);
//        }
        if (!pkgName.matches(regex)) {
            errorReport.addError("Package", pkgName, " must follow 'com.edu.fit.*' (*:anything)");
        }
    }


    public void checkLegalClass(String className, ClassOrInterfaceDeclaration classDeclaration) {
        boolean startsWithUpperCase = Character.isUpperCase(className.charAt(0));
        boolean isNoun = pos.isNoun(className);
        boolean hasClassDesc = hasClassDescription(classDeclaration);

//        if (!startsWithUpperCase || !isNoun || !hasClassDesc) {
//            System.out.print(ANSI_RED + "Class [" + className + "] ");
//
//            if (!startsWithUpperCase) {
//                System.out.print("must start with an uppercase letter");
//                if (!isNoun || !hasClassDesc) {
//                    System.out.print(" and ");
//                }
//            }
//
//            if (!isNoun) {
//                System.out.print("must be a noun or noun phrase");
//                if (!hasClassDesc) {
//                    System.out.print(" and ");
//                }
//            }
//
//            if (!hasClassDesc) {
//                System.out.print("must have a class description");
//            }
//
//            System.out.println(ANSI_RESET);
//        }
        if (!startsWithUpperCase || !isNoun || !hasClassDesc) {
            String errorMsg = " ";

            if (!startsWithUpperCase) {
                errorMsg += "must start with an uppercase letter";
                if (!isNoun || !hasClassDesc) {
                    errorMsg += " and ";
                }
            }

            if (!isNoun) {
                errorMsg += "must be a noun or noun phrase";
                if (!hasClassDesc) {
                    errorMsg += " and ";
                }
            }

            if (!hasClassDesc) {
                errorMsg += "must have a class description";
            }

            errorReport.addError("Class", className, errorMsg);
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

//        if (isConstant) {
//            if (!isInInterface(fieldDeclaration) || !isUpperCase(fieldDeclaration.getVariable(0).getNameAsString())) {
//                System.out.println(ANSI_RED + "Constant [" + fieldDeclaration.getVariable(0).getNameAsString() + "] is invalid" + ANSI_RESET);
//            }
//        } else {
//            if (!startsWithLowerCase || !isNoun) {
//                System.out.print(ANSI_RED + "Field [" + fieldDeclaration.getVariable(0).getNameAsString() + "]");
//                if (!startsWithLowerCase) {
//                    System.out.print(" must start with a lowercase letter");
//                }
//                if (!startsWithLowerCase && !isNoun) {
//                    System.out.print(" and");
//                }
//                if (!isNoun) {
//                    System.out.print(" must be a noun or noun phrase");
//                }
//                System.out.println(ANSI_RESET);
//            }
//        }
        if (isConstant) {
            if (!isInInterface(fieldDeclaration) && !isUpperCase(fieldDeclaration.getVariable(0).getNameAsString())) {
                errorReport.addError("Constant", fieldDeclaration.getVariable(0).getNameAsString(), " must be uppercase letter and in Interface");
            }
            if (!isInInterface(fieldDeclaration)) {
                errorReport.addError("Constant", fieldDeclaration.getVariable(0).getNameAsString(), " must be in Interface");
            }
            if (!isUpperCase(fieldDeclaration.getVariable(0).getNameAsString())) {
                errorReport.addError("Constant", fieldDeclaration.getVariable(0).getNameAsString(), " must be uppercase letter");
            }
        } else {
            if (!startsWithLowerCase || !isNoun) {
                String errorMsg = " ";

                if (!startsWithLowerCase) {
                    errorMsg += "must start with a lowercase letter";
                }

                if (!startsWithLowerCase && !isNoun) {
                    errorMsg += " and";
                }

                if (!isNoun) {
                    errorMsg += "must be a noun or noun phrase";
                }

                errorReport.addError("Field", fieldDeclaration.getVariable(0).getNameAsString(), errorMsg);
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

//        if (!methodName.equals("hashCode") && !methodName.equals("equals") && !methodName.equals("toString") && !methodName.startsWith("get") && !methodName.startsWith("set") && !methodName.equals("main")) {
//
//            if (!startsWithLowerCase || !isVerb || !hasMethodDesc) {
//                System.out.print(ANSI_RED + "Method [" + methodName + "]");
//
//                if (!startsWithLowerCase) {
//                    System.out.print(" must start with a lowercase letter");
//                    if (!isVerb || !hasMethodDesc) {
//                        System.out.print(" and ");
//                    }
//                }
//
//                if (!startsWithLowerCase && !isVerb) {
//                    System.out.print(" and");
//                }
//
//                if (!isVerb) {
//                    System.out.print(" must be a verb");
//                    if (!hasMethodDesc) {
//                        System.out.print(" and ");
//                    }
//                }
//
//                if (!hasMethodDesc) {
//                    System.out.print("must have a method description");
//                }
//
//                System.out.println(ANSI_RESET);
//            }
//        }
        if (!methodName.equals("hashCode") && !methodName.equals("equals") && !methodName.equals("toString") && !methodName.startsWith("get") && !methodName.startsWith("set") && !methodName.equals("main")) {
            String errorMsg = " ";

            if (!startsWithLowerCase) {
                errorMsg += "must start with a lowercase letter";
                if (!isVerb || !hasMethodDesc) {
                    errorMsg += " and ";
                }
            }

            if (!startsWithLowerCase && !isVerb) {
                errorMsg += " and";
            }

            if (!isVerb) {
                errorMsg += "must be a verb";
                if (!hasMethodDesc) {
                    errorMsg += " and ";
                }
            }

            if (!hasMethodDesc) {
                errorMsg += "must have a method description";
            }

            errorReport.addError("Method", methodName, errorMsg);
        }
    }

    public boolean hasMethodDescription(MethodDeclaration methodDeclaration) {
        String comments = methodDeclaration.getComment().toString();
        return comments.isEmpty();
    }
}
