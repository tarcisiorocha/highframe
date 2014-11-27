package comanche;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.objectweb.fractal.fraclet.annotations.Component;
import OpenCOM.IConnections;
import OpenCOM.ILifeCycle;
import OpenCOM.IMetaInterface;
import OpenCOM.IUnknown;
import OpenCOM.OCM_SingleReceptacle;
import OpenCOM.OpenCOMComponent;

public class FileRequestHandler extends OpenCOMComponent implements IUnknown, ILifeCycle, IMetaInterface, IConnections , comanche.RequestHandler {


	public FileRequestHandler (IUnknown mpIOCM) {
		super(mpIOCM);
	}

    public void handleRequest(Request r) throws IOException {
        File f = new File(r.url);
        if (f.exists() && !f.isDirectory()) {
            InputStream is = new FileInputStream(f);
            byte[] data = new byte[is.available()];
            is.read(data);
            is.close();
            r.out.print("HTTP/1.0 200 OK\n\n");
            r.out.write(data);
        } else
            throw new IOException("File not found");
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