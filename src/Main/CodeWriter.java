package Main;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class CodeWriter {
	
	private BufferedWriter file = null;
	private String file_name; //without extension
	private String function_name = null;
	
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
	
	private int conditional_lab_count; //both used to generate "built-in" labels for eq,gt,lt ops
	String conditional_label; 
	
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
		
		conditional_lab_count = 0;
		
	}
	
	
	/**
	 * Writes to the output file the assembly code that implements
	 * the given command
	 * @param command
	 */
	public void writeArithmetic(String command) {
		String[] temp = null;
		conditional_label = "LABEL_"+ String.valueOf(conditional_lab_count); 
		
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
				temp[7] = "@"+conditional_label;
				temp[12] = "("+conditional_label+")";
				conditional_lab_count++;
				break;
			case "gt":
				temp = gt.clone();
				temp[7] = "@"+conditional_label;
				temp[12] = "("+conditional_label+")";
				conditional_lab_count++;
				break;
			case "lt":
				temp = lt.clone();
				temp[7] = "@"+conditional_label;
				temp[12] = "("+conditional_label+")";
				conditional_lab_count++;
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
			System.out.println("ERROR: Couldn't write ARITHMETIC COM on output file\"");
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
			System.out.println("ERROR: Couldn't write PUSH/POP on output file");
		}
	}
	
	/**
	 * Informs the codeWrtier that the translation of a new VM file has started.
	 * @param name
	 */
	public void setFileName(String name) {
		this.close();
		try {
			this.file = new BufferedWriter(new FileWriter(file_name));
		} catch (IOException e) {
			System.out.println("ERROR: Output can't be used or created");
		}		
		this.file_name = file_name.substring(0, file_name.indexOf("."));
		
	}
	
	/**
	 * Writes the assembly instructions that effect the bootstrap code that initializes
	 * the VM. THis code must be placed at the beginning of the generated *.asm file.
	 */
	public void writeInit() {
		
	}
	
	/**
	 * Writes the assembly code that effects the label command.
	 * @param label
	 */
	public void writeLabel(String label) {
		String[] code = null;
		if(this.function_name==null) { //if the code it's not in a function
			String[] temp = {"//Label", "("+this.file_name+"."+label+")"};
			code = temp.clone();
		}else {
			String[] temp = {"//Label", "("+this.file_name+"."+this.function_name+"$"+label+")"};
			code = temp.clone();
		}
		try {
			for(int i = 0; i<code.length; i++) {
				this.file.write(code[i]);
				this.file.newLine();
			}
			this.file.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Couldn't write LABEL on output file");
		}
	}
	
	/**
	 * 
	 * @param label
	 */
	public void writeGoto(String label) {
		if(this.function_name==null) { //if the code it's not in a function
			label = "@"+this.file_name+"."+label;
		}else {
			label = "@"+this.file_name+"."+this.function_name+"$"+label;
		}
		String[] code = {"//Goto", label, "D;JMP"};
		try {
			for(int i = 0; i<code.length; i++) {
				this.file.write(code[i]);
				this.file.newLine();
			}
			this.file.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Couldn't write LABEL on output file");
		}
	}
	
	/**
	 * 
	 * @param label
	 */
	public void writeIf(String label) {
		if(this.function_name==null) { //if the code it's not in a function
			label = "@"+this.file_name+"."+label;
		}else {
			label = "@"+this.file_name+"."+this.function_name+"$"+label;
		}
		String[] code = {"//If-goto", SP, "AM=M-1", "D=M", label, "D;JNE"};
		try {
			for(int i = 0; i<code.length; i++) {
				this.file.write(code[i]);
				this.file.newLine();
			}
			this.file.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Couldn't write LABEL on output file");
		}
	}
	
	/**
	 * Writes function Label and pushes numVars 0 to stack to init Local variables
	 * @param function_name
	 * @param numVars
	 */
	public void writeFunction(String function_name, String numVars) {
		int nV = Integer.parseInt(numVars);
		this.function_name = function_name;
		try {
			this.file.write("("+this.file_name+"."+function_name+")");  //writes label, aka function pointer
			this.file.newLine();
			
			//init local variables to 0
			for(int i = 0; i<nV; i++) {
				this.writePushPop(Parser.C_PUSH, "constant", "0");
			}
			
			this.file.flush();
			
		} catch (IOException e) {
			System.out.println("ERROR: Couldn't write FUNCTION on output file");
		}
	}
	
	/**
	 * 
	 * @param string
	 * @param numArgs
	 */
	public void writeCall(String string, String numArgs) {
			
		}

	/**
	 * 
	 */
	public void writeReturn() {
		/* EndFrame = LCL */
		String[] ef_code = {"//Return", LOCAL, "D=M", SP, "A=M", "M=D", SP, "M=M+1"}; //Push local start address to the stack
		this.writeString(ef_code);

		this.writePushPop(Parser.C_POP, "temp", "0"); //EndFrame = temp 0
		
		/* retAddr = *(EndFrame-5)*/
		this.writePushPop(Parser.C_PUSH, "temp", "0");
		this.writePushPop(Parser.C_PUSH, "constant", "5");
		this.writeArithmetic("sub");
		this.writePushPop(Parser.C_POP, "temp", "1"); //retAddr = temp 1
		
		/* *ARG = return value*/
		this.writePushPop(Parser.C_POP, "argument", "0");
		
		/*SP = ARG +1 */
		String[] sp_code = {ARGUMENT, "D=M", "@1", "D=D+A", SP, "M=D"};
		this.writeString(sp_code);
		
		/* THAT = *(EndFrame-1) (endFrame == temp 0 aka @5)*/
		String[] that_code = {"//r_that", "@5", "D=M", "@1", "A=D-A", "D=M", THAT, "M=D"};
		this.writeString(that_code);
		
		/* THIS = *(EndFrame-2)*/	
		String[] this_code = {"//r_this", "@5", "D=M", "@2", "A=D-A", "D=M", THAT, "M=D"};
		this.writeString(this_code);
		
		/* ARG = *(EndFrame-3)*/
		String[] arg_code = {"//r_arg", "@5", "D=M", "@3", "A=D-A", "D=M", THAT, "M=D"};
		this.writeString(arg_code);
		
		/* LCL = *(EndFrame-4)*/
		String[] lcl_code = {"//r_lcl", "@5", "D=M", "@4", "A=D-A", "D=M", THAT, "M=D"};
		this.writeString(lcl_code);
		
		/* goto retAddr (temp 1 aka @6)*/
		String[] goto_code = {"@6", "A=M", "A=M", "D;JMP"};
		this.writeString(goto_code); 
		
		
		
	}
	
	public void close() {
		try {
			this.file.close();
		} catch (IOException e) {
			System.out.println("ERROR: Error at closing the output file");
		}
	}
	
	public void writeString(String[] code) {
		try {
			for(int i = 0; i<code.length; i++) {
				this.file.write(code[i]);
				this.file.newLine();
			}
			this.file.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Error at writing on output file");
		}
			
	}
}


