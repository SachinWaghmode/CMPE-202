import java.util.ArrayList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

public aspect aspecttest {

	ArrayList<String> methodsCalledInMainClass = new ArrayList<String>(); // store main method calls
	ArrayList<String> executionOfAllMethods = new ArrayList<String>(); //store execution of methods
	ArrayList<String> callToAllMethods = new ArrayList<String>(); //store all method calls
	ArrayList<String> activate = new ArrayList<String>(); //store activation bar for participants
	
	static String grammar = ""; //store intermediate grammer
	static String prevCClass= "";
	static String currentAciveBar = null;
	static String newAciveBar = null;
	static String activatebar = null;
	static String deactivatebar = null;
	static String allMethodName = null;
	static String allClassName = null;

	//pointcut to get method calls in main class
	pointcut methodsCalledInMainClass():
		call(* *.*(..)) && !within(aspecttest) && !cflow(execution(*.new(..)));
	
	//pointcut to get method calls in all java classes	
	pointcut callToAllMethods():
		call(* *.*(..)) && !within(aspecttest) && !cflow(execution(*.new(..)));
	
	//pointcut to get execution of all methods
	pointcut executionOfAllMethods():
		execution(* *.*(..)) && !within(aspecttest) && !cflow(execution(*.new(..)));
	
	//pointcut to get execution of main method
	pointcut executionOfMainMethod():
		execution(* *.main(..)) && !within(aspecttest) ;
	
	
	//method to get intermediate grammer 
	private void generateIntermediateGrammer() {
		
		GenerateSequenceDiagram gs = new GenerateSequenceDiagram();
		int InMainExecution = 0;
		int InNestedMethodCalls = 0; 
		grammar += "activate Main" + "\n";
		for(String exeMethodSign: executionOfAllMethods)
		{
			
			InMainExecution=0;
			InNestedMethodCalls = 0; 
			
			String[] temp = exeMethodSign.split("\\.");
			String eClassName = temp[0]; //get execution class name
			String eMethodName = temp[1]; //get execution method name
			//boolean setDeactivation= false;
			
			if (!eClassName.equalsIgnoreCase("Main"))
			{
				for(String callMethodSign: methodsCalledInMainClass)
				{
					
				//method calling class
					if (InMainExecution == 1) 
					{
						grammar += "deactivate " + currentAciveBar + "\n";
					}
					else
					{
						String[] tempCall = callMethodSign.split("\\.");
						String cClassName = tempCall[0]; //get calling class name
						String cMethodName = tempCall[1]; //get called method name
				
						if((eMethodName.equals(cMethodName) && eClassName.equals(cClassName)==false)) 
						{
					
							currentAciveBar = "Main";
							grammar += "\n"+ currentAciveBar + " -> " + cClassName + ":" + cMethodName + "()\n";
							currentAciveBar = cClassName;
							prevCClass = callMethodSign;
							InMainExecution = 1;
							grammar += "activate " + currentAciveBar + "\n";
							newAciveBar = currentAciveBar;
						}
						
					}
				}
				if(InMainExecution ==0)
				{
				//Methods which are not called in main class
				
					InNestedMethodCalls = 0;
				
					for(String allMethodSign : callToAllMethods)
					{
						String[] tempAll = allMethodSign.split("\\.");
						allClassName = tempAll[0];
						allMethodName = tempAll[1];
						if(allMethodName.equals(eMethodName) && eClassName.equals(allClassName) && InNestedMethodCalls == 0)
						{
							if(newAciveBar.equals(currentAciveBar))
							{
								grammar += newAciveBar + " -> " + newAciveBar + ":" + eMethodName + "()\n";
								activatebar = newAciveBar;
								grammar += "activate " + activatebar + "\n";
								activate.add(activatebar);
								
							}
							else
							{
								grammar += newAciveBar + " -> " + currentAciveBar + ":" + eMethodName + "()\n";
								activatebar = currentAciveBar;
								grammar += "activate " + activatebar + "\n";
								activate.add(activatebar);
								grammar += "deactivate " + currentAciveBar + "\n";
							}
							InNestedMethodCalls = 1;
							
							
						}
						else if (allMethodName.equals(eMethodName) && eClassName.equals(allClassName)==false && InNestedMethodCalls == 0)
						{
							grammar += currentAciveBar + " -> " + eClassName + ":" + eMethodName + "()\n";
							newAciveBar = eClassName;
							InNestedMethodCalls = 1;
							activatebar = eClassName;
							grammar += "activate " + activatebar + "\n";
							activate.add(activatebar);
						}
					}
				}
				else
				{
					methodsCalledInMainClass.remove(prevCClass);
				}
				
				if (InNestedMethodCalls == 0 && InMainExecution == 1){
					
					for (String bar : activate)
					{
						grammar += "deactivate " + bar + "\n";
					}
					
				}
			}	
	   }
		grammar += "deactivate Main" + "\n";
		
		gs.generatePlantUML(grammar, "/Users/sachinwaghmode/eclipse/test/");
    }
	
	before():methodsCalledInMainClass(){
		
		if(thisJoinPoint.getSourceLocation().toString().contains("Main")){
			StringBuffer sb= new StringBuffer();
			sb.append(thisJoinPoint.getSignature().getDeclaringTypeName()).append(".").append(thisJoinPoint.getSignature().getName());
			methodsCalledInMainClass.add(sb.toString());
		}
	}
	
	before():callToAllMethods(){
		StringBuffer sb= new StringBuffer();
		sb.append(thisJoinPoint.getSignature().getDeclaringTypeName()).append(".").append(thisJoinPoint.getSignature().getName());
		callToAllMethods.add(sb.toString());
	}
	
	before():executionOfAllMethods(){
			StringBuffer sb= new StringBuffer();
			sb.append(thisJoinPoint.getSignature().getDeclaringTypeName()).append(".").append(thisJoinPoint.getSignature().getName());
			executionOfAllMethods.add(sb.toString());
	}
	
	after():executionOfMainMethod(){
		
			generateIntermediateGrammer();	
		}
	
}
