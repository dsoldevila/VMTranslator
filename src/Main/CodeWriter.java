package Main;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;


public class CodeWriter {
	private BufferedWriter file = null;
	
	private String file_name; //without extension
	
	private static final String SP = "@SP";
	
	/*FREE USER REGISTERS*/
	private static final String R13 = "@R13";
	private static final String R14 = "@R14";
	private static final String R15 = "@R15";
	
	/* ARITHMETIC */
	private static final String[] add = {"//add",SP,"AM=M-1","D=M","A=A-1","M=M+D"};
	private static final String[] sub = {"//sub",SP,"AM=M-1","D=M","A=A-1","M=M-D"};
	private static final String[] neg = {"//neg", SP, "A=M-1", "M=-M"};
	
	/*CONDITIONALS*/
	private static final String[] eq = {"//eq", SP, "AM=M-1", "D=M", SP, "AM=M-1", "D=D-M", "M=0", "@LABEL_n", "D;JNE", SP, "A=M", "M=-1", 
			"(LABEL_n)", SP, "M=M+1"};
	private static final String[] gt = {"//eq", SP, "AM=M-1", "D=M", SP, "AM=M-1", "D=D-M", "M=0", "@LABEL_n", "D;JLT", SP, "A=M", "M=-1", 
			"(LABEL_n)", SP, "M=M+1"};
	private static final String[] lt = {"//eq", SP, "AM=M-1", "D=M", SP, "AM=M-1", "D=D-M", "M=0", "@LABEL_n", "D;JGT", SP, "A=M", "M=-1", 
			"(LABEL_n)", SP, "M=M+1"};
	
	private int label_counter;
	String label; 
	
	/*LOGICAL*/
	private static final String[] and = {"//and", SP, "AM=M-1", "D=M", "A=A-1", "M=M&D"};
	private static final String[] or = {"//and", SP, "AM=M-1", "D=M", "A=A-1", "M=M|D"};
	private static final String[] not = {"//not", SP, "A=M-1", "M=!M"};
	
	
	/* SEGMENTS */
	private static final String ARGUMENT = "@ARG";
	private static final String THIS = "@THIS";
	private static final String THAT = "@THAT";
	private static final String LOCAL = "@LCL";
	//temp == 5+i
	//Static == Filename.i
	//constant == i
	//pointer 0/1 (THIS/THAT)
	
	
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
		
		label_counter = 0;
		
	}
	
	
	/**
	 * Writes to the output file the assembly code that implements
	 * the given command
	 * @param command
	 */
	public void writeArithmetic(String command) {
		String[] temp = null;
		label = "LABEL_"+ String.valueOf(label_counter);
		
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
				temp = eq.clone();
				temp[8] = "@"+label;
				temp[13] = "("+label+")";
				label_counter++;
				break;
			case "gt":
				temp = gt.clone();
				temp[8] = "@"+label;
				temp[13] = "("+label+")";
				label_counter++;
				break;
			case "lt":
				temp = lt.clone();
				temp[8] = "@"+label;
				temp[13] = "("+label+")";
				label_counter++;
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
			case "pointer":
				if(index.equals("0")) {
					index = THIS;
				}else {
					index = THAT;
				}
				has_segment = false;
				break;
		}
		if(type==Parser.C_PUSH) {
			if(has_segment) { //arg, local, this, that
				String[] t = {"//Push", segment, "D=M", index, "A=D+A", "D=M", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}else if(is_constant){ //constant
				String[] t = {"//Push", index, "D=A", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}else { //static or temp
				String[] t = {"//Push", index, "D=M", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}
			
		}else if(type==Parser.C_POP) {
			if(has_segment) { //arg, local, this, that
				String[] t = {"//Pop", segment, "D=M", index, "D=A+D", R13, "M=D", SP, "AM=M-1", "D=M", R13, "A=M", "M=D"};
				temp = t.clone();
			}else {
				String[] t = {"//Pop", SP, "AM=M-1", "D=M", index, "M=D"};
				temp = t.clone();
			}
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


