package comanche;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Requires;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class RequestAnalyzer extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , comanche.RequestHandler, java.lang.Runnable {

	private OCM_SingleReceptacle<comanche.RequestHandler> rh;
	private OCM_SingleReceptacle<comanche.Logger> l;

	public RequestAnalyzer (IUnknown mpIOCM) {
		super(mpIOCM);
		this.rh = new OCM_SingleReceptacle<comanche.RequestHandler>(comanche.RequestHandler.class);
		this.l = new OCM_SingleReceptacle<comanche.Logger>(comanche.Logger.class);
	}

    public String testando() {
    	l.m_pIntf.log("Teste");
    	String teste = "algo";
    	return teste;
    }

    public void handleRequest(Request r) throws IOException {
        r.in = new InputStreamReader(r.s.getInputStream());
        r.out = new PrintStream(r.s.getOutputStream());
        String rq = new LineNumberReader(r.in).readLine();
        l.m_pIntf.log(rq);
        if (rq.startsWith("GET ")) {
            r.url = rq.substring(5, rq.indexOf(' ', 4));
            rh.m_pIntf.handleRequest(r);
        }
        r.out.close();
        r.s.close();
    }

	public void run() {
		// TODO Auto-generated method stub
		
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
		if (riid.equals("comanche.RequestHandler")){ return this.rh.connectToRecp(pSinkIntf, riid, provConnID); }
		if (riid.equals("comanche.Logger")){ return this.l.connectToRecp(pSinkIntf, riid, provConnID); }
		return false;
	}

	public boolean disconnect(String riid, long connID) {
		if (riid.equals("comanche.RequestHandler")){ return this.rh.disconnectFromRecp(connID); }
		if (riid.equals("comanche.Logger")){ return this.l.disconnectFromRecp(connID); }
		return false;
	}
}