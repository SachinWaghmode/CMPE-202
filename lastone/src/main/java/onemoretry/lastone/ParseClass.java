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
	Map<String, String> allAttributes = new HashMap<String, String>();

	
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        List<BodyDeclaration<?>> classList = n.getMembers() ;
        classname = "class " ;
        resul += classname + n.getName() + " {"; 
        
	for (BodyDeclaration bd : classList )
        {
        	if(bd instanceof FieldDeclaration)
        	{
        		FieldDeclaration attribute = (FieldDeclaration) bd ;
        		if (attribute.getModifiers().toString().compareToIgnoreCase("public")==0)
        			modifier = "+";
        		else if (attribute.getModifiers().toString().compareToIgnoreCase("[private]")==0)
        			modifier = "-";
        		else
        			modifier = "#";
        		
        		
        		resul += "\n" + modifier ;
        		List<VariableDeclarator> attributeList = attribute.getVariables();
        				for (VariableDeclarator var : attributeList)
        				{
        					if (var.getType() instanceof ReferenceType)
        					{
        						if (var.getType().toString().contains("[]"))
        						{
        							resul += "\n" + modifier ;
                					resul += var.getType();
                					resul += var.getName();
                					allAttributes.put(var.getType().toString(),var.getName().toString());
        						}
        							
        					}else{
        						resul += "\n" + modifier ;
        					resul += var.getType();
        					resul += var.getName();
        					allAttributes.put(var.getType().toString(),var.getName().toString());
        					
        					}
        					
        				}
        	}
        	
        
             
       
        }
        resul += "\n}\n";
        super.visit(n, arg);
     }
	

	
	
    
    
	public void compile(String inputfile, String outputfile ) {
		// parse the file
		File config = new File(inputfile);
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

				//System.out.println(javaClass.toString());
				//System.out.println(classname);
				generatePlantUML(resul,outfile);
				
				}
		  }
		}
		
	public void generatePlantUML(String intermediateCode, String outpath){
	 String startUML = "@startuml";
	 String endUML = "@enduml";
	 String result =" ";
	 
	 result = startUML + "\n" + intermediateCode + "\n" + endUML;
	 
	 SourceStringReader s = new SourceStringReader(result);
	 try
	 {
		 FileOutputStream f = new FileOutputStream (outpath);
		 s.generateImage(f);
		 f.close();
	 
	 }
	 catch (Exception e){
		 System.out.println(e);
	 }
	 
	}
	
	
	
	
	
	
	
	
	
                
        
        
        
        
	// CompilationUnit cu = JavaParser.parse(inputfil);

	// new MethodVisitor().visit(cu, null);
	// new yUMLClass().generateClassdiagram(resul);
	// new VariableVisitor().visit(cu, null);

}
