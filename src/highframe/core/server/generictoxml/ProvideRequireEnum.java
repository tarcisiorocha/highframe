package highframe.core.server.generictoxml;
public enum ProvideRequireEnum {
    
    R("Required"), P("Provided");
    
    private final String label;
    
    ProvideRequireEnum(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
