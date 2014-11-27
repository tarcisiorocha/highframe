package br.teste;

import br.teste.Service;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;
import org.objectweb.fractal.api.control.BindingController;

public class ClientImpl implements BindingController , java.lang.Runnable {


	public ClientImpl () {
		System.err.println("CLIENT created");
	}

	@Requires(name = "s")
	private Service service;
	
	@Requires(name = "s2")
	private Service service2;
	
	@Requires(name = "s3")
	private Service service3;

	public void run() {
		service.print("Hello World !");
	}
	
	public String[] listFc() {
		return new String[] { "s, s2, s3" };
	}

	public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("s")) { return service; }
		if (clientItfName.equals("s2")) { return service2; }
		if (clientItfName.equals("s3")) { return service3; }
		return null;
	}

	public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("s")) { service = (Service)serverItf; }
		if (clientItfName.equals("s2")) { service2 = (Service)serverItf; }
		if (clientItfName.equals("s3")) { service3 = (Service)serverItf; }
		
	}

	public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("s")) { service = null; }
		if (clientItfName.equals("s2")) { service2 = null; }
		if (clientItfName.equals("s3")) { service3 = null; }
		
	}
}