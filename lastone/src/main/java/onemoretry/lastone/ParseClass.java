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
	static String nonPrimitive="";
	static String finalG="";
	static String drawline="";
	public static String pUmlinput = "";
	String classname = "[";
    	String methodname ="";
    	String modifier ="";
	String s1="";
    	String resultstring="";
    
	Map<String, String> allclasses = new HashMap<String, String>();
	Map<String, String> allAttributes = new HashMap<String, String>();

	
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        List<BodyDeclaration<?>> classList = n.getMembers() ;
         if(n.isInterface())
        {
        	classname = "interface " ;
        
        }else{
        	classname = "class " ;
        	notinterface = true;
        }
        resul += classname + n.getName(); 
	
	 List<ClassOrInterfaceType> allExtends = n.getExtendedTypes();
        for (ClassOrInterfaceType coi : allExtends)
		{
			
			resul += " extends " + coi.getName();
		}
        
        List<ClassOrInterfaceType> implementList = n.getImplementedTypes();
		for (ClassOrInterfaceType ci : implementList)
		{
			if (morethanoneinterface)
			{
				resul+= "," +ci.getName();
			}
			else{
			resul += " implements " + ci.getName();
			morethanoneinterface = true;
			}
		}
        
        resul+= " {"; 
        
	for (BodyDeclaration bd : classList )
        {
        	if(bd instanceof FieldDeclaration)
        	{
        		FieldDeclaration attribute = (FieldDeclaration) bd ;
        		
        		if (attribute.getModifiers().toString().contains("PRIVATE"))
        			modifier = "-";
        		else if (attribute.getModifiers().toString().contains("PUBLIC"))
        			modifier = "+";
        		else{
        			modifier = "";
        			skipattributes = true;
        		}
			
			if (!skipattributes)// skip protected and package attributes
        		{
        		List<VariableDeclarator> attributeList = attribute.getVariables();
        				for (VariableDeclarator var : attributeList)
        				{       					
        					//check if primitive
        					//System.out.println(var.getType());
        					if (var.getType() instanceof ReferenceType)
        					{
        						//check for array
        						if (var.getType().toString().contains("[]"))
        						{
        							resul += "\n" + modifier ;
        							resul += var.getName();
            						resul += ":";
            						resul += var.getType();
        						}
        						else{
        						
        							nonPrimitive = var.getType().toString();
        							
        							if (nonPrimitive.contains("Collection"))
        							{
        							String	s = nonPrimitive.substring(nonPrimitive.indexOf("<")+1);
              		    			String	 f =s.replace(">", "*");
              		    			attributeSet.add(f);
              		    			
        							}
        							else if(nonPrimitive.contains("String")){	
        								resul += "\n" + modifier ;
                						resul += var.getName();
                						resul += ":";
                						resul += var.getType();
                						//attributeSet.add(var.getType().toString());
        							}
        							else{
        							attributeSet.add(var.getType().toString());
        							//System.out.println(var.getType().toString());
        							//nonPrimitive += var.getName();  
        							}
        						}       									
        					}else{
        						resul += "\n" + modifier ;
        						resul += var.getName();
        						resul += ":";
        						resul += var.getType();
        					   
        					//System.out.println(resul);
        					}   	
        					
        					modifierAttribute.put(var.getName().toString() ,modifier);
        					//System.out.println(modifierAttribute);
        				}//end of variable Declarator
        				skipattributes = false;
        		}//end of skipattributes
        	}// end of field Declaration
        	
		if (bd instanceof MethodDeclaration)
        	{
        		MethodDeclaration method = (MethodDeclaration) bd;
        		
        		
        		if (method.getModifiers().toString().contains("PUBLIC"))
        			modifier = "+";
        		else{
        			modifier = "";
        			skipmethod = true;
        		}
        	
        		methodname= method.getName().toString();
        		//System.out.println(methodname);
        		
        	
        		if(!skipmethod)
        		{
        			for(Map.Entry pairs : modifierAttribute.entrySet())
        			{
        				System.out.println(pairs.getKey().toString());
        				if  ( methodname.contains("get") )
        					methodname = methodname.replace("get", "");
        				else if (methodname.contains("set"))
        					methodname = methodname.replace("set", "");
        			
        				if (methodname.compareToIgnoreCase(pairs.getValue().toString())==0)
        				{      				
        					gettersetter = true;	
        				}
        				
        				if (pairs.getKey().toString().compareToIgnoreCase(methodname)==0)
						{
        					String changemodifier= pairs.getValue().toString() ;
        					pairs.setValue(changemodifier.replace("-", "+"));
						//System.out.println(pairs.getValue());
						}
						attributegrammer += pairs.getValue().toString();
						//System.out.println(resul);
        			}	
        			//System.out.println(modifierAttribute);
        			resul += attributegrammer;
        			attributegrammer ="";
        			
        			if (!gettersetter)
        			{
                		methodgrammer += "\n" + modifier + method.getName() + "(";
                		List<Parameter> methodargument = method.getParameters();
                		for (Parameter argmnt : methodargument)
                		{
                			methodgrammer += argmnt.getName() + ":" ;
                			if (argmnt.getType() instanceof ReferenceType)
        					{
        						//check for array
        						if (argmnt.getType().toString().contains("[]"))
        						{	
            						methodgrammer += argmnt.getType();
        						}
        						else{    						     
        							nonPrimitive = argmnt.getType().toString();     							
        								if (nonPrimitive.contains("Collection"))
        								{
        									String	s = nonPrimitive.substring(nonPrimitive.indexOf("<")+1);
        									String	 f =s.replace(">", "*");
        									attributeSet.add(f);
        								}
        								else 
        								if(nonPrimitive.contains("String")){	
        											methodgrammer += argmnt.getType();   									
        								}
        								else{    									
        									s1 = argmnt.getType().toString() + "uses";
        									attributeSet.add(s1);
        									methodgrammer += argmnt.getType();
        								}
        						}       									
        					}else{   						
        						methodgrammer += argmnt.getType();
             					}   	
                			
        					modifierAttribute.put(argmnt.getName().toString() ,modifier);
                		}//end of method argumnet
                		methodgrammer += ")" + ":" + method.getType();
        			}//end of getter setter
        			gettersetter = false;
        		
        		}//end of skipmethod
        		skipmethod = false;
        		resul += methodgrammer;
        		methodgrammer = "";
        	}//end of method declaration
        	
        	
             
       
        }//end of Body Declaration
        resul += "\n}\n";
        if (notinterface)
        {
        	allClasses.put(n.getName().toString(),resul);
        	//notinterface=false;
        }
        else
        {
        	allInterfaces.put(n.getName().toString(),resul);
        	//System.out.println(allInterfaces);
        }
        allAttributes.put(n.getName().toString(),attributeSet);
        resul = "";
        drawDependency();
        printGrammer();
        super.visit(n, arg);
     }
	
	public void printGrammer(){
		
		for(Map.Entry pairs : allInterfaces.entrySet())
		{
			finalG += pairs.getValue().toString(); 
	    	}
		for(Map.Entry pairs : allClasses.entrySet())
		{
			finalG += pairs.getValue().toString(); 
	    
	    	}
	}

	public void drawDependency()
	{
		
        String ckcoll="";
		
		Boolean flag=true;
        	
        	for(String cname : allClasses.keySet())	
        	{
        		
        		
        		for   (String aname : attributeSet)
        		{
        			
        			ckcoll=cname+aname;
        			
        			if (checkclass.contains(aname))
        			{
        				continue;
					//System.out.println("do nothing:");
        			}
        			else
        			{
        				//Show multiplicity
        				if (aname.contains("*"))
        				{
        					String remove = aname.replace("*", "");
        					drawline = cname + "\"" + "1"  + "\"" + " -- "  + "\"" + "*"  + "\"" + remove ;
            				resultString += drawline + "\n";
            				checkclass += cname+remove;
        				}
        				else if (aname.contains("uses"))
        				{
        					String remove = aname.replace("uses", "");
        					drawline = cname + " ..> "   + remove ;
            				resultString += drawline + "\n";
            				checkclass += cname+remove;
        				}
        				else
        				{
        				drawline = cname + "\"" + "1"  + "\"" + " -- "  + "\"" + "1"  + "\"" + aname ;
        				resultString += drawline + "\n";
        				checkclass += cname+aname;
        				}
        			}
    			
					
        		}
        	}
        	
        	
        
		
	
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
