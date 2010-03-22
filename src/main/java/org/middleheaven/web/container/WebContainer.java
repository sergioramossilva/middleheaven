package org.middleheaven.web.container;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.middleheaven.core.BootstrapContainer;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFiles;

/**
 * 
 */
public abstract class WebContainer implements BootstrapContainer  {

	private ServletContext context;
	private ManagedFile appConfigRepository;
	private ManagedFile appDataRepository;
	private ManagedFile appLogRepository;
	private ManagedFile appClasspathRepository;

	private ManagedFile environmentConfigRepository;
	private ManagedFile environmentDataRepository;

	public WebContainer(ServletContext context) {
		this.context = context;

		setupDefaultFilesRepositories(context);

		String fileMapping = this.context.getInitParameter("filesMapping");
		if(fileMapping!=null){
			ManagedFile file = environmentConfigRepository.retrive(fileMapping);
			if(file.exists()){
				setupConfigurationForFilesRepositories(file);
			}
		} 
	}

	public ServletContext getServletContext(){
		return this.context;
	}

	public void init(WiringService wiringService){
		//no-op
	}

	protected void setupConfigurationForFilesRepositories(ManagedFile file){

		Properties params = new Properties();
		try {
			params.load(file.getContent().getInputStream());

			final File root = new File(context.getRealPath("."));

			environmentConfigRepository = resolveFile("environmentConfig" , root , params , environmentConfigRepository);
			environmentDataRepository = resolveFile("environmentData" , root , params , environmentDataRepository);

			appConfigRepository = resolveFile("appConfig" , root , params , appConfigRepository);
			appDataRepository = resolveFile("appData" , root , params , appDataRepository);
			appLogRepository = resolveFile("appLog" , root , params , appLogRepository);
			appClasspathRepository = resolveFile("appClasspath" , root , params , appClasspathRepository);


		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
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

	protected void setupDefaultFilesRepositories(ServletContext context){

		if(environmentConfigRepository==null){
			environmentConfigRepository = ManagedFiles.resolveFile(new File(context.getRealPath("./../conf")));
		}

		if (environmentDataRepository==null){
			environmentDataRepository = ManagedFiles.resolveFile(new File(context.getRealPath("./../data"))).createFolder();
		}

		if(appConfigRepository==null){
			appConfigRepository = ManagedFiles.resolveFile(new File(context.getRealPath("./META-INF")));
		}

		ManagedFile root = getAppRootRepository();

		if(appDataRepository == null){
			appDataRepository = environmentDataRepository.retrive(root.getName()).createFolder();
		}

		if(appLogRepository == null){
			appLogRepository = appDataRepository.retrive("log").createFolder();
		}

		if(appClasspathRepository == null){
			appClasspathRepository = ManagedFiles.resolveFile(new File(context.getRealPath("./WEB-INF/classes")));
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

	public ManagedFile getAppRootRepository() {
		return ManagedFiles.resolveFile(new File(context.getRealPath(".")));
	}
	
	/**
	 * @return <code>ManagedFileRepository</code> representing the Repository where application configuration files are stored. 
	 * Normally this points to the META-INF folder for war applications
	 */
	public ManagedFile getAppConfigRepository() {
		return appConfigRepository;
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the Repository where environment configuration files are stored. 
	 * Normally this points to the WEB-INF folder for war applications
	 */
	@Override
	public ManagedFile getEnvironmentConfigRepository() {
		return environmentConfigRepository;
	}

	@Override
	public ManagedFile getEnvironmentDataRepository() {
		return environmentDataRepository;
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the repository where the application can store data
	 * This defaults to the application`s root web folder. Application are encouraged to use specific folders inside the root
	 */
	public ManagedFile getAppDataRepository() {
		return appDataRepository;
	}

	/**
	 * @return <code>ManagedFileRepository</code> representing the repository where the application can store their logs
	 * This defaults to the application`s root web folder. Application are encouraged to use specific folders inside the root
	 */
	public ManagedFile getAppLogRepository() {
		return appLogRepository;
	}

	public ManagedFile getAppClasspathRepository() {
		return appClasspathRepository;
	}

	protected void setContext(ServletContext context) {
		this.context = context;
	}

	protected void setAppConfigRepository(ManagedFile appConfigRepository) {
		this.appConfigRepository = appConfigRepository;
	}

	protected void setAppDataRepository(ManagedFile appDataRepository) {
		this.appDataRepository = appDataRepository;
	}

	protected void setAppLogRepository(ManagedFile appLogRepository) {
		this.appLogRepository = appLogRepository;
	}

	protected void setAppClasspathRepository(ManagedFile appClasspathRepository) {
		this.appClasspathRepository = appClasspathRepository;
	}

	protected void setEnvironmentConfigRepository(
			ManagedFile environmentConfigRepository) {
		this.environmentConfigRepository = environmentConfigRepository;
	}

	protected void setEnvironmentDataRepository(
			ManagedFile environmentDataRepository) {
		this.environmentDataRepository = environmentDataRepository;
	}



}
