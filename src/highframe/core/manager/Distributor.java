package highframe.core.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import utils.CustomClassLoader;
import zip.Zipper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class Distributor {
	
	private List<Deployment> deploymentList;
	private List<RemoteBinding> remoteBindingList;
	
	public Distributor() {
		deploymentList = new ArrayList<Deployment>();
		remoteBindingList = new ArrayList<RemoteBinding>();
	}
	
	public List<Deployment> getDeploymentList() {
		return deploymentList;
	}
	
	public List<RemoteBinding> getRemoteBindingList() {
		return remoteBindingList;
	}
	
	public void readPlanXml(String file) {
		try {
			File f = new File(file);
			SAXBuilder sb = new SAXBuilder();
			Document d = (Document) sb.build(f);
			Element plan = d.getRootElement();
			List<Element> planElements = plan.getChildren();
			Iterator<Element> planIterator = planElements.iterator();
			while (planIterator.hasNext()) {
				Element planElement = (Element) planIterator.next();				
				if(planElement.getName().equals("deployment")){
					System.out.println("=============== Deployment ===============");
					System.out.println("***ID Subarquitetura: "+planElement.getAttributeValue("idsubarchitecture"));
					System.out.println("***Modelo de Componentes: "+planElement.getAttributeValue("componentmodel"));
					System.out.println("***Host: "+planElement.getAttributeValue("host"));
					Deployment deployment = new Deployment();
					deployment.setIdsubarchitecture(planElement.getAttributeValue("idsubarchitecture"));
					deployment.setComponentModel(planElement.getAttributeValue("componentmodel"));
					deployment.setHost(planElement.getAttributeValue("host"));
					this.deploymentList.add(deployment);
				} else if (planElement.getName().equals("remotebinding")) {
					RemoteBinding rb = new RemoteBinding();
					System.out.println("=============== RemoteBinding ===============");
					System.out.println("***ID Interface Exportada Cliente: "+planElement.getAttributeValue("idexportedclientinterface"));
					System.out.println("***ID Interface Exportada Servidor: "+planElement.getAttributeValue("idexportedserverinterface"));
					System.out.println("***Tipo de Binding: "+planElement.getAttributeValue("bindingtype"));
					
					rb.setIdExportedClientInterface(planElement.getAttributeValue("idexportedclientinterface"));
					rb.setIdExportedServerInterface(planElement.getAttributeValue("idexportedserverinterface"));
					rb.setBindingType(planElement.getAttributeValue("bindingtype"));
					
					this.remoteBindingList.add(rb);
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendAll(String xmlPlan, String xmlData) {
		readPlanXml(xmlPlan);
		ExtractSubXml esx = new ExtractSubXml();
		esx.extract(xmlData, "generated");
		compile();
		CustomClassLoader.addPath("generic");
//		for (int i = 0; i < deploymentList.size(); i++) {
//			send(xmlPlan, "generated/"+deploymentList.get(i).getIdsubarchitecture()+".xml", deploymentList.get(i).getHost());
//		}
		for (int i = deploymentList.size()-1; i >= 0 ; i--) {
			System.out.println(i);
			send(xmlPlan, "generated/"+deploymentList.get(i).getIdsubarchitecture()+".xml", deploymentList.get(i).getHost());
		}
	}
	
	public void compile() {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,	null, null);		
		
		File dirGeneric = new File("generic");
		File[] filesGenerated = dirGeneric.listFiles();
		ArrayList<File> filesToCompile = new ArrayList<File>();
		for (int i = 0; i < filesGenerated.length; i++) {
			if(filesGenerated[i].getName().contains(".java")) {
				filesToCompile.add(filesGenerated[i]);
			}
		}
		
		Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(filesToCompile);
		File dir = new File("generic");
		dir.mkdirs();
		String[] opts = new String[] { "-d", "generic" };
		CompilationTask task = compiler.getTask(null, manager, null, Arrays.asList(opts), null, units);
		task.call();
	}
	
	public void send(String xmlPlan_, String xmlData_, String host) {
		Client client = Client.create();
		System.out.println(host);
		WebResource webResource = client.resource("http://"+host+":8888/resource");
		try {
			File xmlPlan = new File(xmlPlan_);
			File xmlData = new File(xmlData_);
			XmlReader xmlReader = new XmlReader();
			xmlReader.readXML(xmlData);
			HashSet<File> hashSetZip = new HashSet<File>();
			hashSetZip.add(xmlData);
			hashSetZip.add(xmlPlan);
//			File zip[] = new File[(xmlReader.getComponentList().size())+2];
//			zip[0] = xmlPlan;
//			zip[1] = xmlData;
			for (int i = 0; i < xmlReader.getComponentList().size(); i++) {
//				zip[i+2] = new File(getFile(Class.forName(xmlReader.getComponentList().get(i).getComponentName())));
				hashSetZip.add(new File(getFile(Class.forName(xmlReader.getComponentList().get(i).getComponentName()))));
				for (int j = 0; j < xmlReader.getComponentList().get(i).getInterfaces().size(); j++) {
					if (!xmlReader.getComponentList().get(i).getInterfaces().get(j).getSignature().equals("java.lang.Runnable")) {
						hashSetZip.add(new File(getFile(Class.forName(xmlReader.getComponentList().get(i).getInterfaces().get(j).getSignature()))));
					}
				}
			}
			File[] zip = new File[hashSetZip.size()];
			Iterator<File> iterator = hashSetZip.iterator();
			int i=0;
			while(iterator.hasNext()) {
				zip[i] = iterator.next();
				System.out.println(zip[i].getName());
				i++;
			}
//			File genericComponent = new File("src//br//teste//ClientImpl.java");
//			File zipInput[] = new File[] {xmlPlan, xmlData, genericComponent};
			Zipper zipper = new Zipper();
			File zipFile = new File("zipPlan.zip");
			zipper.zip(zip, zipFile);
			webResource.put(getfilebytes(zipFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] getfilebytes(File file)  {
		FileInputStream fis;
		byte[] bytes=null;
		try {
			fis = new FileInputStream(file);		
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];			
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                //System.out.println("read " + readNum + " bytes,");
            }        
            bytes = bos.toByteArray();
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return bytes;	
	}
	
	private String getFile1(Class<?> clazz) {
		String value = System.getProperty("user.dir").toString() + "\\src\\";
		value = value + clazz.getPackage().toString().replace(".", "\\").replace("package ", "") +
				"\\" + clazz.getSimpleName() + ".java";
		return value;
	}
	
	private String getFile(Class<?> clazz) {
		String value = System.getProperty("user.dir").toString() + "\\generic\\";
		value = value + clazz.getSimpleName() + ".java";
		return value;
	}

}
