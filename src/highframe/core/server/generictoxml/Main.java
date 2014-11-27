package highframe.core.server.generictoxml;

import java.io.IOException;
import java.net.MalformedURLException;

import utils.CustomClassLoader;

public class Main {
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IOException {
		ConvertClassesToXml co = new ConvertClassesToXml();		
		co.startConvert("generic", "generic//xml");		
	}
}
