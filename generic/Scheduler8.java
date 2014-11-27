package comanche.fraclet;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import comanche.fraclet.Request;
import comanche.fraclet.Response;

@Component(provides = @Interface(name = "s8", signature = comanche.fraclet.IScheduler.class))
public class Scheduler8 implements IScheduler {
	public void schedule (Runnable task) { new Thread(task).start(); }
}