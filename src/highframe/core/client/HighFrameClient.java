package highframe.core.client;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
 
public class HighFrameClient {
 
    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        WebAppContext wac = new WebAppContext();
        wac.setResourceBase(".");
        wac.setDescriptor("lib/WEB-INF/web.xml");
        wac.setContextPath("/");
        wac.setParentLoaderPriority(true);
        server.setHandler(wac);
        server.start();
        server.join();
    }
    
    public void run() {
        try {
        	Server server = new Server(8888);
            WebAppContext wac = new WebAppContext();
            wac.setResourceBase(".");
            wac.setDescriptor("lib/WEB-INF/web.xml");
            wac.setContextPath("/");
            wac.setParentLoaderPriority(true);
            server.setHandler(wac);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}