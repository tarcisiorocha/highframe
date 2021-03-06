package specific.comanche.fraclet;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import comanche.fraclet.Request;
import comanche.fraclet.Response;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class Scheduler10 extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , comanche.fraclet.IScheduler {


	public Scheduler10 (IUnknown mpIOCM) {
		super(mpIOCM);
	}

	public void schedule (Runnable task) { new Thread(task).start(); }


	@Override
	public boolean startup(Object data) {
		return true;
	} 

	@Override
	public boolean shutdown() {
		return true;
	}
	
	public boolean connect(IUnknown pSinkIntf, String riid, long provConnID) {
		return false;
	}

	public boolean disconnect(String riid, long connID) {
		return false;
	}
}