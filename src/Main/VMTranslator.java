package Main;


public class VMTranslator {
	public static void main (String[] arguments) {
		Parser Parser = new Parser(arguments[0]);
		while(Parser.hasMoreCommands()) {
			Parser.advance();
			byte a = Parser.commandType();
			System.out.println(a);
			System.out.println(Parser.arg1());
			System.out.println(Parser.arg2());
		}
		//CodeWriter CodeWriter = new CodeWriter("write.txt");
	}
}
