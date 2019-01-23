package fr.kriszt.theo.NodeEntities;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationEntity extends NodeEntity {

    private MethodInvocation methodInvocation;
    private TypeEntity callingClass;
    private MethodDeclaration callingMethod;

    private static ArrayList<MethodInvocationEntity> invocations = new ArrayList<>();


    public MethodInvocationEntity(String miName, TypeEntity currentType, MethodDeclaration methodDeclaration, MethodInvocation methodInvocation) {
        super(miName);
        this.methodInvocation = methodInvocation;
        this.callingMethod = methodDeclaration;
        this.callingClass = currentType;

        invocations.add(this);
//        System.out.println("===> MIE :: " + this);
    }



    public static boolean bind(ApplicationEntity application){

        List<ClassEntity> classes = application.getClasses();

        for (MethodInvocationEntity mie : invocations){
            if (! mie.bindClass(classes)){
//                System.err.println("Impossible de binder " + mie);
//                return false;
            }
        }

        return true;
    }

    private boolean bindClass(List<ClassEntity> classes){

        System.out.println("Binding de l'appel de méthode " + name);
        System.out.println("\tInvocation : " + methodInvocation);

        IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
        System.out.println("methodBinding : " + methodBinding);

        System.out.flush();

        int isStatic  = Modifier.STATIC & methodBinding.getModifiers();
        if (isStatic > 0) {
            // method is static method
        } else {
            // method is not static
        }

//        System.out.println("Methode interne appellante : " + callingMethod.getName());
        System.out.println("Recherche de la méthode " + signature() + " dans " );
        for (MethodEntity me : callingClass.getMethods()){
            if (me.toString().equals(signature())){
                System.out.println("\tInscription de l'invocation de " + signature() + " à " + methodInvocation.getExpression());
                Expression expression = methodInvocation.getExpression();
                System.out.println(expression.resolveTypeBinding().getDeclaringClass());
                me.addInvocation( this );
                return true;
            }
        }
//        if (callingClass.methods.contains()){
//
//        }

        // find parent class
//        ClassEntity caller = null;
//        for (ClassEntity ce : classes){
//            if (ce.equals(callingClass)){
//                return true;
//            }
//        }

        return false;

    }

    private String signature(){
        String returnType;
        if (callingMethod.isConstructor()){
            returnType = callingClass.name;
        }else if (callingMethod.getReturnType2() != null){
            returnType = callingMethod.getReturnType2().toString();
        }else returnType = "void";


        return returnType + " " +
                callingClass.name + "." +
                callingMethod.getName() + "(" +
                callingMethod.parameters().toString().replaceAll("\\[|]", "") +
                ")";
    }
}
