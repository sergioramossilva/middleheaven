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
import org.middleheaven.io.repository.machine.MachineFiles;

/**
 * 
 */
public abstract class WebContainer implements BootstrapContainer  {

	private ServletContext context;
	private EditableContainerFileSystem fileSystem;
	private ManagedFile root;
	
	public WebContainer(ServletContext context, ManagedFile root) {
		this.context = context;
		this.root = root;
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

			fileSystem.setEnvironmentConfigRepository(resolveFile("environmentConfig", params , fileSystem.getEnvironmentConfigRepository()));
			fileSystem.setEnvironmentDataRepository(resolveFile("environmentData" , params , fileSystem.getEnvironmentDataRepository()));

			fileSystem.setAppConfigRepository(resolveFile("appConfig" , params , fileSystem.getAppConfigRepository()));
			fileSystem.setAppDataRepository(resolveFile("appData"  , params ,fileSystem.getAppDataRepository()));
			fileSystem.setAppLogRepository(resolveFile("appLog" , params , fileSystem.getAppLogRepository()));
			fileSystem.setAppClasspathRepository(resolveFile("appClasspath" , params ,fileSystem.getAppClasspathRepository()));


		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}

	
	protected void setupDefaultFilesRepositories(ServletContext context, EditableContainerFileSystem fileSystem ){


		fileSystem.setAppRootRepository(root);
		
		fileSystem.setEnvironmentConfigRepository(root.retrive("./../conf"));
		
		fileSystem.setEnvironmentDataRepository(
				root.retrive("./../data").createFolder()
		);
		
		fileSystem.setAppConfigRepository(root.retrive(("./META-INF")));
		fileSystem.setAppDataRepository( // TODO must contain appID
				root.retrive("./../data").createFolder()
		);
		
		// TODO must contain appID
		fileSystem.setAppLogRepository(fileSystem.getAppDataRepository().retrive("log").createFolder());
		
		fileSystem.setAppClasspathRepository(root.retrive("./WEB-INF/classes"));
		

	}
	
	
	private ManagedFile resolveFile(String name, Properties params, ManagedFile defaultLocation) {
		String path = (String)params.get(name);
		if(path == null || path.trim().isEmpty()){
			return defaultLocation;
		}

		if (path.startsWith(".")){
			return root.retrive(path).createFolder();
		} else {
			// TODO handle withou depending on File 
			return MachineFiles.resolveFile(new File(path)).createFolder();
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
	

	public abstract WebContainerInfo getWebContainerInfo();
		


}
