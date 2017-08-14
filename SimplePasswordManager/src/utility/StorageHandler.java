package utility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StorageHandler {
	private String path;
	private File file;
	private boolean has_been_initialized = false;
	
	public StorageHandler( String p ){
		path = p;
		file = new File(path);
		try {
			file.createNewFile(); // if file already exists will do nothing 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		has_been_initialized = true;
	}
	
	public void saveToTextFile( String data ){
		if (has_been_initialized == false )
			throw new java.lang.Error("Cant do that, sorry!");
		FileOutputStream oFile = null;
		
		try {
			oFile = new FileOutputStream(file, false);
			// get the content in bytes
			byte[] contentInBytes = data.getBytes();

			oFile.write(contentInBytes);
			oFile.flush();
			oFile.close();
		 } catch (FileNotFoundException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		 } finally {
			 try {
				 if (oFile != null)
					 oFile.close();
			  } catch (IOException e) {
				 e.printStackTrace();
			  }
		 }
	}
	
	public String getAllFromFile(){
		if (has_been_initialized == false )
			throw new java.lang.Error("Cant do that, sorry!");
		Path fileLocation = Paths.get(path);
		byte[] data = null;
		try {
			data = Files.readAllBytes(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return new String(data);
	}
	
	public void saveJSONData( Object[][] o ){
		if (has_been_initialized == false )
			throw new java.lang.Error("Cant do that, sorry!");
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(file, false);
			// get the content in bytes
			GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
	        String data = gson.toJson(o);
			byte[] contentInBytes = data.getBytes();
	
			oFile.write(contentInBytes);
			oFile.flush();
			oFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null)
					oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Object[][] getJSONData(){
		if (has_been_initialized == false )
			throw new java.lang.Error("Cant do that, sorry!");
		String JSON = getAllFromFile();
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
       
        return gson.fromJson(JSON, Object[][].class);
	}

	/*********
	* For security reasons:
	* prevent common breaches
	*/
	private final void readObject(ObjectInputStream in) throws java.io.IOException {
		throw new java.io.IOException("Class cannot be deserialized");
	}
  
	private final void writeObject(ObjectOutputStream out) throws java.io.IOException {
		throw new java.io.IOException("Object cannot be serialized");
	}
  
	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException {
		throw new java.lang.CloneNotSupportedException();
	}
}
