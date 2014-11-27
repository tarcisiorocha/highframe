package comanche;

import static com.google.common.collect.Maps.newTreeMap;
import static org.objectweb.fractal.fraclet.types.Cardinality.COLLECTION;
import java.io.IOException;
import java.util.Map;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Requires;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class RequestDispatcher extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , comanche.RequestHandler {

	private OCM_SingleReceptacle<java.util.Map> handlers;

	public RequestDispatcher (IUnknown mpIOCM) {
		super(mpIOCM);
		this.handlers = new OCM_SingleReceptacle<java.util.Map>(java.util.Map.class);
	}

    public void handleRequest(Request r) throws IOException {
        for (RequestHandler h : this.handlers.m_pIntf.values())
            try {
                h.handleRequest(r);
                return;
            } catch (IOException _) {
            }
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
		if (riid.equals("java.util.Map")){ return this.handlers.connectToRecp(pSinkIntf, riid, provConnID); }
		return false;
	}

	public boolean disconnect(String riid, long connID) {
		if (riid.equals("java.util.Map")){ return this.handlers.disconnectFromRecp(connID); }
		return false;
	}
}