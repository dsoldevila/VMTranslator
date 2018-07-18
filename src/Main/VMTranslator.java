package Main;


public class VMTranslator {
	private static byte type;
	private static char a;
	
	public static void main (String[] arguments) {
		String input_file_name = arguments[0];
		System.out.println(input_file_name);
		String output_file_name = input_file_name.substring(0, input_file_name.indexOf("."));
		output_file_name = output_file_name + ".asm";
		Parser Par = new Parser(arguments[0]);
		CodeWriter Writer = new CodeWriter(output_file_name);
		a = '0';
		while(Par.hasMoreCommands()) {
			Par.advance();
			if((type = Par.commandType())!=-1) {
				System.out.println("iter: "+a);
				System.out.println(type);
				System.out.println(Par.arg1());
				if(type==Parser.C_ARITHMETIC)
					Writer.writeArithmetic(Par.arg1());
				if(type==Parser.C_POP || type==Parser.C_PUSH)
					Writer.writePushPop(type, Par.arg1(), Par.arg2());
				a++;
			}
		}
		//CodeWriter CodeWriter = new CodeWriter("write.txt");
	}
}
