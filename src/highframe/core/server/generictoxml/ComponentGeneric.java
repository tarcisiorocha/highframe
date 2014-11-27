package highframe.core.server.generictoxml;
import java.util.ArrayList;
import java.util.List;

public final class ComponentGeneric {
    
    private String name;
    private List<ComponentInterface> interfaces = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<ComponentInterface> getInterfaces(){
        return this.interfaces;
    }
    
    public void setInterfaces(List<ComponentInterface> interfaces){
        this.interfaces = interfaces;
    }
    
    public void addInterface(ComponentInterface cI){
        interfaces.add(cI);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
