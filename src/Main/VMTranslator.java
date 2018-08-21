package Main;

import java.io.File;

public class VMTranslator {
	private static byte type; //current command type
	private static int i; //iteration
	
	private String in_file_name = null;
	private String out_file_name = null;
	private String dir_name = null;
	private String[] files = null;
	
	private boolean is_directory = false;
	private boolean is_file = false;
	
	public static void main (String[] arguments) {
		
		VMTranslator Translator = new VMTranslator();
		Translator.Translate(arguments[0]);
		

	}
	
	public void Translate(String file_name) {
		
		File input = new File(file_name);
		
		if(input.isFile() && file_name.contains(".vm")) { //if input is a single .vm file
			this.in_file_name = input.getName();
			System.out.println(this.in_file_name); //
			this.out_file_name = this.in_file_name.substring(0, this.in_file_name.indexOf("."));
			this.out_file_name = this.out_file_name + ".asm";
			String[] files = {this.in_file_name};
			this.files = files;
			this.is_file = true;
			
		}else if(input.isDirectory()) { //if input is a directory containing multiple files
			this.dir_name = file_name;
			System.out.println(this.in_file_name); //
			this.out_file_name = this.dir_name + ".asm";
			this.files = getFiles(file_name);
			this.is_directory = true;
			
		}else { //Not a file nor a directory ??
			System.out.println("INVALID input"); //
		}
		
		if(this.is_file || this.is_directory) {
			
		
			Parser Par = new Parser(this.in_file_name); 
			CodeWriter Writer = new CodeWriter(this.out_file_name);
			
			int j = this.files.length;
			do {
				//Par.nextFile(file_name)
				//Writer.setFileName(name);
				//Translate file
				j--;
				
			
				i = 0;
				while(Par.hasMoreCommands()) {
					Par.advance();
					if((type = Par.commandType())!=Parser.C_NULL) {
						System.out.println("iter: "+String.valueOf(i));
						System.out.println(type);
						if(type!=Parser.C_RETURN) {
							System.out.println(Par.arg1());
						}
						switch(Par.commandType()) {
							case Parser.C_ARITHMETIC:
								Writer.writeArithmetic(Par.arg1());
								break;
							case Parser.C_LABEL:
								Writer.writeLabel(Par.arg1());
								break;
							case Parser.C_GOTO:
								Writer.writeGoto(Par.arg1());
								break;
							case Parser.C_IF:
								Writer.writeIf(Par.arg1());
								break;
							case Parser.C_PUSH:
							case Parser.C_POP:
								System.out.println(Par.arg2());
								Writer.writePushPop(type, Par.arg1(), Par.arg2());
								break;
							case Parser.C_FUNCTION:
								System.out.println(Par.arg2());
								Writer.writeFunction(Par.arg1(), Par.arg2());
								break;
							case Parser.C_CALL:
								Writer.writeCall(Par.arg1(), Par.arg2());
								break;
							case Parser.C_RETURN:
								Writer.writeReturn();
								break;
					
						}
						i++;
					}
		
				}
			}while(j>0);
		}
	}
	
	public String[] getFiles(String dir_name) {
		
		File directory = new File(dir_name);
		File[] files = directory.listFiles();
		boolean[] is_valid_file = new boolean[files.length]; //false by default (language spec)
		int valid_files_counter = 0;
		
		for(int i = 0; i<files.length; i++) { //check if elements in directory are actually .vm files
			if(files[i].isFile() && files[i].getName().contains(".vm")) {
				is_valid_file[i] = true;
			}
			valid_files_counter++;
		}
		
		String[] file_names = new String[valid_files_counter];
		
		int i = 0;
		int j = 0;
		while(i<valid_files_counter) {
			if(is_valid_file[j]) {
				file_names[i] = files[j].getName();
				i++;
			}
			j++;
		}
		
		return file_names;
	}
}
