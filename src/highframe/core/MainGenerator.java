package highframe.core;

public class MainGenerator {
	public static void main(String[] args) throws ClassNotFoundException {
//		String a = "Os nomes das classes em Java precisam come�ar com letra e depois  desta quaisquer combina��es de } letras e d�gitos.";
//		if (a.contains("}")) {
//			int contador = a.split("}").length-1;
//			System.out.println(contador);
//			System.out.println(a);
//			
//		}
		ComponentGeneratorOpenCOM co = new ComponentGeneratorOpenCOM();
		co.generateComponent(Class.forName("comanche.fraclet.Scheduler"));
		
//		 String x = "Os nomes das classes em Java precisam come�ar com letra e depois desta }quaisquer combina��es }de letras e d�gitos.";     
//         
//		    int total = x.replaceAll("[^}]", "").length();  
//		    System.out.println(total); 
	}

}
