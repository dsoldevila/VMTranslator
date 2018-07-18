package Main;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CodeWriter {
	private FileOutputStream file = null;
	
	private String file_name; //without extension
	
	private static final String SP = "@SP\n";
	
	/* ARITHMETIC */
	private static final byte[] add = "//add\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M+D\n".getBytes();
	private static final byte[] sub = "//sub\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n".getBytes();
	private static final byte[] neg = "//neg\n@SP\nA=M-1\nM=-M\n".getBytes();
	
	//TODO
	//Not implemented yet
	private static final byte[] eq = "//eq\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n".getBytes();
	private static final byte[] gt = "//gt\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n".getBytes();
	private static final byte[] lt = "//gt\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D\n".getBytes();
	
	private static final byte[] and = "//and\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M&D\n".getBytes();
	private static final byte[] or = "//or\n@SP\nAM=M-1\nD=M\nA=A-1\nM=M|D\n".getBytes();
	private static final byte[] not = "//not\n@SP\nA=M-1\nM=!M\n".getBytes();
	
	
	/* SEGMENTS */
	private static final String ARGUMENT = "@ARG\n";
	private static final String THIS = "@THIS\n";
	private static final String THAT = "@THAT\n";
	private static final String LOCAL = "@LCL\n";
	//temp == 5+i
	//Static == Filename
	//constant == nothing
	
	
	
	/**
	 * Opens the output file and gets ready to write into it
	 */
	public CodeWriter(String file_name) {
		try {
			this.file = new FileOutputStream(file_name);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Output can't be used or created");
		}		
		this.file_name = file_name.substring(0, file_name.indexOf(" ")-1);
		
	}
	
	
	/**
	 * Writes to the output file the assembly code that implements
	 * the given command
	 * @param command
	 */
	public void writeArithmetic(String command) {
		byte[] temp = null;
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
			this.file.write(temp);
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
		String temp = null;
		boolean has_segment = true;
		boolean is_constant = false;
		
		switch(segment) {
			case "argument":
				segment=ARGUMENT;
				index = "@"+index+"\n";
				break;
			case "local":
				segment=LOCAL;
				index = "@"+index+"\n";
				break;
			case "this":
				segment=THIS;
				index = "@"+index+"\n";
				break;
			case "that":
				segment=THAT;
				index = "@"+index+"\n";
				break;
			case "static":
				index = this.file_name+"."+index;
				has_segment = false;
				break;
			case "constant":
				index = "@"+index+"\n";
				is_constant = true;
				has_segment = false;
				break;
			case "temp":
				index ="@"+String.valueOf(Integer.parseInt(index)+5)+"\n";
				has_segment = false;
				break;
		}
		
		if(type==Parser.C_PUSH) {
			if(has_segment) { //arg, local, this, that
				temp = "//Push\n"+segment+"D=A"+index+"A=D+A\nD=M\n"+SP+"A=M\nM=D\n"+SP+"M=M+1\n";
			}else if(is_constant){ //constant
				temp = "//Push\n"+index+"D=A\n"+SP+"A=M\n"+"M=D\n"+SP+"M=M+1\n";
			}else { //static or temp
				temp = "//Push\n"+index+"D=M\n"+SP+"A=M\nM=D\n"+SP+"M=M+1\n";
			}
			
		}else if(type==Parser.C_POP) {
			if(has_segment) {
				temp = "//Pop\n"+segment+"D=A"+index+"D=A+D"+SP+"A=M"+"M=D"+"A=A-1"+"D=M"+SP+"A=M"+
						"A=M"+"M=D"+SP+"M=M-1";
			}else {
				temp = "//Pop\n"+SP+"AM=M-1\nD=M\n"+index+"M=D\n";
			}
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
