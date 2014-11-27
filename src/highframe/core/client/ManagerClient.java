package highframe.core.client;

import highframe.core.IComponentGenerator;
import highframe.core.Runner;
import highframe.core.manager.Component;
import highframe.core.manager.Distributor;
import highframe.core.manager.XmlReader;
import interopframe.api.IFrame;
import interopframe.core.InteropFrame;
import interopframe.utils.ServerPadraoRMI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import utils.CustomClassLoader;
import zip.Zipper;
import OpenCOM.ILifeCycle;
import OpenCOM.IOpenCOM;
import OpenCOM.IUnknown;
import OpenCOM.OpenCOM;

@Path("/resource")
public class ManagerClient {
	
	public static int count=0;
	XmlReader xmlReader;
	
	@PUT
    @Consumes("application/octet-stream")
    public void putBookmark(byte[] ab) {
		try {
			count++;
			xmlReader = new XmlReader();
			this.setJavaHome();
			this.writeToDiskAndUnzip(ab);
			compile2();
			File dirUnzip = new File("received//unzipped");		    
		    Distributor d = new Distributor();
		    d.readPlanXml("received//unzipped//plan.xml");
		    File[] files = dirUnzip.listFiles();
		    for (int i = 0; i < d.getDeploymentList().size(); i++) {
		    	for (int j = 0; j < files.length; j++) {
		    		if (files[j].getName().equals(d.getDeploymentList().get(i).getIdsubarchitecture()+".xml")) {
		    			//System.out.println(files[j].getName());
		    			long ini = System.currentTimeMillis();
		    			this.generateComponents("received//unzipped//"+d.getDeploymentList().get(i).getIdsubarchitecture()+".xml", d.getDeploymentList().get(i).getGeneratorClass());
		    			long end = System.currentTimeMillis();
		    			System.out.println("Tempo de geração dos componentes: "+(end-ini));
		    			break;
		    		}
				}
			}
		    
		    this.compile();
			
		    CustomClassLoader.addPath(".");
		    System.out.println(count);
//		    List<Component> componentList = this.xmlReader.getComponentList();
//		    Class<?>[] classArray = new Class<?>[componentList.size()];
//		    Object[] objectArray = new Object[componentList.size()];
//		    for (int i = 0; i < componentList.size(); i++) {
//				classArray[i] = Class.forName("specific."+componentList.get(i).getComponentName());
//				objectArray[i] = classArray[i].newInstance();
//			}
		    
		    if (count%2 == 0) {
//		    	Class<?> clazzReceiver = Class.forName("specific.comanche.fraclet.Receiver");
//				Class<?> clazzScheduler = Class.forName("specific.comanche.fraclet.Scheduler");
				Class<?> clazzAnalyzer = Class.forName("specific.comanche.fraclet.Analyzer");
				Class<?> clazzLogger = Class.forName("specific.comanche.fraclet.Logger");
				Class<?> clazzDispatcher = Class.forName("specific.comanche.fraclet.Dispatcher");
				Class<?> clazzFileHandler = Class.forName("specific.comanche.fraclet.FileHandler");
				Class<?> clazzErrorHandler = Class.forName("specific.comanche.fraclet.ErrorHandler");			
				
//				Object receiver = clazzReceiver.newInstance();
//				Object scheduler = clazzScheduler.newInstance();
				Object analyzer = clazzAnalyzer.newInstance();
				Object logger = clazzLogger.newInstance();
				Object dispatcher = clazzDispatcher.newInstance();
				Object fileHandler = clazzFileHandler.newInstance();
				Object errorHandler = clazzErrorHandler.newInstance();
				
				Method bindFcAnalyzer = clazzAnalyzer.getDeclaredMethod("bindFc", new Class[] { String.class, Object.class });
				bindFcAnalyzer.invoke(analyzer, new Object[] { "id", dispatcher });
				bindFcAnalyzer.invoke(analyzer, new Object[] { "l", logger });
				
				Method bindFcDispatcher = clazzDispatcher.getDeclaredMethod("bindFc", new Class[] { String.class, Object.class });
				bindFcDispatcher.invoke(dispatcher, new Object[] { "ifh", fileHandler });
				bindFcDispatcher.invoke(dispatcher, new Object[] { "ieh", errorHandler });
				
				
//				Method bindFcReceiver = clazzReceiver.getDeclaredMethod("bindFc", new Class[] { String.class, Object.class });
//				bindFcReceiver.invoke(receiver, new Object[] { "s", scheduler });
				//Object client = clazzClient.newInstance();
				
				new ServerPadraoRMI();
				new InteropFrame(analyzer);
				//new InteropFrame(objectArray[1]);
							
//				IFrame iFrame = new InteropFrame();
//				iFrame.setParameters("specific.comanche.fraclet.Receiver",
//						"comanche.fraclet.IAnalyzer", "Fractal",
//						"specific.comanche.fraclet.Analyzer",
//						"comanche.fraclet.IAnalyzer", "Fractal");
//				iFrame.setParametersBinding("RMI");
//				iFrame.remoteBinding(receiver);
//				
//				Method run = clazzReceiver.getDeclaredMethod("run", (Class<?>[]) null);
//
//				run.invoke(receiver, (Object[]) null);
		    	
		    }
		    
		    if (count%2 != 0) {
		    	//Runtime do OpenCOM Client
				OpenCOM runtime = new OpenCOM();
		        IOpenCOM oc =  (IOpenCOM) runtime.QueryInterface("OpenCOM.IOpenCOM"); 
		       
		        IUnknown pRequestReceiverUnk = (IUnknown) oc.createInstance("specific.comanche.fraclet.Receiver", "Receiver");
		        ILifeCycle pRequestReceiverLife =  (ILifeCycle) pRequestReceiverUnk.QueryInterface("OpenCOM.ILifeCycle");
		        pRequestReceiverLife.startup(oc);
		        
		        IUnknown pSchedulerUnk = (IUnknown) oc.createInstance("specific.comanche.fraclet.Scheduler", "Scheduler");
		        ILifeCycle pSchedulerLife =  (ILifeCycle) pSchedulerUnk.QueryInterface("OpenCOM.ILifeCycle");
		        pSchedulerLife.startup(oc);
		        
		        oc.connect(pRequestReceiverUnk, pSchedulerUnk, "comanche.fraclet.IScheduler");
		        
		        
//		    	Class<?> clazzReceiver = Class.forName("specific.comanche.fraclet.Receiver");
//				Class<?> clazzScheduler = Class.forName("specific.comanche.fraclet.Scheduler");
//				Class<?> clazzAnalyzer = Class.forName("specific.comanche.fraclet.Analyzer");
//				Class<?> clazzLogger = Class.forName("specific.comanche.fraclet.Logger");
//				Class<?> clazzDispatcher = Class.forName("specific.comanche.fraclet.Dispatcher");
//				Class<?> clazzFileHandler = Class.forName("specific.comanche.fraclet.FileHandler");
//				Class<?> clazzErrorHandler = Class.forName("specific.comanche.fraclet.ErrorHandler");			
				
//				Object receiver = clazzReceiver.newInstance();
//				Object scheduler = clazzScheduler.newInstance();
//				Object analyzer = clazzAnalyzer.newInstance();
//				Object logger = clazzLogger.newInstance();
//				Object dispatcher = clazzDispatcher.newInstance();
//				Object fileHandler = clazzFileHandler.newInstance();
//				Object errorHandler = clazzErrorHandler.newInstance();
				
//				Method bindFcAnalyzer = clazzAnalyzer.getDeclaredMethod("bindFc", new Class[] { String.class, Object.class });
//				bindFcAnalyzer.invoke(analyzer, new Object[] { "id", dispatcher });
//				bindFcAnalyzer.invoke(analyzer, new Object[] { "l", logger });
//				
//				Method bindFcDispatcher = clazzDispatcher.getDeclaredMethod("bindFc", new Class[] { String.class, Object.class });
//				bindFcDispatcher.invoke(dispatcher, new Object[] { "ifh", fileHandler });
//				bindFcDispatcher.invoke(dispatcher, new Object[] { "ieh", errorHandler });
				
				
//				Method bindFcReceiver = clazzReceiver.getDeclaredMethod("bindFc", new Class[] { String.class, Object.class });
//				bindFcReceiver.invoke(receiver, new Object[] { "s", scheduler });
				//Object client = clazzClient.newInstance();
				
//				new ServerPadraoRMI();
//				new InteropFrame(analyzer);
				//new InteropFrame(objectArray[1]);
				IFrame iFrame = new InteropFrame("169.254.197.31", 6666);
				iFrame.setParameters("specific.comanche.fraclet.Receiver",
						"comanche.fraclet.IAnalyzer", "OpenCOM",
						"specific.comanche.fraclet.Analyzer",
						"comanche.fraclet.IAnalyzer", "Fractal");
				iFrame.setParametersBinding("WebServiceSOAP", "169.254.197.31");
				long ini = System.currentTimeMillis();
				iFrame.remoteBinding(oc);
				long end = System.currentTimeMillis();
				System.out.println("Tempo geração comunicação remota"+": "+(end-ini));
				
				final Runner r = (Runner) pRequestReceiverUnk.QueryInterface("highframe.core.Runner");
		        new Thread(new Runnable() {
					public void run() {
						r.run();
					}
				}).start();
//				Method run = clazzReceiver.getDeclaredMethod("run", (Class<?>[]) null);
//
//				run.invoke(receiver, (Object[]) null);
		    	
		    }
		    //System.out.println("Tempo HIGHFRAME CLIENT: "+(System.currentTimeMillis()-ini));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
	
	public void setJavaHome() {
		String javaHome = System.getProperty("java.home");
		File javaHomeDir = new File(javaHome);
		if ( (javaHome.contains("jre") && javaHome.contains("jdk")) || (javaHome.contains("bin") && javaHome.contains("jdk")) ) {
			javaHomeDir = javaHomeDir.getParentFile();
		}
		System.setProperty("java.home", javaHomeDir.toString());
	}
	
	public void writeToDiskAndUnzip(byte[] ab) {
		File dirReceived = new File("received");
		dirReceived.mkdirs();
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		File zipFile = new File("received\\plan_"+time+".zip");
	    FileOutputStream fos;
	    try {
			fos = new FileOutputStream(zipFile);
			fos.write(ab);
		    fos.flush();
		    fos.close();
		    System.out.println(date.toString() + " - O arquivo foi enviado através do método PUT com sucesso!!!");
		    Zipper zipper = new Zipper();
		    File dirUnzip = new File("received//unzipped");
		    CustomClassLoader.addPath("received//unzipped");
		    dirUnzip.mkdirs();
		    zipper.unzip(zipFile, dirUnzip);
		    zipFile.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateComponents(String xmlData, Class<?> clazzGenerator) {		
		Constructor<?> ctorComp;
		try {
			ctorComp = clazzGenerator.getDeclaredConstructor();
			ctorComp.setAccessible(true);
			IComponentGenerator co = (IComponentGenerator) ctorComp.newInstance();
			this.xmlReader.readXML(new File(xmlData));
			for (int i = 0; i < this.xmlReader.getComponentList().size(); i++) {			
				Component c = this.xmlReader.getComponentList().get(i);
				Class<?> clazz = Class.forName(c.getComponentName());
				long ini = System.currentTimeMillis();
				co.generateComponent(clazz);
				System.out.println("Tempo geração "+c.getComponentName()+": "+(System.currentTimeMillis()-ini));
//				for (int j = 0; j < this.xmlReader.getComponentList().get(i).getInterfaces().size(); j++) {
//					String signatureInterface = this.xmlReader.getComponentList().get(i).getInterfaces().get(j).getSignature();
//					if (signatureInterface.equals("java.lang.Runnable")) {
//						break;
//					}
//					signatureInterface = signatureInterface.substring(signatureInterface.lastIndexOf(".")+1);
//					FileChannel source = new FileInputStream(new File("received//unzipped//"+signatureInterface+".java")).getChannel();
//					FileChannel destination = new FileOutputStream(new File("generated//"+signatureInterface+".java")).getChannel();
//					source.transferTo(0, source.size(), destination);
//					source.close();
//					destination.close();
//				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public void compile() {
		CustomClassLoader.addPath("bin");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,	null, null);		
		
		File dirGenerated = new File("generated");
		File[] filesGenerated = dirGenerated.listFiles();
		ArrayList<File> filesToCompile = new ArrayList<File>();
		for (int i = 0; i < filesGenerated.length; i++) {
			if(filesGenerated[i].getName().contains(".java")) {
				filesToCompile.add(filesGenerated[i]);
			}
		}
		
		//Arrays.asList("generated//ClientImpl.java", "generated//ServerImpl.java");
		Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(filesToCompile);
		//File dir = new File("compiled\\fractal");
		//dir.mkdirs();
		String[] opts = new String[] { "-d", "bin" };
		CompilationTask task = compiler.getTask(null, manager, null, Arrays.asList(opts), null, units);
		task.call();
	}
	
	public void compileFile(File file) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,	null, null);
		
		//Arrays.asList("generated//ClientImpl.java", "generated//ServerImpl.java");
		Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(Arrays.asList(file));
		//File dir = new File("compiled\\fractal");
		//dir.mkdirs();
		String[] opts = new String[] { "-d", "bin" };
		CompilationTask task = compiler.getTask(null, manager, null, Arrays.asList(opts), null, units);
		task.call();
	}
	
	public void compile2() {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,	null, null);		
		
		File dirGeneric = new File("received//unzipped");
		File[] filesGenerated = dirGeneric.listFiles();
		ArrayList<File> filesToCompile = new ArrayList<File>();
		for (int i = 0; i < filesGenerated.length; i++) {
			if(filesGenerated[i].getName().contains(".java")) {
				filesToCompile.add(filesGenerated[i]);
			}
		}
		
		//Arrays.asList("generated//ClientImpl.java", "generated//ServerImpl.java");
		Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(filesToCompile);
		//File dir = new File("received//unzipped");
		//dir.mkdirs();
		String[] opts = new String[] { "-d", "received//unzipped" };
		CompilationTask task = compiler.getTask(null, manager, null, Arrays.asList(opts), null, units);
		task.call();
	}
}