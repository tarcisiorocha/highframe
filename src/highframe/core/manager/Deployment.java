package highframe.core.manager;

public class Deployment {
	private String idsubarchitecture;
	private String componentModel;
	private String host;
	
	public String getIdsubarchitecture() {
		return idsubarchitecture;
	}
	public void setIdsubarchitecture(String idsubarchitecture) {
		this.idsubarchitecture = idsubarchitecture;
	}
	public String getComponentModel() {
		return componentModel;
	}
	public void setComponentModel(String componentModel) {
		this.componentModel = componentModel;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public Class<?> getGeneratorClass() {
		Class<?> clazz;
		try {
			clazz = Class.forName("highframe.core.ComponentGenerator"+componentModel);
			return clazz;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}