package Main;

import java.io.File;

public class VMTranslator {
	private byte command_type; //current command type
	private int command_counter = 0; //iteration
	
	private String out_file_name = null;
	private String dir_name = null;
	private String[] file_full_paths = null;
	private String[] file_names = null;
	
	private boolean is_directory = false;
	private boolean is_file = false;
	
	public static void main (String[] arguments) {
		
		VMTranslator Translator = new VMTranslator();
		Translator.Translate(arguments[0]);
		

	}
	
	public void Translate(String file_name) {
		
		File input = new File(file_name);
		
		if(input.isFile() && file_name.contains(".vm")) { //if input is a single .vm file
			String[] path = {input.getAbsolutePath()};
			String[] name = {input.getName()};
			
			System.out.println(name[0]); //
			
			this.out_file_name = name[0].substring(0, file_name.indexOf("."));
			this.out_file_name = this.out_file_name + ".asm";

			this.file_full_paths = path;
			this.file_names = name;
			this.is_file = true;
			
		}else if(input.isDirectory()) { //if input is a directory containing multiple files
			this.dir_name = file_name;
			
			System.out.println(file_name); //
			
			this.out_file_name = this.dir_name + ".asm";
			getFiles(file_name); //updates this.full_file_paths and this.file_names
			this.is_directory = true;
			
		}else { //Not a file nor a directory ??
			System.out.println("INVALID input"); //
		}
		
		if(this.is_file || this.is_directory) {
			
			Parser Par = new Parser(this.file_full_paths[0]); 
			CodeWriter Writer = new CodeWriter(this.out_file_name);
			this.writeFile(Par, Writer);
			
			int j = 1;
			int len = this.file_full_paths.length;
			System.out.println("NUMBER OF FILES: "+len);
			while(j<len) {    //if there is more than one file
				Par.close();
				Par = new Parser(this.file_full_paths[j]);
				Writer.setFileName(this.file_names[j]);
				
				this.writeFile(Par, Writer);		
				j++;
			}
			Par.close();
			Writer.close();
		}
		
	}
	
	public void writeFile(Parser Par, CodeWriter Writer) {
		while(Par.hasMoreCommands()) {
			Par.advance();
			if((command_type = Par.commandType())!=Parser.C_NULL) {
				System.out.println("iter: "+String.valueOf(command_counter));
				System.out.print(command_type+" ");
				if(command_type!=Parser.C_RETURN) {
					System.out.print(Par.arg1()+" ");
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
						System.out.print(Par.arg2());
						Writer.writePushPop(command_type, Par.arg1(), Par.arg2());
						break;
					case Parser.C_FUNCTION:
						System.out.print(Par.arg2());
						Writer.writeFunction(Par.arg1(), Par.arg2());
						break;
					case Parser.C_CALL:
						Writer.writeCall(Par.arg1(), Par.arg2());
						break;
					case Parser.C_RETURN:
						Writer.writeReturn();
						break;
			
				}
				System.out.println();
				command_counter++;
			}
		}
	}
	
	public void getFiles(String dir_name) {
		
		File directory = new File(dir_name);
		File[] files = directory.listFiles();
		boolean[] is_valid_file = new boolean[files.length]; //false by default (language spec)
		int valid_files_counter = 0;
		
		for(int i = 0; i<files.length; i++) { //check if elements in directory are actually .vm files
			if(files[i].isFile() && files[i].getName().contains(".vm")) {
				is_valid_file[i] = true;
				valid_files_counter++;
			}
		}
		
		String[] file_paths = new String[valid_files_counter];
		String[] file_names = new String[valid_files_counter];
		
		int i = 0;
		int j = 0;
		while(j<files.length) {
			if(is_valid_file[j]) {
				file_paths[i] = files[j].getAbsolutePath();
				file_names[i] = files[j].getName();
				i++;
			}
			j++;
		}
		this.file_full_paths = file_paths;
		this.file_names = file_names;

	}
}
