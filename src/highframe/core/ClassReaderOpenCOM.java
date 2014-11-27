package highframe.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.velocity.VelocityContext;
import org.objectweb.fractal.fraclet.annotations.Requires;

public class ClassReaderOpenCOM {
	
	private Class<?> clazz;
	private FileReader fr;
	private BufferedReader in;
	private VelocityContext vc;
	private ArrayList<String[]> fields;

	public ClassReaderOpenCOM(Class<?> classe) {
		this.clazz = classe;
		this.vc = new VelocityContext();
		this.fields = this.getFields();
		this.contextConnect();
		this.contextDisconnect();
		this.contextPackage();
		this.contextFields();
		this.contextConstructor();
		//this.getMethods();
		try {
			this.contextClassBody();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public VelocityContext getContext() {
		return this.vc;
	}

	public String getClassName() {
		return clazz.getSimpleName();
	}
	
	public void contextConnect() {
		String aux = "";
		for (int i=0; i<fields.size(); i++) {
			if (this.fields.get(i)[2] != null) {
				aux += "if (riid.equals(\""+fields.get(i)[1]+"\")){ return this."+fields.get(i)[0]+".connectToRecp(pSinkIntf, riid, provConnID); }\n		";				
			}
		}
		aux += "return false;";
		vc.put("connect", aux);
	}
	
	public void contextDisconnect() {
		String aux = "";
		for (int i=0; i<fields.size(); i++) {
			if (this.fields.get(i)[2] != null) {
				aux += "if (riid.equals(\""+fields.get(i)[1]+"\")){ return this."+fields.get(i)[0]+".disconnectFromRecp(connID); }\n		";				
			}
		}
		aux += "return false;";
		vc.put("disconnect", aux);
	}
	
	public void contextPackage() {
		String aux = this.clazz.getPackage().toString();
		if(aux.length()>0) {
			StringBuilder stringBuilder = new StringBuilder(aux);
			stringBuilder.insert(8, "specific.");
			vc.put("packageName", stringBuilder.toString());
		}
	}

	public ArrayList<String[]> getFields() {
		ArrayList<String[]> fieldsList = new ArrayList<String[]>();
		Field[] field = clazz.getDeclaredFields();
		for (Field f : field) {
			String[] fieldReturn = new String[4];
			//campo[0] = Nome da variável
			fieldReturn[0] = f.getName();
			String nomeType = f.getType().getCanonicalName().toString();
			//campo[1] = Nome do tipo da variável
			fieldReturn[1] = nomeType;
			//campo[3] = Modificador de visibilidade
			fieldReturn[3] = f.toString().substring(0, f.toString().indexOf(" "));
			Annotation[] anot = f.getAnnotations();
			for (Annotation an : anot) {
				if (an instanceof Requires) {
					Requires requires = (Requires)an;
					//campo[2] = Nome da interface da anotação
					fieldReturn[2] = requires.name();
				}
			}
			fieldsList.add(fieldReturn);
		}
		return fieldsList;
	}
	
	public void contextFields() {
		ArrayList<String> fieldsOpenCOM = new ArrayList<String>();
		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i)[2] != null) {
				fieldsOpenCOM.add(this.fields.get(i)[3]+" OCM_SingleReceptacle<"+this.fields.get(i)[1]+"> "+this.fields.get(i)[0]+";");				
			}
			else {
				fieldsOpenCOM.add(this.fields.get(i)[3]+" "+this.fields.get(i)[1]+" "+this.fields.get(i)[0]+";");
			}
		}
		vc.put("campos", fieldsOpenCOM);
	}
	
	public void contextConstructor() {
		ArrayList<String> fieldsConstructorOpenCOM = new ArrayList<String>();
		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i)[2] != null) {
				fieldsConstructorOpenCOM.add("this."+this.fields.get(i)[0]+" = new OCM_SingleReceptacle<"+this.fields.get(i)[1]+">("+this.fields.get(i)[1]+".class);");
			}
		}
		vc.put("construtor", fieldsConstructorOpenCOM);
	}
	
	public String getImplements() {
		String signature = "";
		Type[] interfaces = this.clazz.getGenericInterfaces();
		for (int i = 0; i < interfaces.length; i++) {			
			signature += ", " + interfaces[i].toString();
		}
		return signature.replace(" interface ", " ");
	}
	
	public ArrayList<String> getMethods() {
		ArrayList<String> listMethods = new ArrayList<String>();
		Method[] methods = this.clazz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (!methods[i].getName().contains("QueryInterface")) {
				listMethods.add(methods[i].getReturnType().getSimpleName()+" "+methods[i].getName());
			}
		}
		return listMethods;
	}
	
	public void contextClassBody() throws IOException {
		String name = getFile();
		fr = new FileReader (name);
		in = new BufferedReader (fr);
		String line;
		ArrayList<String> methods = getMethods();
		ArrayList<String> bodyLines = new ArrayList<String>();
		//boolean iscorpo = false;
		String aux = "";
		int openedBraceCounter = 0;
		int closedBraceCounter = 0;
		
		while ( (line=in.readLine())!= null) {
			
			for (int i = 0; i < methods.size(); i++) {
				if (line.contains(methods.get(i))) {
					aux += line+"\n";
					if (line.contains("{")) {
						openedBraceCounter+=line.replaceAll("[^{]", "").length();						
					}
					if (line.contains("}")) {
						closedBraceCounter+=line.replaceAll("[^}]", "").length();
					}
					while ( (openedBraceCounter != closedBraceCounter)){
						//!(line=in.readLine()).contains("}") &&
						line=in.readLine();
						for (int j = 0; j < fields.size(); j++) {
							if (this.fields.get(j)[2] != null) {
								if (line.matches("(.*[^a-zA-Z0-9]+|^)"+fields.get(j)[0]+"\\..*")) {
									line = line.replace(fields.get(j)[0]+".", fields.get(j)[0]+".m_pIntf.");
								}
							}
							//line =  line.replace(campos.get(j)[0]+".", campos.get(j)[0]+".m_pIntf.");
						}
						if (line.contains("{")) {
							openedBraceCounter+=line.replaceAll("[^{]", "").length();
						}
						if (line.contains("}")) {
							closedBraceCounter+=line.replaceAll("[^}]", "").length();
						}
						
						aux += line+"\n";
						
					}
					//aux += "	}";
					bodyLines.add(aux);
					aux = "";
					openedBraceCounter = 0;
					closedBraceCounter = 0;
				}
			}
			
			
			
//			if (iscorpo){
//				linhascorpo.add(line);
//			}
//			if (line.contains("public class")){
//				iscorpo = true;
//			}
			
		}
		
		this.vc.put("metodos", bodyLines);
		
		
//		in.close ();
//		fr.close();
//
//		for (int i=linhascorpo.size()-1; i>0; i = i-1){
//			if (linhascorpo.get(i).contains("}")){
//				linhascorpo.remove(i);
//				break;
//			}
//		}	
	}
	
	private String getFile() {
		String value = System.getProperty("user.dir").toString() + "\\generic\\";
		value = value + clazz.getSimpleName() + ".java";
		return value;
	}

	public ArrayList<String> getImports() throws IOException {
		String name = getFile();		
		fr = new FileReader (name);
		in = new BufferedReader (fr);
		String line;
		ArrayList<String> importLines = new ArrayList<String>();
		while ( (line=in.readLine())!= null){
			if (line.contains("import ")){
				importLines.add(line);
			}   	  
		}
		in.close ();
		fr.close();
		return importLines;
	}
}