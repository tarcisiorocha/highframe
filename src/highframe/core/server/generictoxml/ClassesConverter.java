package highframe.core.server.generictoxml;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public final class ClassesConverter {

    private final XMLOutputter xmlOutput = new XMLOutputter();
    private final List<ComponentGeneric> cGenericList; 
    private final File diretorio;

    /**
     * 
     * @param destinationDirectory : Diretorio de saida dos arquivos xml
     * @param cGenericList : lista de componentes que será convertida
     */
    public ClassesConverter(File destinationDirectory, List<ComponentGeneric> cGenericList){        
        this.diretorio = destinationDirectory;
        this.cGenericList = cGenericList;
    }
    
    public void convert(){
        for (ComponentGeneric componentGeneric : cGenericList){
            convertToXml(componentGeneric);
        }
    }

    private void convertToXml(ComponentGeneric cGeneric){
        String fileName = cGeneric.getName().concat(".xml");
        File file = new File(diretorio, fileName);		
        try {
            Element componentElement = new Element("component");
            componentElement.setAttribute(new Attribute("name", cGeneric.getName()));
            Document document = new Document(componentElement);

            Element interfaces = new Element("interfaces");
            for (ComponentInterface cInterface : cGeneric.getInterfaces()){
                Element interface_ = new Element("interface");
                interface_.setAttribute("name", cInterface.getName());
                interface_.setAttribute("signature", cInterface.getSignature().getName());
                interface_.setAttribute("type", cInterface.getProvideRequire().name());
                interfaces.addContent(interface_);
            }

            document.getRootElement().addContent(interfaces);

            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileWriter(file));

          } catch (IOException io) {
              io.printStackTrace();
          }
    }
   
}
