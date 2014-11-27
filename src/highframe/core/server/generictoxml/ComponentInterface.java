package highframe.core.server.generictoxml;
public final class ComponentInterface {
    
    private String name;
    private Class<?> signature;
    private ProvideRequireEnum provideRequire;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Class<?> getSignature() {
        return signature;
    }
    
    public void setSignature(Class<?> signature) {
        this.signature = signature;
    }
    
    public ProvideRequireEnum getProvideRequire() {
        return provideRequire;
    }
    
    public void setProvideRequire(ProvideRequireEnum provideRequire) {
        this.provideRequire = provideRequire;
    }    
}
