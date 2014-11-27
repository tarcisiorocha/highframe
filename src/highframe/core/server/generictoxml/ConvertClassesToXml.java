package highframe.core.server.generictoxml;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import zip.Zipper;

public final class ConvertClassesToXml {
    
    /**
     * Filtro para selecionar todas as classes .java
     */
    private final FileFilter javaFileFilter = new FileFilter() {		
        @Override
        public boolean accept(File pathname) {
                return pathname.getName().endsWith(".java");
        }
    };
    
    /**
     * Filtro para selecionar todas as classes compiladas .class
     */
    private final FileFilter classFileFilter = new FileFilter() {		
        @Override
        public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class");
        }
    };    
    
    /**
     * 
     * @param sourcePath : Caminho do diretorio que será lido os arquivos .java
     * @param destinationPath : Caminho do diretorio que será guardado os arquivos
     *                          .xml
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public void startConvert(String sourcePath, String destinationPath) 
            throws MalformedURLException, ClassNotFoundException, IOException {
        File sourceDirectory = new File(sourcePath);
        File destinationDirectory = new File(destinationPath);
        
        File[] javaFiles = sourceDirectory.listFiles(javaFileFilter);
        compileFiles(javaFiles);
        
        //File[] classFiles = sourceDirectory.listFiles(classFileFilter);
        
        List<File> files = buscaRecursiva2(sourceDirectory, "class");
        
        String[] vetorFiles = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {			
			vetorFiles[i] = files.get(i).getPath().replace(sourceDirectory.getName()+"\\", "").replace(".class", "").replace("\\", ".");
			
		}
        
        List<Class<?>> classList = getClassesLoaded(sourceDirectory, vetorFiles);
        
        ProcessAnnotation process = new ProcessAnnotation(classList);
        
        List<ComponentGeneric> componentGenericList = new ArrayList<>();
        
        process.process(componentGenericList);
        
        ClassesConverter converter = new ClassesConverter(destinationDirectory, componentGenericList);
        
        converter.convert();
        
        //deleteFiles(vetorFiles);
        
        File dirXml = new File("generic/xml");
		File[] filesXml = dirXml.listFiles();
		
		Zipper zip = new Zipper();
		File zipFile = new File("generic/components.zip");
		zip.zip(filesXml, zipFile);
        
    }
    
    public List<File> buscaRecursiva(File pasta, String ext) {
        List<File> resultados = new ArrayList<File>();
        for (File f : pasta.listFiles()) {
           if (f.isDirectory()) {
               resultados.addAll(buscaRecursiva(f, ext));
           } else if (f.getName().endsWith(ext)) {
               resultados.add(f);
           }
        }
        return resultados;
    }
    
    public List<File> buscaRecursiva2(File pasta, String ext) {
        List<File> resultados = new ArrayList<File>();
        for (File f : pasta.listFiles()) {
           if (f.isDirectory()) {
               resultados.addAll(buscaRecursiva(f, ext));
           } else if (f.getName().endsWith(ext)) {
               resultados.add(f);
           }
        }
        
       
        
        return resultados;
    }
    
    private void deleteFiles(File[] files){
        for (File file : files){
            file.delete();
        }
    }
    
//    private void compileFiles(File[] files) throws FileNotFoundException, IOException {
//        
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
//        
//        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
//        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();      
//        
//        fileManager.close();   
//    }
    
    public void compileFiles(File[] files) {
    	//System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.7.0_25");
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,	null, null);
		File dirGenerated = new File("generic");
		ArrayList<File> filesToCompile = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			if(files[i].getName().contains(".java")) {
				filesToCompile.add(files[i]);
			}
		}
		
		//Arrays.asList("generated//ClientImpl.java", "generated//ServerImpl.java");
		Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromFiles(filesToCompile);
		//File dir = new File("compiled\\fractal");
		//dir.mkdirs();
		String[] opts = new String[] { "-d", "generic" };
		CompilationTask task = compiler.getTask(null, manager, null, Arrays.asList(opts), null, units);
		task.call();
	}
    
    private List<Class<?>> getClassesLoaded(File sourceDirectory, String[] classFileList) 
            throws MalformedURLException, ClassNotFoundException{
        
        List<Class<?>> lista = new ArrayList<>();
        URL[] arrayUrl = new URL[]{sourceDirectory.toURI().toURL()};
        
        ClassLoader classLoader = new URLClassLoader(arrayUrl);
        
        for (String file : classFileList) {
            Class<?> clazz = (Class<?>) classLoader.loadClass(file);
            lista.add(clazz);
        }
        return lista;
    }

    private String getFileNameWithoutExtension(File file) {
        return file.getName().split("\\.")[0];
    }    
    
}
