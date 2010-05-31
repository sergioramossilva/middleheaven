package org.middleheaven.web.container;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContainer;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.bootstrap.EditableContainerFileSystem;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;

/**
 * 
 */
public abstract class WebContainer implements BootstrapContainer  {

	private ServletContext context;
	private EditableContainerFileSystem fileSystem;
	
	public WebContainer(ServletContext context) {
		this.context = context;
	}
	
	public void configurate(BootstrapContext context) {
		//no-op
	}
	
	public ServletContext getServletContext(){
		return this.context;
	}

	@Override
	public final ContainerFileSystem getFileSystem() {
		if (this.fileSystem==null){
			fileSystem = new EditableContainerFileSystem();
			
			setupDefaultFilesRepositories(context,fileSystem);

			String fileMapping = this.context.getInitParameter("filesMapping");
			if(fileMapping!=null){
				ManagedFile file = fileSystem.getEnvironmentConfigRepository().retrive(fileMapping);
				if(file.exists()){
					setupConfigurationForFilesRepositories(file,fileSystem);
				}
			} 
			
			
		}
		
		return fileSystem;
	}
	
	protected void setupConfigurationForFilesRepositories(ManagedFile configFile, EditableContainerFileSystem fileSystem){

		Properties params = new Properties();
		try {
			params.load(configFile.getContent().getInputStream());

			final File root = new File(context.getRealPath("."));


			fileSystem.setEnvironmentConfigRepository(resolveFile("environmentConfig" , root , params , fileSystem.getEnvironmentConfigRepository()));
			fileSystem.setEnvironmentDataRepository(resolveFile("environmentData" , root , params , fileSystem.getEnvironmentDataRepository()));

			fileSystem.setAppConfigRepository(resolveFile("appConfig" , root , params , fileSystem.getAppConfigRepository()));
			fileSystem.setAppDataRepository(resolveFile("appData" , root , params ,fileSystem.getAppDataRepository()));
			fileSystem.setAppLogRepository(resolveFile("appLog" , root , params , fileSystem.getAppLogRepository()));
			fileSystem.setAppClasspathRepository(resolveFile("appClasspath" , root , params ,fileSystem.getAppClasspathRepository()));


		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}

	
	protected void setupDefaultFilesRepositories(ServletContext context, EditableContainerFileSystem fileSystem ){


		fileSystem.setEnvironmentConfigRepository(ManagedFiles.resolveFile(new File(context.getRealPath("./../conf"))));
		fileSystem.setAppDataRepository(ManagedFiles.resolveFile(new File(context.getRealPath("./../data"))).createFolder());
		fileSystem.setAppConfigRepository(ManagedFiles.resolveFile(new File(context.getRealPath("./META-INF"))));
		
		ManagedFile root = ManagedFiles.resolveFile(new File(context.getRealPath(".")));
		fileSystem.setAppRootRepository(root);
		
		fileSystem.setAppDataRepository(fileSystem.getEnvironmentDataRepository().retrive(root.getPath().getBaseName()).createFolder());
		fileSystem.setAppLogRepository(fileSystem.getAppDataRepository().retrive("log").createFolder());
		
		fileSystem.setAppClasspathRepository(ManagedFiles.resolveFile(new File(context.getRealPath("./WEB-INF/classes"))));
		

	}
	
	
	private ManagedFile resolveFile(String name, File root, Properties params, ManagedFile defaultLocation) {
		String path = (String)params.get(name);
		if(path == null || path.trim().isEmpty()){
			return defaultLocation;
		}

		if (path.startsWith(".")){
			return ManagedFiles.resolveFile(new File(root, path)).createFolder();
		} else {
			return ManagedFiles.resolveFile(new File(path)).createFolder();
		}
	}

	

	@Override
	public void start() {
		// no-op
	}

	@Override
	public void stop(){
		this.context = null; 
	}

}
