package testes.auxiliares;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import zip.Zipper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ClientMain {

	public static void main(String[] args) {
		Client client = Client.create();
		WebResource webResource = client.resource("http://localhost:8080/resource");
		try {
			File xmlPlan = new File("Plan.xml");
			File xmlData = new File("Data.xml");
			File genericComponent = new File("src//br//teste//ClientImpl.java");
			File zipInput[] = new File[] {xmlPlan, xmlData, genericComponent};
			Zipper zipper = new Zipper();
			File someFile = new File("zipPlan.zip");
			zipper.zip(zipInput, someFile);
			ClientMain cl = new ClientMain();
			webResource.put(cl.getfilebytes(someFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		 
	}
	
	private byte[] getfilebytes(File file)  {
		FileInputStream fis;
		byte[] bytes=null;
		try {
			fis = new FileInputStream(file);		
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];			
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }        
            bytes = bos.toByteArray();
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return bytes;	
	}
}