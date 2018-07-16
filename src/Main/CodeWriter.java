package Main;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CodeWriter {
	private FileOutputStream file = null;
	
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
		byte[] temp = command.getBytes();
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
	public void writePushPop(char type, String segment, int index) {
		
	}
	
	public void close() {
		
	}

}
