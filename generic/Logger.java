package comanche.fraclet;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = @Interface(name = "l", signature = comanche.fraclet.ILogger.class))
public class Logger implements ILogger {
	public void log (String msg) {
		if (msg != null)
		System.out.println(msg); 
	}
}