package zip;

import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Zipper zh = new Zipper();
		zh.unzip(new File("Esquemas XML.zip"), new File("TESTEZIP"));
	}

}
