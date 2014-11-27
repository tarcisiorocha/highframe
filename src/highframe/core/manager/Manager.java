package highframe.core.manager;

import highframe.core.ComponentGeneratorFractal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Node;

public class Manager {
	
	public void generateComponents(String xmlData) {
		XmlReader xmlReader = new XmlReader();
		xmlReader.readXML(new File(xmlData));
		for (int i = 0; i < xmlReader.getComponentList().size(); i++) {
			try {
				Component c = xmlReader.getComponentList().get(i);
				Class<?> clazz = Class.forName(c.getComponentName());
				ComponentGeneratorFractal gc = new ComponentGeneratorFractal();
				gc.generateComponent(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	//LEMBRAR DE PREENCHER ESSE MÉTODO
	public void generateComponents(String componentModel, String algo) {
		
	}
	
	public static String serializeXml(Element element) throws Exception
	{
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    StreamResult result = new StreamResult(buffer);

	    DOMSource source = new DOMSource((Node) element);
	    TransformerFactory.newInstance().newTransformer().transform(source, result);

	    return new String(buffer.toByteArray());
	}
	
}
