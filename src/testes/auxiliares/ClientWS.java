package testes.auxiliares;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ClientWS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 Client client = Client.create();
		 WebResource webResource = client.resource("http://localhost:8080/helloworld");
		 //WebResource webResource = client.resource("http://localhost:8080/GET-jdom-2.0.5.zip");
		 byte[] s = webResource.get(byte[].class);
		 File dirGet = new File("get");
		 dirGet.mkdirs();
		 File someFile = new File("get//GET-jdom-2.0.5.zip");
	     FileOutputStream fos;
	     
	 try {
		 fos = new FileOutputStream(someFile);
		 fos.write(s);
		 fos.flush();
		 fos.close();
		 System.out.println("O arquivo foi obtido através do método GET com sucesso!!!");
		  
		 webResource.put(s);
	 } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	 } catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
	 }		 
	}
}
