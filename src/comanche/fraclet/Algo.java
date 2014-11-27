package comanche.fraclet;

import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OpenCOMComponent;

public class Algo extends OpenCOMComponent implements Coisa, IUnknown, ILifeCycle, IMetaInterface, IConnections{

	public Algo(IUnknown mpIOCM) {
		super(mpIOCM);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean connect(IUnknown arg0, String arg1, long arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect(String arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startup(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
