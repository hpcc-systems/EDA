package org.hpccsystems.pentaho.job.eclpluginception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFolder {
	public void copy(File src, File dest) throws IOException {
		  if (src.isDirectory()) {
			  System.out.println("yes");  
		    //if directory not exists, create it
		    if (!dest.exists()) {
		      dest.mkdir();
		    }
		    //list all the directory contents
		    String files[] = src.list();
		    for (String file : files) {
		      //construct the src and dest file structure
		      File srcFile = new File(src, file);
		      //System.out.println(src.getName()); 
		      File destFile = new File(dest, file);//+"\\"+src.getName()
		      //recursive copy
		      copy(srcFile,destFile);
		    }
		  } else {
		    //if file, then copy it
		    //Use bytes stream to support all file types
		    InputStream in = new FileInputStream(src);
		    OutputStream out = new FileOutputStream(dest); 
		    byte[] buffer = new byte[1024]; 
		    int length;
		    //copy the file content in bytes 
		    while ((length = in.read(buffer)) > 0){
		      out.write(buffer, 0, length);
		    }
		    in.close();
		    out.close();
		    //System.out.println("File copied from " + src + " to " + dest);
		  }
		}
}
