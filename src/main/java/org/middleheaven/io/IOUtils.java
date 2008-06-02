/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.middleheaven.io.repository.FileNotFoundManagedException;

public final class IOUtils {

	IOUtils(){}

	private static void doStreamCopy(InputStream in,OutputStream out) throws IOException{

		byte[] buffer = new byte[1024 * 4]; //4 Kb
		int n = 0;
		while (-1 != (n = in.read(buffer))) {
			out.write(buffer, 0, n);
		}
		out.flush();
		
		out.close();
		in.close();
	}

	private static void doCopyFile(FileInputStream in , FileOutputStream out) throws IOException{
		FileChannel inChannel = null, outChannel = null;

		try {

			inChannel = in.getChannel();
			outChannel = out.getChannel();

			inChannel.transferTo(0, inChannel.size(), outChannel);

		} finally {
			if (inChannel != null)  inChannel.close();
			if (outChannel != null) outChannel.close();
		}

	}

	/**
	 * The copy does not closes the steams.
	 * @param in
	 * @param out
	 */
	public static void copy(InputStream in,OutputStream out) throws IOException{
		if (in==null || out ==null){
			throw new IllegalArgumentException("Cannot copy a non existent stream");
		}

		if (in instanceof FileInputStream && out instanceof FileOutputStream){
			doCopyFile((FileInputStream)in,(FileOutputStream)out);
		} else {
			doStreamCopy(in,out);
		}

	}

	public static void move(File in,File out) throws IOException{
		if (!in.isFile()){
			throw new IllegalArgumentException("Can only move a file");
		}
		// if out is a directory, create a folder with the same name
		if (out.isDirectory()){
			out = new File (out.getAbsolutePath() + "/" + in.getName());
			out.mkdirs();
		}
		copy(in,out);
		in.delete();
	}

	public static void copy(File in,File out) throws IOException{
		FileInputStream fis =null;
		FileOutputStream fos =null;

		try {
			fis  =new FileInputStream(in);
		} catch (FileNotFoundException e){
			throw new FileNotFoundManagedException(in);
		}

		try {
			fos = new FileOutputStream(out);
		} catch (FileNotFoundException e){
			throw new FileNotFoundManagedException(out);
		}

		doCopyFile(fis,fos);

	}
}
