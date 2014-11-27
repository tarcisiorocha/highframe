package highframe.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.velocity.VelocityContext;
import org.objectweb.fractal.fraclet.annotations.Requires;

public class ClassReaderFractal {

	private Class<?> clazz;
	private FileReader fr;
	private BufferedReader in;
	private VelocityContext vc;
	private ArrayList<String[]> fields;

	public ClassReaderFractal(Class<?> clazz) {
		this.clazz = clazz;
		this.vc = new VelocityContext();
		this.fields = this.getFields();
		this.contextListFc();
		this.contextLookupFc();
		this.contextBindFc();
		this.contextUnbindFc();
		this.contextPackage();
	}
	
	public VelocityContext getContext() {
		return this.vc;
	}

	public String getClassName() {
		return clazz.getSimpleName();
	}
	
	public void contextListFc() {
		String aux = "";
		for (int i=0; i<fields.size(); i++) {
			aux += ", \""+fields.get(i)[2]+"\"";
		}
		aux = aux.replaceFirst(", ", "");
		vc.put("listFc", aux);
	}
	
	public void contextLookupFc() {
		String aux = "";
		for (int i=0; i<fields.size(); i++) {
			aux += "if (clientItfName.equals(\""+fields.get(i)[2]+"\")) { return "+fields.get(i)[0]+"; }\n		";
		}
		aux += "return null;";
		vc.put("lookupFc", aux);
	}
	
	public void contextBindFc() {
		String aux = "";
		for (int i=0; i<fields.size(); i++) {
			aux += "if (clientItfName.equals(\""+fields.get(i)[2]+"\")) { "+fields.get(i)[0]+" = ("+fields.get(i)[1]+")serverItf; }\n		";
		}
		vc.put("bindFc", aux);
	}
	
	public void contextUnbindFc() {
		String aux = "";
		for (int i=0; i<fields.size(); i++) {
			aux += "if (clientItfName.equals(\""+fields.get(i)[2]+"\")) { "+fields.get(i)[0]+" = null; }\n		";
		}
		vc.put("unbindFc", aux);
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
			String[] fieldReturn = new String[3];
			//campo[0] = Nome da variável
			fieldReturn[0] = f.getName();
			String nomeType = f.getType().toString();
			//campo[1] = Nome do tipo da variável
			fieldReturn[1] = nomeType.substring(nomeType.lastIndexOf(".")+1);
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
	
	public String getImplements() {
		String signature = "";
		Type[] interfaces = this.clazz.getGenericInterfaces();
		for (int i = 0; i < interfaces.length; i++) {			
			signature += ", " + interfaces[i].toString();
		}
		return signature.replace(" interface ", " ");
	}

	public ArrayList<String> getClassBody() throws IOException {
		String name =  getFile();		
		fr = new FileReader (name);
		in = new BufferedReader (fr);
		String line;
		ArrayList<String> bodyLines = new ArrayList<String>();
		boolean isCorpo = false;
		while ( (line=in.readLine())!= null){
			if (isCorpo){
				bodyLines.add(line);
			}
			if (line.contains("public class")){
				isCorpo = true;
			}  
		}
		in.close ();
		fr.close();

		for (int i=bodyLines.size()-1; i>0; i = i-1){
			if (bodyLines.get(i).contains("}")){
				bodyLines.remove(i);
				break;
			}
		}

		return bodyLines;		
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