package comanche.fraclet;

import java.io.IOException;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import comanche.fraclet.Request;
import comanche.fraclet.Response;

@Component(provides = @Interface(name = "e", signature = comanche.fraclet.IErrorHandler.class))
public class ErrorHandler implements IErrorHandler {
	public Response handleRequest (Request r) throws IOException {
		Response res = new Response();
		res.message = "HTTP/1.0 404 Not Found\n\n"+"<html>Document not found.</html>";
		res.data = new byte[0];
		return res;    
	}
}