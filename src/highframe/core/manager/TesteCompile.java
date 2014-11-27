package highframe.core.manager;

import java.io.File;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class TesteCompile {
	public static void main(String[] args) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null,	null, null);		
		
		Iterable<? extends JavaFileObject> units = manager.getJavaFileObjectsFromStrings(Arrays.asList("generated//ClientImpl.java", "generated//ServerImpl.java"));
		File dir = new File("compiled\\fractal");
		dir.mkdirs();
		String[] opts = new String[] { "-d", "compiled\\fractal" };
		CompilationTask task = compiler.getTask(null, manager, null, Arrays.asList(opts), null, units);
		task.call();
	}

}
