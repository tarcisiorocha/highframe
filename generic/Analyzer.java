package comanche.fraclet;

import java.io.IOException;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

import comanche.fraclet.IDispatcher;
import comanche.fraclet.ILogger;
import comanche.fraclet.Request;
import comanche.fraclet.Response;

@Component(provides = @Interface(name = "a", signature = comanche.fraclet.IAnalyzer.class))
public class Analyzer implements IAnalyzer {
	
	@Requires(name = "id")
	private IDispatcher id;
	@Requires(name = "l")
	private ILogger l;

		
	// functional aspect
	public Response handleRequest (Request r) throws IOException {
		String rq = r.rq;
		l.log(rq);
		try {
			if (rq.startsWith("GET ")) {
				r.url = rq.substring(5, rq.indexOf(' ', 4));
				return id.handleRequest(r);
			}
			else { return new Response(); }
		} catch (NullPointerException e) {
			return new Response();
		}
	}
}