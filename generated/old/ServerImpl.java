package br.teste;

import br.teste.Service;
import org.objectweb.fractal.fraclet.annotations.Attribute;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.api.control.BindingController;

public class ServerImpl implements BindingController , br.teste.Service {

	
	@Attribute(value="->")
	private String header;
	
	public ServerImpl() {
		System.err.println("SERVER created");
	}

	public void print(final String msg) {
		System.out.println(header + msg);
	}
	
	public String[] listFc() {
		return new String[] { "null" };
	}

	public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("null")) { return header; }
		return null;
	}

	public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("null")) { header = (String)serverItf; }
		
	}

	public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("null")) { header = null; }
		
	}
}