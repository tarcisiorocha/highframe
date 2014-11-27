package comanche;

import org.objectweb.fractal.fraclet.annotations.Component;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class SequentialScheduler extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , comanche.Scheduler {


	public SequentialScheduler (IUnknown mpIOCM) {
		super(mpIOCM);
	}

    public void schedule(Runnable task) {
        task.run();
    }


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