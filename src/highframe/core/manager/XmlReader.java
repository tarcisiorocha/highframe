package highframe.core.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlReader {
	
	private List<Component> componentList;
	private List<Binding> bindingList;
	private List<ExportedInterface> exportedInterfaceList;

	public XmlReader() {
		componentList = new ArrayList<Component>();
		bindingList = new ArrayList<Binding>();
		exportedInterfaceList = new ArrayList<ExportedInterface>();
	}

	public void listarComponentes() {
		for (int i = 0; i < componentList.size(); i++) {
			System.out.println(componentList.get(i).toString());
		}
	}

	public List<Component> getComponentList() {
		return componentList;
	}
	
	public List<Binding> getBindingList() {
		return bindingList;
	}
	
	public List<ExportedInterface> getExportedInterfaceList() {
		return exportedInterfaceList;
	}

	public void readXML(File f) {
		try {
			SAXBuilder sb = new SAXBuilder();  
			Document d = (Document) sb.build(f);
			Element mural = d.getRootElement();
			List<Element> architecture = mural.getChildren();
			Iterator<Element> architectureIterator = architecture.iterator();
			while (architectureIterator.hasNext()) {
				Element subarchitecture = (Element) architectureIterator.next();  
				if(subarchitecture.getName().equals("subarchitecture")){

					System.out.println("=============== SubArchitecture ===============");
					System.out.println("***ID Subarquitetura: "+subarchitecture.getAttributeValue("idsubarchitecture"));
					System.out.println("***Nome Subarquitetura: "+subarchitecture.getAttributeValue("name"));

					List<Element> subarchitectureElements = subarchitecture.getChildren();
					Iterator<Element> subarchitectureIterator = subarchitectureElements.iterator();
					while (subarchitectureIterator.hasNext()) {
						Element subarchitectureElement = (Element) subarchitectureIterator.next();

						//Componente
						if (subarchitectureElement.getName().equals("component")) {
							System.out.println("*************** Componentes **************");
							System.out.println("***Nome: "+subarchitectureElement.getAttributeValue("name"));
							System.out.println("***ID: "+subarchitectureElement.getAttributeValue("id"));
							Component c = new Component();							
							c.setComponentName(subarchitectureElement.getAttributeValue("name"));
							c.setComponentId(subarchitectureElement.getAttributeValue("id"));							
							List<Element> interfaces = subarchitectureElement.getChildren();
							if (!interfaces.isEmpty()) {
								ArrayList<Interface> listaInterfaces = new ArrayList<Interface>();
								Iterator<Element> interfacesIterator = interfaces.iterator();								
								while (interfacesIterator.hasNext()) {
									System.out.println("::::::::::::::: Interfaces :::::::::::::::");
									Interface i = new Interface();
									Element interfacez = (Element) interfacesIterator.next();
									System.out.println("***Tipo de Interface:" + interfacez.getName());
									System.out.println("***Nome: "+ interfacez.getAttributeValue("name"));  
									System.out.println("***ID: "+ interfacez.getAttributeValue("idinterface"));
									System.out.println("***Assinatura: "+ interfacez.getAttributeValue("signature"));
									i.setInterfaceType(interfacez.getName());
									i.setNameInterface(interfacez.getAttributeValue("name"));
									i.setIdInterface(interfacez.getAttributeValue("idinterface"));
									i.setSignature(interfacez.getAttributeValue("signature"));
									listaInterfaces.add(i);

									//									List<Element> interfaces =  interfacez.getChildren();
									//									
									//									Iterator<Element> k = interfaces.iterator();
									//									while (k.hasNext()) {
									//										Element interfaceimp = (Element) k.next();
									//										System.out.println("::::::Nome:"+ interfaceimp.getAttributeValue("nome"));  
									//										System.out.println("::::::Assinatura:"+ interfaceimp.getAttributeValue("assinatura")); 
									//
									//									}
								}
								c.setInterfaces(listaInterfaces);
							}
							this.componentList.add(c);
						} else if (subarchitectureElement.getName().equals("binding")) {
							System.out.println("*************** Bindings **************");
							System.out.println("***Componente Cliente: "+subarchitectureElement.getAttributeValue("clientcomponent"));
							System.out.println("***Interface Cliente: "+subarchitectureElement.getAttributeValue("clientinterface"));
							System.out.println("***Componente Servidor: "+subarchitectureElement.getAttributeValue("servercomponent"));
							System.out.println("***Interface Servidor: "+subarchitectureElement.getAttributeValue("serverinterface"));
							Binding b = new Binding();
							b.setClientComponent(subarchitectureElement.getAttributeValue("clientcomponent"));
							b.setClientInterface(subarchitectureElement.getAttributeValue("clientinterface"));
							b.setServerComponent(subarchitectureElement.getAttributeValue("servercomponent"));
							b.setServerInterface(subarchitectureElement.getAttributeValue("serverinterface"));
							this.bindingList.add(b);
							
						} else if (subarchitectureElement.getName().equals("exportedinterface")) {
							System.out.println("*************** Exported Interfaces **************");						
							ExportedInterface e = new ExportedInterface();
							e.setComponentName(subarchitectureElement.getAttributeValue("componentname"));
							e.setInterfaceId(subarchitectureElement.getAttributeValue("idinterface"));
							List<Component> componentList = this.componentList;
							for (int i = 0; i < componentList.size(); i++) {
								for (int j = 0; j < componentList.get(i).getInterfaces().size(); j++) {
									if(e.getInterfaceId().equals(componentList.get(i).getInterfaces().get(j).getIdInterface())) {
										e.setSignature(componentList.get(i).getInterfaces().get(j).getSignature());
										e.setComponentName(componentList.get(i).getComponentName());
									}
								}
							}
							System.out.println("***Componente: "+e.getComponentName());
							System.out.println("***Interface: "+subarchitectureElement.getAttributeValue("idinterface"));
							System.out.println("***Assinatura: "+e.getSignature());
							this.exportedInterfaceList.add(e);
						}

					}
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

	}

}
