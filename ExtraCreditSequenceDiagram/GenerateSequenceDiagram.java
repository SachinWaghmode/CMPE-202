import java.io.FileOutputStream;

import net.sourceforge.plantuml.SourceStringReader;

public class GenerateSequenceDiagram {

	public void generatePlantUML(String intermediateCode, String outfile){
		
		String startUML = "@startuml  \n";
		String endUML = "@enduml";
		String outpath = outfile + "output.png";
	    String resul= ""; 
		resul = startUML + intermediateCode + endUML;
		
		System.out.println(resul);
	
		SourceStringReader s = new SourceStringReader(resul);
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