package comanche;

import static java.util.logging.Logger.getLogger;
import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.api.control.BindingController;

public class BasicLogger implements BindingController , comanche.Logger {

    private final java.util.logging.Logger log = getLogger("comanche");
    /* (non-Javadoc)
     * @see comanche.Logger#log(java.lang.String)
     */
    public void log(String msg) {
        log.info(msg);
    }
	
	public String[] listFc() {
		return new String[] { "null" };
	}

	public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
		if (clientItfName.equals("null")) { return log; }
		return null;
	}

	public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("null")) { log = (Logger)serverItf; }
		
	}

	public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		if (clientItfName.equals("null")) { log = null; }
		
	}
}