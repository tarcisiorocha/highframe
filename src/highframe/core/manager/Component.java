package highframe.core.manager;

import java.util.List;

public class Component {
	
	public String componentName;
	public String componentId;
	public List<Interface> interfaces;	
		
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public List<Interface> getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(List<Interface> interfaces) {
		this.interfaces = interfaces;
	}
	
	@Override
	public String toString() {
		return "Componente [nome=" + componentName + ", id=" + componentId
				+ ", interfaces=" + interfaces + "]";
	}
}
