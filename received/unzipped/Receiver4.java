package comanche.fraclet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

import comanche.fraclet.Request;
import comanche.fraclet.Response;
import comanche.fraclet.IScheduler;
import comanche.fraclet.IAnalyzer;

@Component(provides = @Interface(name = "r", signature = highframe.core.Runner.class))
public class Receiver4 implements highframe.core.Runner {
	
	@Requires(name = "s")
	private IScheduler s;
	@Requires(name = "ia")
	private IAnalyzer ia;
	
	private ServerSocket ss;
	
	
	// functional aspect
	public void run () {
		try {
			ss = new ServerSocket(8080);
			System.out.println("Comanche HTTP Server ready on port 8080.");
			while (true) {
				final Socket socket = ss.accept();
				final Request r = new Request();        
				s.schedule(new  Runnable () {
					public void run () {
						try {
							Reader in = new InputStreamReader(socket.getInputStream());
							PrintStream out = new PrintStream(socket.getOutputStream());
							String rq = new LineNumberReader(in).readLine();
							r.rq = rq;
							Response res = ia.handleRequest(r);
							if (res != null) {
								out.print(res.message);
								out.write(res.data);
							}
							out.close();
							socket.close();
						} catch (Exception _) { }
					}
				});
			}

		} catch (IOException e) { e.printStackTrace(); } 
	}
	
	@Override
	public String QueryInterface(String s) {
		return null;
	}
}