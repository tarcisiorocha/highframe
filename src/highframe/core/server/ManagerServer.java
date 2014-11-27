package highframe.core.server;

import highframe.core.manager.Distributor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import utils.CustomClassLoader;
import zip.Zipper;

@Path("/hfserver")
public class ManagerServer {
	
	@PUT
    @Consumes("application/octet-stream")
    public void putBookmark(byte[] ab)  {
		long ini = System.currentTimeMillis();
		this.writeToDiskAndUnzip(ab);
		long end = System.currentTimeMillis();
		System.out.println("Tempo HIGHFRAME SERVER: "+(end-ini));
	}
	
	public void writeToDiskAndUnzip(byte[] ab) {
		File dirReceived = new File("received\\fromguitool");
		dirReceived.mkdirs();
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		File zipFile = new File("received\\fromguitool\\plan_"+time+".zip");
	    FileOutputStream fos;
	    try {
			fos = new FileOutputStream(zipFile);
			fos.write(ab);
		    fos.flush();
		    fos.close();
		    System.out.println(date.toString() + " - O arquivo foi enviado através do método PUT com sucesso!!!");
		    Zipper zipper = new Zipper();
		    File dirUnzip = new File("received\\fromguitool");
		    CustomClassLoader.addPath("received\\fromguitool");
		    dirUnzip.mkdirs();
		    zipper.unzip(zipFile, dirUnzip);
		    zipFile.delete();
		    
		    Distributor d = new Distributor();
			d.sendAll("received/fromguitool/plan.xml", "received/fromguitool/data.xml");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
