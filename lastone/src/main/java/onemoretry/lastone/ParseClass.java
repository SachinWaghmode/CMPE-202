package onemoretry.lastone;

import java.io.File;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParseClass extends VoidVisitorAdapter{
	static String out = "";
	static String resul = "";
	public static String pUmlinput = "";
	String classname = "[";
    String methodname ="";
    String modifier ="";
    String resultstring="";
    
	Map<String, String> allclasses = new HashMap<String, String>();

	// public static void main(String[] args) throws Exception {

	// FileInputStream in = new
	// FileInputStream("/Users/sachinwaghmode/eclipse/lastone/src/main/TestCase1/");
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        
        classname += n.getName();
        System.out.println(classname);
        super.visit(n, arg);
     }
	
	
    
    
	public void compile(String inputfil) {
		// parse the file
		File config = new File(inputfil);
		File[] javafileset = config.listFiles();
		if (javafileset != null) {
			for (File javaFile : javafileset) {
				if (javaFile.isDirectory()) {
					continue;
				} else if (!(javaFile.getAbsolutePath().endsWith(".java"))) {
					continue;
				}
				CompilationUnit javaClass = null;
				try {
					javaClass = JavaParser.parse(javaFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new ParseClass().visit(javaClass, null) ;
				allclasses.put(classname, javaClass.toString());

				System.out.println(javaClass.toString());
				System.out.println(classname);
			}
		}
		}
		 
                
        
        
        
        
	// CompilationUnit cu = JavaParser.parse(inputfil);

	// new MethodVisitor().visit(cu, null);
	// new yUMLClass().generateClassdiagram(resul);
	// new VariableVisitor().visit(cu, null);

}
