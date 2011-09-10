package org.middleheaven.web.rendering;

public interface RenderingProcessorResolver {

	public RenderingProcessor resolve(String url,String contentType);

	public boolean canProcess(String url, String contentType);
}
