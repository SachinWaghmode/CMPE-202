package onemoretry.lastone;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassVisitor extends VoidVisitorAdapter<Object> {
    @Override
    public void visit(ClassOrInterfaceDeclaration coi, Object arg) {
        /* here you can access the attributes of the method.
         this method will be called for all methods in this 
         CompilationUnit, including inner class methods */
        System.out.println(coi.getName());
        super.visit(coi, arg);
     }
    

	
}
