package org.middleheaven.core.reflection.inspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.EnhancedArrayList;
import org.middleheaven.util.collections.EnhancedCollection;
import org.middleheaven.util.collections.TransformedCollection;

public class PackageIntrospector extends Introspector {

	private Package typePackage;

	public static PackageIntrospector of (Package typePackage){
		return new PackageIntrospector(typePackage);
	}
	
	
	PackageIntrospector(Package typePackage) {
		this.typePackage = typePackage;
	}

	public String getName(){
		return typePackage.getName();
	}

	public EnhancedCollection<PackageIntrospector> getSubpackages(){
		final String name = this.getName();
		
		final Package[] packages = Package.getPackages();
		EnhancedCollection<PackageIntrospector> all = new EnhancedArrayList<PackageIntrospector>();
		
		for (Package p : packages){
			// if the lenght is the same is the same package. discar that
			if (p.getName().startsWith(name) && p.getName().length() != name.length() ){
				all.add(of(p));
			}
		}
		
		return all;
	}

	public EnhancedCollection<ClassIntrospector> getClassesIntrospectors(){
		return CollectionUtils.enhance(
				TransformedCollection.transform( getPackageClasses(typePackage) , 
						new Classifier< ClassIntrospector,Class<?>>(){

							@Override
							public ClassIntrospector classify(Class<?> obj) {
								return Introspector.of(obj);
							}
						}
				)
		);
	}

	public EnhancedCollection<Class<?>> getClasses(){
		return CollectionUtils.enhance(getPackageClasses(typePackage));
	}
	
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return this.typePackage.getAnnotation(annotationClass);
	}

	@Override
	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return this.typePackage.isAnnotationPresent(annotationClass);
	}
	
	
	/**
	 * Obtains all classes in the package.
	 * 
	 * @param classPackage the package where to look.
	 * @return the collection of all packages.
	 */
	public static Set<Class<?>> getPackageClasses(Package classPackage) {

		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			ClassLoader cl = classPackage.getClass().getClassLoader();
			if (cl == null) {
				// no class loader specified -> use thread context class loader
				cl = Thread.currentThread().getContextClassLoader();
			}
			String packageUrl = classPackage.getName().replaceAll("\\.", "/");
			Enumeration<URL> packageLocations = cl.getResources(packageUrl);

			while (packageLocations.hasMoreElements()){
				URL url = packageLocations.nextElement();
				process(classes,url,classPackage.getName());
			}

			return classes;
		} catch (IOException e) {
			throw new ReflectionException(e);
		}
	}

    private static void process(Set<Class<?>> classes , URL url , String packageName) throws IOException {
        if (url.getProtocol().equals("file")) {
            try {
                File folder = new File(url.toURI());

                File[] files = folder.listFiles(new FilenameFilter() {

                    public boolean accept(File file, String name) {
                        return name.indexOf("$") < 0 && name.endsWith(".class");
                    }

                });

                for (File f : files) {
                    String name = packageName
                    + "."
                    + f.getName().substring(0, f.getName().indexOf('.'));
                    classes.add(ClassIntrospector.loadFrom(name).getIntrospected());
                }
            } catch (URISyntaxException e) {
                throw new ReflectionException(e);
            }
        } else if (url.getProtocol().equals("jar")) {

            int pos = url.getFile().indexOf('!');

            extractClasseInJarPackage(
                    classes ,
                    url.getFile().substring(0, pos),
                    packageName);
        } else {
            throw new ReflectionException(
                    "Cannot find classes in package using protocol " + url.getProtocol()
            );
        }
    }

    private static void extractClasseInJarPackage(Set<Class<?>> classes, String jarName, String packageName)
        throws IOException {

        packageName = packageName.replaceAll("\\." , "/");
        if (jarName.startsWith("file:")) {
            jarName = jarName.substring("file:".length());
        }

        JarInputStream jarFile = new JarInputStream(new FileInputStream(new File(jarName)));
        JarEntry jarEntry;

        while (true) {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            if (jarEntry.getName().startsWith(packageName)
                    && jarEntry.getName().endsWith(".class")) {

                String className = jarEntry.getName().replaceAll("/", "\\.");
                className = className.substring(0, className.length() - ".class".length());

                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    // no-op
                    continue;
                }
            }
        }


    }

	/**
	 * @return
	 */
	public Package getIntrospected() {
		return this.typePackage;
	}
}
