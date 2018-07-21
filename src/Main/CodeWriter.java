package Main;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


public class CodeWriter {
	private BufferedWriter file = null;
	
	private String file_name; //without extension
	
	private static final String SP = "@SP";
	
	/* ARITHMETIC */
	private static final String[] add = {"//add",SP,"AM=M-1","D=M","A=A-1","M=M+D"};
	private static final String[] sub = {"//sub",SP,"AM=M-1","D=M","A=A-1","M=M-D"};
	private static final String[] neg = {"//neg", SP, "A=M-1", "M=-M"};
	
	//TODO
	//Not implemented yet
	private static final String[] eq = {"//eq\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n"};
	private static final String[] gt = {"//gt\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n"};
	private static final String[] lt = {"//gt\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n"};
	
	//Logical
	private static final String[] and = {"//and", SP, "AM=M-1", "D=M", "A=A-1", "M=M&D"};
	private static final String[] or = {"//and", SP, "AM=M-1", "D=M", "A=A-1", "M=M|D"};
	private static final String[] not = {"//not", SP, "A=M-1", "M=!M"};
	
	
	/* SEGMENTS */
	private static final String ARGUMENT = "@ARG";
	private static final String THIS = "@THIS";
	private static final String THAT = "@THAT";
	private static final String LOCAL = "@LCL";
	//temp == 5+i
	//Static == Filename
	//constant == nothing
	
	
	
	/**
	 * Opens the output file and gets ready to write into it
	 */
	public CodeWriter(String file_name) {
		try {
			this.file = new BufferedWriter(new FileWriter(file_name));
		} catch (IOException e) {
			System.out.println("ERROR: Output can't be used or created");
		}		
		this.file_name = file_name.substring(0, file_name.indexOf("."));
		
	}
	
	
	/**
	 * Writes to the output file the assembly code that implements
	 * the given command
	 * @param command
	 */
	public void writeArithmetic(String command) {
		String[] temp = null;
		switch(command) {
			case "add":
				temp = add;
				break;
			case "sub":
				temp = sub;
				break;
			case "neg":
				temp = neg;
				break;
			case "eq":
				temp = eq;
				break;
			case "gt":
				temp = gt;
				break;
			case "lt":
				temp = lt;
				break;
			case "and":
				temp = and;
				break;
			case "or":
				temp = or;
				break;
			case "not":
				temp = not;
				break;
		}
		try {
			for(int i=0; i<temp.length; i++) {
				this.file.write(temp[i]);
				this.file.newLine();
			}
			this.file.flush();
			
		} catch (IOException e) {
			System.out.println("ERROR: Can't output command");
		}
		
	}
	
	/**
	 * Writes to the output file the assembly code that implements
	 * the push or pop command
	 * @param type C_PUSH or C_POP
	 * @param segment
	 * @param index
	 */
	public void writePushPop(byte type, String segment, String index) {
		String[] temp = null;
		boolean has_segment = true;
		boolean is_constant = false;
		
		switch(segment) {
			case "argument":
				segment=ARGUMENT;
				index = "@"+index;
				break;
			case "local":
				segment=LOCAL;
				index = "@"+index;
				break;
			case "this":
				segment=THIS;
				index = "@"+index;
				break;
			case "that":
				segment=THAT;
				index = "@"+index;
				break;
			case "static":
				index = "@"+this.file_name+"."+index;
				has_segment = false;
				break;
			case "constant":
				index = "@"+index;
				is_constant = true;
				has_segment = false;
				break;
			case "temp":
				index ="@"+String.valueOf(Integer.parseInt(index)+5);
				has_segment = false;
				break;
		}
		if(type==Parser.C_PUSH) {
			if(has_segment) { //arg, local, this, that
				String[] t = {"//Push", segment, "D=A", index, "A=D+A", "D=M", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}else if(is_constant){ //constant
				String[] t = {"//Push", index, "D=A", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}else { //static or temp
				String[] t = {"//Push", index, "D=M", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}
			
		}else if(type==Parser.C_POP) {
			if(has_segment) {
				String[] t = {"//Pop", segment, "D=A", index, "D=A+D", SP, "A=M", "M=D", "A=A-1", "D=M", SP, "A=M",	"A=M", "M=D", SP, "M=M-1"};
				temp = t.clone();
				}
			}else {
				String[] t = {"//Pop", SP, "AM=M-1", "D=M", index, "M=D"};
				temp = t.clone();
			}
		try {
			for(int i=0; i<temp.length; i++) {
				this.file.write(temp[i]);
				this.file.newLine();
			}
			this.file.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Couldn't write on output file");
		}
}
	
	public void close() {
		try {
			this.file.close();
		} catch (IOException e) {
			System.out.println("ERROR: Error at closing the output file");
		}
	}
}


