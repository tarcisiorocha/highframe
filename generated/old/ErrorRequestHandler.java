package comanche;

import java.io.IOException;
import org.objectweb.fractal.fraclet.annotations.Component;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class ErrorRequestHandler extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , comanche.RequestHandler {


	public ErrorRequestHandler (IUnknown mpIOCM) {
		super(mpIOCM);
	}

    public void handleRequest(Request r) throws IOException {
        r.out.print("HTTP/1.0 404 Not Found\n\n");
        r.out.print("<html>Document not found.</html>");
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