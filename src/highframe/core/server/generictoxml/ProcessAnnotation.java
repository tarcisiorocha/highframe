package highframe.core.server.generictoxml;

import java.lang.reflect.Field;
import java.util.List;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

public final class ProcessAnnotation {
    
    private final List<Class<?>> classList;

    public ProcessAnnotation(List<Class<?>> classList) {
        this.classList = classList;
    }
    
    /**
     * Preenche a lista passada com os Componentes Genéricos extraídos das classes.
     * @param componentGenericList 
     */
    public void process(List<ComponentGeneric> componentGenericList){
        
        for (Class<?> clazz : classList) {
            //!
            if (!clazz.isAnnotationPresent(Component.class)) continue;
            ComponentGeneric componentGeneric = new ComponentGeneric();
            Component component = clazz.getAnnotation(Component.class);
            componentGeneric.setName(isStringEmpty(component.name()) ? clazz.getName(): component.name());

            preencherInterfaces(componentGeneric, component.provides(), clazz.getDeclaredFields());

            componentGenericList.add(componentGeneric);
        }
    }
    
    private void preencherInterfaces(ComponentGeneric componentGeneric, 
            Interface[] providers, Field[] requires ){
        
        preecherInterfacesProvidas(providers, componentGeneric);

        preencherInterfacesRequeridas(requires, componentGeneric);
    }

    private static void preecherInterfacesProvidas(Interface[] interfaces, 
            ComponentGeneric componentGeneric){
        
        for (Interface interface1 : interfaces){
            ComponentInterface cInterface = new ComponentInterface();
            cInterface.setName(interface1.name());
            cInterface.setSignature(interface1.signature());
            cInterface.setProvideRequire(ProvideRequireEnum.P);
            componentGeneric.addInterface(cInterface);
        }
    }
	
    private static void preencherInterfacesRequeridas(Field[] fields, 
            ComponentGeneric componentGeneric){
        
        for (Field field : fields ){
            if (!field.isAnnotationPresent(Requires.class)) continue;
            Requires requires = field.getAnnotation(Requires.class);
            ComponentInterface cInterface = new ComponentInterface();
            cInterface.setName(requires.name());
            cInterface.setSignature(field.getType());
            cInterface.setProvideRequire(ProvideRequireEnum.R);
            componentGeneric.addInterface(cInterface);
        }
    }
    
    private boolean isStringEmpty(String str){
        return str == null || str.trim().equals("");
    }

}
