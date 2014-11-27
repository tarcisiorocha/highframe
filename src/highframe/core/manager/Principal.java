package highframe.core.manager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Principal {

	public static void main(String[] args) throws JDOMException, IOException {

		File f = new File("Dados.xml");

		//Criamos uma classe SAXBuilder que vai processar o XML4  
		SAXBuilder sb = new SAXBuilder();  

		//Este documento agora possui toda a estrutura do arquivo.  
		Document d = (Document) sb.build(f);  

		//Recuperamos o elemento root  
		Element mural = d.getRootElement();

		//Recuperamos os elementos filhos (children)  
		List<Element> elements = mural.getChildren();  
		Iterator<Element> i = elements.iterator();  		  
		//Iteramos com os elementos filhos, e filhos do dos filhos  
		while (i.hasNext()) {  
			Element element = (Element) i.next();  
			if(element.getName().equals("no")){
				System.out.println("=============== Nó ===============");
				System.out.println("Endereco:"+ element.getAttributeValue("endereco"));  
				System.out.println("Porta:"+ element.getAttributeValue("porta"));  

				List<Element> componentes =  element.getChildren();
				if (!componentes.isEmpty()){
					System.out.println("*************** Componentes **************");
					Iterator<Element> j = componentes.iterator();		  
					while (j.hasNext()) {
						Element componente = (Element) j.next();
						System.out.println("***Nome:"+ componente.getAttributeValue("nome"));  
						System.out.println("***Modelo:"+ componente.getAttributeValue("modelo")); 
						List<Element> interfaces =  componente.getChildren();
						System.out.println("::::::::::::::: Interfaces :::::::::::::::");
						Iterator<Element> k = interfaces.iterator();
						while (k.hasNext()) {
							Element interfaceimp = (Element) k.next();
							System.out.println("::::::Nome:"+ interfaceimp.getAttributeValue("nome"));  
							System.out.println("::::::Assinatura:"+ interfaceimp.getAttributeValue("assinatura")); 

						}
					}
				}
			} else if (element.getName().equals("conecte")) {
				System.out.println("=============== Conexões ===============");
				System.out.println("Nó Cliente:"+ element.getAttributeValue("nocliente"));
				System.out.println("Nó Servidor:"+ element.getAttributeValue("noservidor")); 
				System.out.println("Porta:"+ element.getAttributeValue("porta")); 
				System.out.println("Conexão:"+ element.getAttributeValue("conexao")); 
				System.out.println("Cliente:"+ element.getAttributeValue("componentecliente")); 
				System.out.println("Servidor:"+ element.getAttributeValue("componenteservidor")); 
			}
		}
	}  

}
