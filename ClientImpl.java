package br.teste;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;
import br.teste.Service;

@Component(provides=@Interface(name="r", signature=java.lang.Runnable.class))
public class ClientImpl implements Runnable {

	public ClientImpl () {
		System.err.println("CLIENT created");
	}

	@Requires(name = "s")
	private Service service;

	public void run() {
		service.print("Hello World !");
	}
}
