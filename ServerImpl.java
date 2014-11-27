package br.teste;

import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import br.teste.Service;

@Component(provides = @Interface(name = "s", signature = br.teste.Service.class))
public class ServerImpl implements Service {
	
	//@Attribute(value="->")
	private String header = "EXECUTANDO ";
	
	public ServerImpl() {
		System.err.println("SERVER created");
	}

	public void print(final String msg) {
		System.out.println(header + msg);
	}
}
