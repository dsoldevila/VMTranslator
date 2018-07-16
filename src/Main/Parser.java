package Main;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;


/**
 * Handles the parsinf of a single .vm file
 * Reads a VM command, parses it into tis lexical components and provides
 * convenient acces to these components
 * Ignores all white space and comments
 * @author David
 *
 */
public class Parser {
	
	/*Command Types*/
	public static final byte C_ARITHMETIC = 0;
	public static final byte C_PUSH = 1;
	public static final byte C_POP = 2;
	public static final byte C_LABEL = 3;
	public static final byte C_GOTO = 4;
	public static final byte C_IF = 5;
	public static final byte C_FUNCTION = 6;
	public static final byte C_RETURNN = 7;
	public static final byte C_CALL = 8;
	
	private BufferedReader file = null;
	private String current_command = null;
	private String next_command = null;
	//private byte[] code_line = new byte[20];
	private byte current_type;
	
	/**
	 * Opens the input file and gets ready to parse it
	 * @param file_name
	 */
	public Parser(String file_name) {
		try {
			this.file = new BufferedReader(new FileReader(file_name));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Input file not found");
		}
	}
	
	/**
	 * get the next command in the file
	 * @return true if there is a next_command
	 */
	public boolean hasMoreCommands() {
		
		boolean more_commands = true;
		try {
			if((this.next_command=this.file.readLine())==null)
				more_commands = false;
			else {
				this.next_command=this.next_command.trim();
			}
		} catch (IOException e) {
			System.out.println("ERROR: Can't read file");
		}
		
		return more_commands;
	}
	
	/**
	 * Reads next command from the input and makes it the current
	 * command.
	 * Called only if hasMoreCommands() == true.
	 * Initially there is no current command
	 */
	public void advance () {
		this.current_command = this.next_command;
		
	}
	
	/**
	 * 
	 * @return type of the current command, if it's not a valid type (empty line or comment), it 
	 * returns -1
	 */
	public byte commandType() {
		String temp = null;
		if(this.current_command.contains(" ")) {
			temp = this.current_command.substring(0, this.current_command.indexOf(" "));
		}else {
			temp = this.current_command;
		}
		byte type = -1;
		switch(temp) {
			case "add":
			case "sub":
			case "neg":
			case "eq":
			case "gt":
			case "lt":
			case "and":
			case "or":
			case "not":
				type = C_ARITHMETIC;
				break;
			case "pop":
				type = C_POP;
				break;
			case "push":
				type = C_PUSH;
				break;
		
		}
		this.current_type = type;
		return type;
	}
	
	/**
	 * 
	 * @return The first agument of the current_command.
	 * If C_ARITHMETIC, returns the command itself
	 * Should not be called if type is C_RETURN
	 */
	public String arg1() {
		String temp = null;
		if(current_type==C_ARITHMETIC) {
			temp = this.current_command;
			if(temp.contains(" "))
				temp = temp.substring(0, temp.indexOf(" "));
		}else if(current_type==C_PUSH || current_type==C_POP) {
			temp = this.current_command.substring(this.current_command.indexOf(" ")+1);
			temp = temp.substring(0, temp.indexOf(" "));
		}
		
		return temp;	
	}
	
	/**
	 * 
	 * @return The second agument of the current_command.
	 * Only Called if current_command is C_PUSH, C_POP, C_FUNCTION, C_CALL
	 */
	public String arg2() {
		String temp = null;
		if(current_type==C_PUSH || current_type==C_POP) {
			temp = this.current_command.substring(this.current_command.indexOf(" ")+1);
			temp = temp.substring(temp.indexOf(" ")+1);
		}
		return temp;
	}

}
