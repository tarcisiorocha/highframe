package testes.auxiliares;

public class TesteMatches {
	public static void main(String[] args) {
		String line = ".service.print(\"Hello World !\");";
		//line = "serviceprint(\"Hello World !\");";
		boolean teste = line.matches("(.*[^a-zA-Z0-9]+|^)service\\..*");
		System.out.println(teste);
		if (teste) {
			line = line.replace("service.", "service.m_pIntf.");
		}
		System.out.println(line);
	}

}
