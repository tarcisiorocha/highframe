package highframe.core.server;

import java.io.IOException;
import java.net.MalformedURLException;

import highframe.core.server.generictoxml.ConvertClassesToXml;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public class HighFrameServer {

	public static void main(String[] args) {
		try {
			Server server = new Server(8889);
			ResourceHandler resource_handler = new ResourceHandler();
			resource_handler.setDirectoriesListed(true);
			resource_handler.setWelcomeFiles(new String[] { "index.html" });
			resource_handler.setResourceBase("generic");

			WebAppContext wac = new WebAppContext();
			wac.setResourceBase(".");
			wac.setDescriptor("lib/WEB-INF/webServer.xml");
			wac.setContextPath("/");
			wac.setParentLoaderPriority(true);

			HandlerCollection handlerCollection = new HandlerCollection();
			handlerCollection.setHandlers(new Handler[] { resource_handler, wac });

			server.setHandler(handlerCollection);

			ConvertClassesToXml co = new ConvertClassesToXml();
			co.startConvert("generic", "generic/xml");

			server.start();
			server.join();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}