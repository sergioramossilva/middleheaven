package org.middleheaven.process.web;



/**
 * A {@link UrlPattern} determines if a Url matches a certain criteria. 
 *  
 *
 */
public abstract class UrlPattern {

	public static <P extends UrlPattern>  P maxOf(String path, Iterable<? extends P> mappings){
		P selected= null;
		double max =0;
		
		for (P mapping : mappings){
			
			double m = mapping.match(path);
			
			if (Double.compare(m, max) > 0){
				m = max;
				selected = mapping;
			}
		}
		
		return selected;
	}
	
	public static UrlPattern notMatch(final UrlPattern mapping){
		return new UrlPattern(){

			@Override
			public double match(String url) {
				return 1 - mapping.match(url);
			}
			
		};
	
	}

	public static UrlPattern matchSimplePattern(final String pattern){
		return new GlobUrlPattern(pattern);
	}
	
	public static UrlPattern matchAll(){
		return new UrlPattern(){

			@Override
			public double match(String url) {
				return 1;
			}
			
		};
	}

	public abstract double match (String url);
}
