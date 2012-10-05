/**
 * 
 */
package org.middleheaven.global;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.global.text.LocalizationDomainBundle;

/**
 * 
 */
public class  CultureModelLocalizationService implements LocalizationService{

	LocalizationDomainBundle masterBundle;

	private Collection<CultureModelFactory> factories = new LinkedList<CultureModelFactory>();
	
	private Set<Culture> supportedCultures = new HashSet<Culture>();
	

	public CultureModelLocalizationService (){
		
	}
	
	public CultureModel getCultureModel(Culture culture) {

		for (CultureModelFactory factory : factories){
			if (factory.getSupportedCultures().contains(culture)){
				return factory.resolveModelFor(culture);
			}
		}

		return new StandardCultureModel(culture);
	}

	public void registerCultureModelFactory(CultureModelFactory cultureModelFactory) {
		factories.add(cultureModelFactory);
		
		supportedCultures.addAll(cultureModelFactory.getSupportedCultures());
	}


	public String getMessage(TextLocalizable label, boolean asMnemonic) {
		return getMessage(label, this.getExecutionEnvironmentCulture()); 
	}

	@Override
	public String getMessage(TextLocalizable localResource, Culture culture) {
		return  localResource.isLocalized() ? localResource.toString() :  masterBundle.localizeLabel(localResource, culture);
	}

	@Override
	public Culture getExecutionEnvironmentCulture() {
		return Culture.defaultValue();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Culture> getSupportedCultures() {
		return Collections.unmodifiableCollection(supportedCultures);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerLocalizationDomainBundle(LocalizationDomainBundle bundle) {
		this.masterBundle = bundle;
	}

}