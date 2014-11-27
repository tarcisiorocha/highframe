package specific.comanche.fraclet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;
import comanche.fraclet.Request;
import comanche.fraclet.Response;
import comanche.fraclet.IScheduler;
import comanche.fraclet.IAnalyzer;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class Receiver extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , highframe.core.Runner {

	private OCM_SingleReceptacle<comanche.fraclet.IScheduler> s;
	private OCM_SingleReceptacle<comanche.fraclet.IAnalyzer> ia;
	private java.net.ServerSocket ss;

	public Receiver (IUnknown mpIOCM) {
		super(mpIOCM);
		this.s = new OCM_SingleReceptacle<comanche.fraclet.IScheduler>(comanche.fraclet.IScheduler.class);
		this.ia = new OCM_SingleReceptacle<comanche.fraclet.IAnalyzer>(comanche.fraclet.IAnalyzer.class);
	}

	public void run () {
		try {
			ss = new ServerSocket(8080);
			System.out.println("Comanche HTTP Server ready on port 8080.");
			while (true) {
				final Socket socket = ss.accept();
				final Request r = new Request();        
				s.m_pIntf.schedule(new  Runnable () {
					public void run () {
						try {
							Reader in = new InputStreamReader(socket.getInputStream());
							PrintStream out = new PrintStream(socket.getOutputStream());
							String rq = new LineNumberReader(in).readLine();
							r.rq = rq;
							Response res = ia.m_pIntf.handleRequest(r);
							if (res != null) {
								out.print(res.message);
								out.write(res.data);
							}
							out.close();
							socket.close();
						} catch (Exception _) { }
					}
				});
			}

		} catch (IOException e) { e.printStackTrace(); } 
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
		if (riid.equals("comanche.fraclet.IScheduler")){ return this.s.connectToRecp(pSinkIntf, riid, provConnID); }
		if (riid.equals("comanche.fraclet.IAnalyzer")){ return this.ia.connectToRecp(pSinkIntf, riid, provConnID); }
		return false;
	}

	public boolean disconnect(String riid, long connID) {
		if (riid.equals("comanche.fraclet.IScheduler")){ return this.s.disconnectFromRecp(connID); }
		if (riid.equals("comanche.fraclet.IAnalyzer")){ return this.ia.disconnectFromRecp(connID); }
		return false;
	}
}