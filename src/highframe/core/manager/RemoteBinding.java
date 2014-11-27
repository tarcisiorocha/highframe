package highframe.core.manager;

public class RemoteBinding {
	private String idExportedClientInterface;
	private String idExportedServerInterface;
	private String bindingType;
	
	public String getIdExportedClientInterface() {
		return idExportedClientInterface;
	}
	public void setIdExportedClientInterface(String idExportedClientInterface) {
		this.idExportedClientInterface = idExportedClientInterface;
	}
	public String getIdExportedServerInterface() {
		return idExportedServerInterface;
	}
	public void setIdExportedServerInterface(String idExportedServerInterface) {
		this.idExportedServerInterface = idExportedServerInterface;
	}
	public String getBindingType() {
		return bindingType;
	}
	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}
	
	
}