package comanche.fraclet;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

@Component(provides = @Interface(name = "l8", signature = comanche.fraclet.ILogger.class))
public class Logger8 implements ILogger {
	public void log (String msg) {
		if (msg != null)
		System.out.println(msg); 
	}
}