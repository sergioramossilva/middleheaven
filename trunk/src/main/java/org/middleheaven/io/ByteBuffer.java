package org.middleheaven.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteBuffer {

	private byte[] buffer = new byte[0];
	private final Object lock = new Object();
	private BufferByteArrayOutputStream output;
	
	public InputStream getInputStream(){
		byte[] bufferLocal;
		synchronized (lock){ 
			bufferLocal= buffer;
		}
		return new ByteArrayInputStream(bufferLocal);
	}
	
	public OutputStream getOutputStream(){
		if (output == null){
			output = new BufferByteArrayOutputStream();
		}
		return output;
	}
	
	public byte[] getBytes(){
		return buffer;
	}
	
	public int length(){
		return buffer.length;
	}
	
	
	private class BufferByteArrayOutputStream extends ByteArrayOutputStream{

		public void close() throws IOException{
			super.close();
			synchronized (lock){ 
				buffer = this.toByteArray();
				output = null;
			}
			
		}
	}


	public long getSize() {
		return this.buffer.length;
	}

	public void setSize(int size) {
		byte[] newStream = new byte[size];
		System.arraycopy(buffer, 0, newStream, 0, buffer.length);
		synchronized (lock){ 
			buffer = newStream;
		}
	}
}
