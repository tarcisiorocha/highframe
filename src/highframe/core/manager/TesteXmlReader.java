package highframe.core.manager;

import java.io.File;

public class TesteXmlReader {
	public static void main(String[] args) {
		XmlReader xml = new XmlReader();
		xml.readXML(new File("received/fromguitool/data.xml"));
	}
}