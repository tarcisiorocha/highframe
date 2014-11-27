package comanche.fraclet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import comanche.fraclet.Request;
import comanche.fraclet.Response;

@Component(provides = @Interface(name = "f", signature = comanche.fraclet.IFileHandler.class))
public class FileHandler implements IFileHandler {
	public Response handleRequest (Request r) throws IOException {
		File f = new File(r.url);
		if (f.exists() && !f.isDirectory()) {
			Response res = new Response();
			InputStream is = new FileInputStream(f);
			byte[] data = new byte[is.available()];
			is.read(data);
			res.data = data;
			is.close();
			res.message = "HTTP/1.0 200 OK\n\n";
			return res;
		} else {
			throw new IOException("File not found");
		}
	}
}