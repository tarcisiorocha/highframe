package highframe.core.manager;

import java.io.File;

public class TesteManager {
	public static void main(String[] args) {
//		File f = new File("Data.xml");
//		Manager m = new Manager();
//		m.readXMLNew(f);
//		m.listarComponentes();
		//m.generateComponents();
		
		Distributor d = new Distributor();
		//d.send("Plan.xml", "Data.xml", "localhost");
		//d.readPlanXml("Plan.xml");
		d.sendAll("received/fromguitool/plan.xml", "received/fromguitool/data.xml");
	}
}
