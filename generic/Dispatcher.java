package comanche.fraclet;

import java.io.IOException;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

import comanche.fraclet.Request;
import comanche.fraclet.Response;
import comanche.fraclet.IFileHandler;
import comanche.fraclet.IErrorHandler;

@Component(provides = @Interface(name = "id", signature = comanche.fraclet.IDispatcher.class))
public class Dispatcher implements IDispatcher {
	
	@Requires(name ="ifh")
	private IFileHandler ifh;
	@Requires(name= "ieh")
	private IErrorHandler ieh;	

	// functional aspect
	public Response handleRequest (Request r) throws IOException {
		Response res = null;	
		try {
			res = (this.ifh.handleRequest(r));
			return res;
		}
		catch (Exception e) {
			res = this.ieh.handleRequest(r);
			return res;
		}
	}
}