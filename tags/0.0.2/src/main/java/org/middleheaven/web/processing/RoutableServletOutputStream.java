package org.middleheaven.web.processing;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

public class RoutableServletOutputStream extends ServletOutputStream  {
	
	     private ServletOutputStream  destination;
	     private DestinationFactory factory;
	
	     /**
	      * Factory to lazily instantiate the destination.
	      */
	     public static interface DestinationFactory {
	         ServletOutputStream  create() throws IOException ;
	     }
	
	     public RoutableServletOutputStream(DestinationFactory factory) {
	         this.factory = factory;
	     }
	
	     private ServletOutputStream  getDestination() throws IOException  {
	         if (destination == null) {
	             destination = factory.create();
	         }
	         return destination;
	     }
	
	     public void updateDestination(DestinationFactory factory) {
	         destination = null;
	         this.factory = factory;
	     }
	
	     public void close() throws IOException  {
	         getDestination().close();
	     }
	
	     public void write(int b) throws IOException  {
	         getDestination().write(b);
	     }
	
	     public void print(String  s) throws IOException  {
	         getDestination().print(s);
	     }
	
	     public void print(boolean b) throws IOException  {
	         getDestination().print(b);
	     }
	
	     public void print(char c) throws IOException  {
	         getDestination().print(c);
	     }
	
	     public void print(int i) throws IOException  {
	         getDestination().print(i);
	     }
	
	     public void print(long l) throws IOException  {
	         getDestination().print(l);
	     }
	
	     public void print(float v) throws IOException  {
	         getDestination().print(v);
	     }
	
	     public void print(double v) throws IOException  {
	         getDestination().print(v);
	     }
	
	     public void println() throws IOException  {
	         getDestination().println();
	     }
	
	     public void println(String  s) throws IOException  {
	         getDestination().println(s);
	     }
	
	     public void println(boolean b) throws IOException  {
	         getDestination().println(b);
	     }
	
	     public void println(char c) throws IOException  {
	         getDestination().println(c);
	     }
	
	     public void println(int i) throws IOException  {
	         getDestination().println(i);
	     }
	
	     public void println(long l) throws IOException  {
	         getDestination().println(l);
	     }
	
	     public void println(float v) throws IOException  {
	         getDestination().println(v);
	     }
	
	     public void println(double v) throws IOException  {
	         getDestination().println(v);
	     }
	
	     public void write(byte[] b) throws IOException  {
	         getDestination().write(b);
	     }
	
	     public void write(byte[] b, int off, int len) throws IOException  {
	         getDestination().write(b, off, len);
	     }
	
	     public void flush() throws IOException  {
	         getDestination().flush();
	     }
	 }
