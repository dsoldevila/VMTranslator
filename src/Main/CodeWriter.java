package Main;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;


public class CodeWriter {
	
	private BufferedWriter file = null;
	private String file_name; //without extension
	
	/*STACK POINTER*/
	private static final String SP = "@SP";
	
	/*FREE USER REGISTERS
	private static final String R13 = "@R13";
	private static final String R14 = "@R14";
	private static final String R15 = "@R15";
	*/
	
	/* ARITHMETIC */
	private static final String[] add = {SP,"AM=M-1","D=M","A=A-1","M=M+D"};
	private static final String[] sub = {SP,"AM=M-1","D=M","A=A-1","M=M-D"};
	private static final String[] neg = {SP, "A=M-1", "M=-M"};
	
	/*CONDITIONALS*/
	private static final String[] eq = {SP, "AM=M-1", "D=M", SP, "AM=M-1", "D=D-M", "M=0", "@LABEL_n", "D;JNE", SP, "A=M", "M=-1", 
			"(LABEL_n)", SP, "M=M+1"};
	private static final String[] gt = {SP, "AM=M-1", "D=M", SP, "AM=M-1", "D=M-D", "M=0", "@LABEL_n", "D;JLE", SP, "A=M", "M=-1", 
			"(LABEL_n)", SP, "M=M+1"};
	private static final String[] lt = {SP, "AM=M-1", "D=M", SP, "AM=M-1", "D=M-D", "M=0", "@LABEL_n", "D;JGE", SP, "A=M", "M=-1", 
			"(LABEL_n)", SP, "M=M+1"};
	
	private int label_counter;
	String label; 
	
	/*LOGICAL*/
	private static final String[] and = {SP, "AM=M-1", "D=M", "A=A-1", "M=M&D"};
	private static final String[] or = {SP, "AM=M-1", "D=M", "A=A-1", "M=M|D"};
	private static final String[] not = {SP, "A=M-1", "M=!M"};
	
	
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
	 * @param file_name
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
				temp[7] = "@"+label;
				temp[12] = "("+label+")";
				label_counter++;
				break;
			case "gt":
				temp = gt.clone();
				temp[7] = "@"+label;
				temp[12] = "("+label+")";
				label_counter++;
				break;
			case "lt":
				temp = lt.clone();
				temp[7] = "@"+label;
				temp[12] = "("+label+")";
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
			this.file.write("//"+command);
			this.file.newLine();
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
	public void writePushPop(byte type, String seg, String ind) {
		String[] temp = null;
		String segment = seg;
		String index = ind;
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
				String[] t = {segment, "D=M", index, "A=D+A", "D=M", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}else if(is_constant){ //constant
				String[] t = {index, "D=A", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}else { //static or temp
				String[] t = {index, "D=M", SP, "A=M", "M=D", SP, "M=M+1"};
				temp = t.clone();
			}
			
		}else if(type==Parser.C_POP) {
			if(has_segment) { //arg, local, this, that
				String[] t = {segment, "D=M", index, "D=A+D", SP, "A=M", "M=D", SP, "AM=M-1", "D=M", SP, "A=M+1", "A=M", "M=D"};
				//String[] t = {segment, "D=M", index, "D=A+D", R13, "M=D", SP, "AM=M-1", "D=M", R13, "A=M", "M=D"};
				temp = t.clone();
			}else {
				String[] t = {SP, "AM=M-1", "D=M", index, "M=D"};
				temp = t.clone();
			}
		}
		
		try {
			if(type==Parser.C_PUSH) {
				this.file.write("//Push"+" "+seg+" "+ind);
				this.file.newLine();
			}else if(type==Parser.C_POP) {
				this.file.write("//Pop"+" "+seg+" "+ind);
				this.file.newLine();
			}
			for(int i=0; i<temp.length; i++) {
				this.file.write(temp[i]);
				this.file.newLine();
			}
			this.file.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Couldn't write on output file");
		}
}
	
	/**
	 * Informs the codeWrtier that the translation of a new VM file has started.
	 * @param name
	 */
	public void setFileName(String name) {
		
	}
	
	/**
	 * Writes the assembly instructions that effect the bootstrap code that initializes
	 * the VM. THis code must be placed at the beginning of the generated *.asm file.
	 */
	public void writeInit() {
		
	}
	
	/**
	 * Writes the assembly code that effects the label command.
	 * @param string
	 */
	public void writeLabel(String string) {
		
	}
	
	/**
	 * 
	 * @param string
	 */
	public void writeGoto(String string) {
			
	}
	
	/**
	 * 
	 * @param string
	 */
	public void writeIf(String string) {
		
	}
	
	/**
	 * 
	 * @param string
	 * @param numVars
	 */
	public void writeFunction(String string, int numVars) {
		
	}
	
	/**
	 * 
	 * @param string
	 * @param numArgs
	 */
	public void writeCall(String string, int numArgs) {
			
		}

	/**
	 * 
	 */
	public void writeReturn() {
		
	}
	
	public void close() {
		try {
			this.file.close();
		} catch (IOException e) {
			System.out.println("ERROR: Error at closing the output file");
		}
	}
}


