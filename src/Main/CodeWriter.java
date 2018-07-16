package Main;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CodeWriter {
	private FileOutputStream file = null;
	
	//TODO
	//AM=M-1 is wrong, fix later
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
	
	
	/**
	 * Opens the output file and gets ready to write into it
	 */
	public CodeWriter(String file_name) {
		try {
			this.file = new FileOutputStream(file_name);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Output can't be used or created");
		}		
		
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
	public void writePushPop(byte type, String segment, int index) {
		if(type==Parser.C_PUSH) {
			
		}else if(type==Parser.C_POP) {
			
		}
	}
	
	public void close() {
		
	}

}
