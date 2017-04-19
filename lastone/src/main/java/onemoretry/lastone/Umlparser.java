package onemoretry.lastone;

public class Umlparser {

		public static void main(String[] args)throws Exception{
		
			
		final String infile = args[0];
		final String outfile = args[1];
		
		ParseClass pc = new ParseClass();
		//Check if class diagram
		pc.compile(infile,outfile);
		//else if sequence diagram
		//ParseSequence ps = new ParseSequence
			// ps.compile(infile,outfile)
	}
}
