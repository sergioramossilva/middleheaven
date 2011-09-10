/**
 * 
 */
package org.middleheaven.process.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.global.Culture;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.io.repository.MediaManagedFileContent;
import org.middleheaven.io.repository.StreamBasedMediaManagedFileContent;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.web.HttpCookieReader;
import org.middleheaven.process.web.HttpCookieWriter;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.HttpStatusCode;

/**
 * 
 */
class ServletBasedHttpServerResponse implements HttpServerResponse {

	private HttpServletResponse response;

	/**
	 * Constructor.
	 * @param servletResponse
	 */
	public ServletBasedHttpServerResponse(HttpServletResponse servletResponse) {
		this.response = servletResponse;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEntry(HttpEntry entry) {
		// TODO Auto-generated method stub
		
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCulture(Culture culture) {
		response.setLocale(culture.toLocale());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCommitted() {
		return response.isCommitted();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		response.reset();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendError(HttpStatusCode sc, String msg)
			throws HttpProcessIOException {
		try {
			response.sendError(sc.intValue(), msg);
		} catch (IOException e) {
			throw new HttpProcessIOException(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendError(HttpStatusCode sc) throws HttpProcessIOException {
		try {
			response.sendError(sc.intValue());
		} catch (IOException e) {
			throw new HttpProcessIOException(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStatus(HttpStatusCode sc) {
		response.setStatus(sc.intValue());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpCookieWriter getHttpCookieWriter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void renameTo(String newName) {
		((HttpServletResponse) response).setHeader("Content-disposition", "attachment; filename=".concat(newName));
	}
	
	public class ResponseHttpEntry implements HttpEntry {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getContentEncoding() {
			return response.getCharacterEncoding();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRepeatable() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStreaming() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isChunked() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public MediaManagedFileContent getContent() {
			return new MediaManagedFileContent(){
				boolean streamIsOpen = false;
				String name = "";
				
				public String getName(){
					return name;
				}
			
				@Override
				public InputStream getInputStream() {
					throw new UnsupportedOperationException("Response Virtual File as no input stream associated");
				}

				@Override
				public OutputStream getOutputStream()  {
					try {
						 OutputStream out = response.getOutputStream();
						 streamIsOpen = true;
						 return out;
					} catch (IOException e) {
						throw ManagedIOException.manage(e);
					} 
				}



				@Override
				public boolean setSize(long expectedSize)  {
					if (streamIsOpen){
						throw new ManagedIOException("Configuration must be made before opening streams");
					}
					response.setContentLength((int)expectedSize);
					return true;
				}

				@Override
				public void setContentType(String mimeContentType){
					if (streamIsOpen){
						throw new ManagedIOException("Configuration must be made before opening streams");
					}
					response.setContentType(mimeContentType);
				}

				@Override
				public String getContentType()  {
					throw new UnsupportedOperationException("Content type cannot be read for this file type");
				}

				@Override
				public long getSize() {
					return response.getBufferSize();
				}
			};
		}
		
	};

}
