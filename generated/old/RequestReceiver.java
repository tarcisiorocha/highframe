package comanche;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class RequestReceiver extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , java.lang.Runnable {

	private OCM_SingleReceptacle<java.util.logging.Logger> log;
	private OCM_SingleReceptacle<int> port;
	private OCM_SingleReceptacle<comanche.Scheduler> s;
	private OCM_SingleReceptacle<comanche.RequestHandler> rh;

	public RequestReceiver (IUnknown mpIOCM) {
		super(mpIOCM);
		this.log = new OCM_SingleReceptacle<java.util.logging.Logger>(java.util.logging.Logger.class);
		this.port = new OCM_SingleReceptacle<int>(int.class);
		this.s = new OCM_SingleReceptacle<comanche.Scheduler>(comanche.Scheduler.class);
		this.rh = new OCM_SingleReceptacle<comanche.RequestHandler>(comanche.RequestHandler.class);
	}

    public void run() {
        try {
            ServerSocket ss = new ServerSocket(this.port);
            log.m_pIntf.info("Comanche HTTP Server ready on port " + this.port);
            log.m_pIntf.info("Load http://localhost:" + this.port + "/gnu.jpg");
            while (true) {
                final Socket socket = ss.accept();
                s.m_pIntf.schedule(new Runnable() {
                    public void run() {
                        try {
                            rh.m_pIntf.handleRequest(new Request(socket));
                        } catch (IOException _) {
                        }
                    }
                });
            }
        } catch (IOException e) {
            log.m_pIntf.log(SEVERE, "Comanche failure: ", e);
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
		if (riid.equals("java.util.logging.Logger")){ return this.log.connectToRecp(pSinkIntf, riid, provConnID); }
		if (riid.equals("int")){ return this.port.connectToRecp(pSinkIntf, riid, provConnID); }
		if (riid.equals("comanche.Scheduler")){ return this.s.connectToRecp(pSinkIntf, riid, provConnID); }
		if (riid.equals("comanche.RequestHandler")){ return this.rh.connectToRecp(pSinkIntf, riid, provConnID); }
		return false;
	}

	public boolean disconnect(String riid, long connID) {
		if (riid.equals("java.util.logging.Logger")){ return this.log.disconnectFromRecp(connID); }
		if (riid.equals("int")){ return this.port.disconnectFromRecp(connID); }
		if (riid.equals("comanche.Scheduler")){ return this.s.disconnectFromRecp(connID); }
		if (riid.equals("comanche.RequestHandler")){ return this.rh.disconnectFromRecp(connID); }
		return false;
	}
}