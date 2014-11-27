package highframe.core.manager;

public class ExtractMain {
	public static void main(String[] args) {
		ExtractSubXml esx = new ExtractSubXml();
		esx.extract("Data.xml", "generated");
	}
}
