package comanche.fraclet;

import java.io.Serializable;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	public String message;
	public byte[] data;
}