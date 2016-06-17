

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ErrorWriter {

	private static Path path;  
	private static File file;
	private static PrintWriter writer;
	
	public ErrorWriter(String pathstring){
		path = Paths.get(pathstring);
		file = new File(path.toString());
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer = new PrintWriter(path.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String str){
		writer.write(str);
	}
	
	public void close(){
		writer.close();
	}
	
	
}
