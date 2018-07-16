package Main;


public class VMTranslator {
	private static byte type;
	private static char a;
	
	public static void main (String[] arguments) {
		Parser Parser = new Parser(arguments[0]);
		CodeWriter CodeWriter = new CodeWriter("write.txt");
		a = '0';
		while(Parser.hasMoreCommands()) {
			Parser.advance();
			if((type = Parser.commandType())!=-1) {
				System.out.println("iter: "+a);
				System.out.println(type);
				System.out.println(Parser.arg1());
				//System.out.println(Parser.arg2());
				CodeWriter.writeArithmetic(Parser.arg1());
				
				a++;
			}
		}
		//CodeWriter CodeWriter = new CodeWriter("write.txt");
	}
}
