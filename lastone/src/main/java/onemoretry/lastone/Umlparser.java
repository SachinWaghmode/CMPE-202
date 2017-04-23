package onemoretry.lastone;

public class Umlparser {

		public static void main(String[] args)throws Exception{
		
			
		final String infile = args[0];
		final String outfile = args[1];
		final String diagram = args[2];
		
		//Check if class diagram
		if (diagram.equalsIgnoreCase("class"))
		{
			ParseClass  pc = new ParseClass();
			pc.compile(infile,outfile);
		}
		else if (diagram.equalsIgnoreCase("sequence"))
		{
			ParseSequence  ps = new ParseSequence();
			ps.compile(infile,outfile);
		}
	}
}
