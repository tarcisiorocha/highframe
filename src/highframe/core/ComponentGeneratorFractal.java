package highframe.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class ComponentGeneratorFractal implements IComponentGenerator {
	
	@Override
	public void generateComponent(Class<?> classe) {
		try {			
			//Classe criada para processar anotações e classe .java
			ClassReaderFractal pc = new ClassReaderFractal(classe);
			
			//Inicializa o velocity  
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			//Cria o contexto velocity que liga o java ao template  
			VelocityContext context = new VelocityContext(pc.getContext());

			//Escolhendo o template  
			Template t = ve.getTemplate("templates/templateFractal.vm");
			
			//Método que retorna a assinatura a ser utilizada no component
			String nomeClasse = pc.getClassName();
			context.put("nomeclasse", nomeClasse); 
			String interfaces = pc.getImplements();
			context.put("implements", interfaces);  
			ArrayList<String> corpo;
			ArrayList<String> pacotes;
			corpo = pc.getClassBody();
			context.put("conteudo", corpo); 
			pacotes = pc.getImports();
			context.put("imports", pacotes);
			
			//Mistura o contexto com o template e escreve em um arquivo em disco.
			StringWriter writer = new StringWriter();
			File file = new File("generated");
			file.mkdirs();
			FileWriter f = new FileWriter(new File("generated/"+nomeClasse+".java"));
			t.merge(context, writer);
			f.write(writer.toString());
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}