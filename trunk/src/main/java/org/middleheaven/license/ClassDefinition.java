package org.middleheaven.license;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.middleheaven.crypto.CryptoUtils;
import org.middleheaven.io.IOUtils;

public class ClassDefinition {

	
	public ClassDefinition (){
		
	}
	
	public void write (OutputStream out , String className, InputStream classDefStream) throws IOException{
		ByteArrayOutputStream a = new ByteArrayOutputStream();
		IOUtils.copy(classDefStream, a);
		a.close();
		
		DataOutputStream da = new DataOutputStream(out);
		da.writeUTF(className);
		da.writeUTF(new String(CryptoUtils.encodeBase64(a.toByteArray())));
	}
	
	public String read (InputStream in , OutputStream classDefStream) throws IOException{
		
		DataInputStream d = new DataInputStream(in);
		String name = d.readUTF();
		String code = d.readUTF();
		
		byte[] def = CryptoUtils.decodeBase64(code.getBytes());
		
		classDefStream.write(def);
		
		return name;
	}
}
