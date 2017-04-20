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
        		
        		List<VariableDeclarator> attributeList = attribute.getVariables();
        				for (VariableDeclarator var : attributeList)
        				{       					
        					if (var.getType() instanceof ReferenceType)//check if primitive
        					{
        						//check for array
        						if (var.getType().toString().contains("[]"))
        						{
        							if(!skipattributes)
        								attributegrammer += "\n" + modifier + var.getName() + ":" + var.getType();
        						}
        						else{
        							nonPrimitive = var.getType().toString();
        							
        							if (nonPrimitive.contains("Collection"))
        							{
        								String	s = nonPrimitive.substring(nonPrimitive.indexOf("<")+1);
        								String	 f =s.replace(">", "*");
        								attributeSet.add(f);
        								nonPrimitive = "";
        							}
        							else if(nonPrimitive.contains("String"))
        							{
        								if (!skipattributes)
        								{
                						attributegrammer += "\n" + modifier + var.getName() + ":" + var.getType();	
        								}
        							}
        							else{
        							attributeSet.add(var.getType().toString()); 
        							}
        						}       									
        					}else
        					{ 
        						if(!skipattributes)
        							attributegrammer += "\n" + modifier + var.getName() + ":" + var.getType();
        					}   	
        					
        					if (!skipattributes)//Skip protected and package attributes
        					{
        					modifierAttribute.put(var.getName().toString() , attributegrammer);
        					}
        					skipattributes = false;
        					attributegrammer ="";
        				}//end of variable Declarator
        				
        				
        				
        				//System.out.println(allAttributes);
        	}// end of field Declaration
        	
		if (bd instanceof MethodDeclaration)
        	{
        		MethodDeclaration method = (MethodDeclaration) bd;
        		
			List<Node> methodbody = method.getChildNodes();
        		for (Node node: methodbody)
        		{
        			
        			for(String attributename : attributeSet)
        			{
        				if (node.toString().contains(attributename))
        				{
        					//check if method body has only getter setter
        				}
        			}
        		}
        		
        		if (method.getModifiers().toString().contains("PUBLIC"))
        			modifier = "+";
        		else{
        			modifier = "";
        			skipmethod = true;
        		}
        	
        		methodname= method.getName().toString();
        		
        		
        	
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
        	
        	     	if(bd instanceof ConstructorDeclaration)
        	{
        		ConstructorDeclaration constructor = (ConstructorDeclaration) bd;
        		if(constructor.getModifiers().toString().contains("PUBLIC"))
        			modifier = "+";
        		
        		constructorgrammer += "\n" + modifier + constructor.getName() + "(";
        		List<Parameter> constructorargument = constructor.getParameters();
        		for (Parameter argmnt : constructorargument)
        		{
        			constructorgrammer += argmnt.getName() + ":";
        			if (argmnt.getType() instanceof ReferenceType)
					{
						//check for array
						if (argmnt.getType().toString().contains("[]"))
						{	
    						constructorgrammer += argmnt.getType();
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
									constructorgrammer += argmnt.getType();
								}
								else{
									constructorgrammer += argmnt.getType();
									s1 = argmnt.getType().toString() + "uses";
									attributeSet.add(s1);	
								}
						}       									
					}else{  
						constructorgrammer += argmnt.getType();
     					}   	
        		}//end of constructor argumnet
        		constructorgrammer += ")";
        	}//end of constructor declaration
        	
             
       
        }//end of Body Declaration
        allAttributes.put(n.getName().toString(),attributeSet);
         //System.out.println(allAttributes);
         //attributeSet.clear();
         //System.out.println(modifierAttribute);
         for(Map.Entry pairs : modifierAttribute.entrySet())
			{
				attributegrammer += pairs.getValue().toString();	
			}	
     		resul += attributegrammer + constructorgrammer + addmethodgrammer + "\n}\n";
			attributegrammer="";
			
			addmethodgrammer = "";
    		
    		constructorgrammer = "";
        
        if (n.isInterface())
        {
        	allInterfaces.put(n.getName().toString(),resul);
        	allInterfaceName.add(n.getName().toString());
        }
        else
        {
        	classname = n.getName().toString();
        	allClasses.put(classname,resul);
        	
        	
        }
        
        resul = "";
       
       for (String inter : allInterfaceName)
       {
    	   interfacegrammer += ":" + inter;
       }
     
      
              super.visit(n, arg);
     }//end of visit
	
	

	public void drawDependency()
	{
		
        
		String ckcoll="";
		Boolean flag=false;
		Boolean usesflag=false;
		String removedependency = "";
		String adddependency = "";
		String removeuses = "";
		String adduses ="";
    
		for (Map.Entry allattri : allAttributes.entrySet())
		{
			String cname = allattri.getKey().toString();
			String aname = allattri.getValue().toString();
			
			//System.out.println("insider :" +aname);
			//drawline = cname ;
			//for(String s: allInterfaces.keySet())
			//{
			String atrriarray[] = aname.split(", ");
			for (int i = 0; i < atrriarray.length; i++)
			{
				aname = atrriarray[i];
				aname = aname.replace("[", "");
				aname = aname.replace("]", "");
				
				String compare = aname.replace("*","");
				compare = aname.replace("uses", "");
				if (aname.contains("*") || aname.contains("uses"))
				{
				 removedependency = cname + "--" + compare;
				 adddependency = compare+ "--" + cname;
				 removeuses = cname+ ".." + compare;
				 adduses = compare+ ".." + cname;
				}
				else
				{
					System.out.println(aname);
					removedependency = cname + "--" + aname;
					adddependency = aname + "--" + cname;
				}
				//System.out.println("set :"+dependencySet);
				for (String sd : dependencySet)
				{
					if (sd.equalsIgnoreCase(removedependency) || sd.equalsIgnoreCase(adddependency))
					{
						flag = true;
					}
					if (sd.equalsIgnoreCase(removeuses) || sd.equalsIgnoreCase(adduses))
					{
						usesflag = true;
					}
				}
				if (flag )
				{
					flag=false;
					//continue;
				}
				else
				{
				 
					if (aname.contains("*"))
					{
						String remove = aname.replace("*", "");
			
					
					drawline = "class " + cname + "\"" + "1"  + "\"" + " -- "  + "\"" + "*"  + "\"" + "class " + remove ;
					resultString += drawline + "\n";
					checkclass = cname+ "--" +remove;
					dependencySet.add(checkclass);
					for(String s: allInterfaces.keySet())
					{
					
					   if (remove.equalsIgnoreCase(s))
					   {
						   drawline = cname + "\"" + "uses"  + "\""  + " ..> "   + remove ;
							//drawline = cname  + " ..> "   + remove ;
							resultString += drawline + "\n";
							checkclass = cname+".."+remove;
							dependencySet.add(checkclass);
					   }
					}
					}
					else  if(!aname.isEmpty() && !aname.contains("uses"))
					{
					drawline = "class " + cname + "\"" + "1"  + "\"" + " -- "  + "\"" + "1"  + "\"" + "class " + aname ;
					resultString += drawline + "\n";
					checkclass = cname+"--"+aname;
					dependencySet.add(checkclass);
					}
				}
				if (usesflag)
				{
					usesflag=false;
					//continue;
				}
				else
				{
					if (aname.contains("uses"))
					
					{
					String remove = aname.replace("uses", "");
					
					for(String s: allInterfaces.keySet())
					{
						
					   if (remove.equalsIgnoreCase(s))
					   {
						   for (String c: allClasses.keySet())
						   {
							   if (c.equalsIgnoreCase(cname))
							   {
						drawline = cname + "\"" + "uses"  + "\""  + " ..> "   + remove ;
						//drawline = cname  + " ..> "   + remove ;
						resultString += drawline + "\n";
						checkclass = cname+".."+remove;
						dependencySet.add(checkclass);
							   }
						   }
					   }
					}
				}
				
				}
			
		}	
			
		}	
	
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
	
    
    
	public void compile(String inputfile, String outputfile ) {
		// parse input java file
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
					
					e.printStackTrace();
				}
				//new ParseClass().visit(javaClass, null) ;
				//System.out.println("next file :");
				visit(javaClass, null) ;
			}
			
			drawDependency(classname);// add dependency/association grammer
			
			printGrammer();//Print Class grammer 
			
			generatePlantUML(finalG, outfile);//generate diagram
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
	

}
