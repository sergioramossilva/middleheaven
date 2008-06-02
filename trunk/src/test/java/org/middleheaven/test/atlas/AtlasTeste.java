package org.middleheaven.test.atlas;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.Country;
import org.middleheaven.global.atlas.modules.DefaultAtlasModule;
import org.middleheaven.global.atlas.modules.ISOFileAtlasModule;


public class AtlasTeste {

	@Test
	public void testDefaultAtlasModule(){
		
		final List<Country> all = new LinkedList<Country>();
		AtlasContext context = new AtlasContext(){

			@Override
			public void addCountry(Country country, Date definitionTimeStamp) {
				all.add(country);
			}};
		
		DefaultAtlasModule m = new DefaultAtlasModule();
		m.loadAtlas(context);
		
		assertEquals(Locale.getISOCountries().length,  all.size());
	}
	
	@Test
	public void testISOFileAtlasModule(){
		
		final Map<String,Country> all = new TreeMap<String, Country>();
		AtlasContext context = new AtlasContext(){

			@Override
			public void addCountry(Country country, Date definitionTimeStamp) {
				all.put(country.getDesignation(), country);
			}};
		
		ISOFileAtlasModule m = new ISOFileAtlasModule();
		m.loadAtlas(context);
		
		assertEquals(246,  all.size());
		
	
		Country brazil = all.get("BR");
		assertEquals(27, brazil.getChildren().size());
		
		assertEquals(263,brazil.getChild("MG").getChildren().size());
		
		Country portugal = all.get("PT");
		assertEquals(20, portugal.getChildren().size());
		
		assertEquals(39,portugal.getChild("01").getChildren().size());
		
		assertTrue(portugal.getCurrentCurrency().toString().equals("EUR"));
		assertTrue(brazil.getCurrentCurrency().toString().equals("BRL"));
		
		assertTrue(portugal.getLanguage().equals("pt"));
		assertTrue(brazil.getLanguage().equals("pt"));
	}
	
	@Test
	public void testISOFileAtlasModlueLoadFromPath(){
		URL url = this.getClass().getResource(".");
		System.setProperty("middleheaven.atlas.data.path" , new File("./src/test/java/org/middleheaven/test/atlas").getAbsolutePath());
		
		final Map<String,Country> all = new TreeMap<String, Country>();
		AtlasContext context = new AtlasContext(){

			@Override
			public void addCountry(Country country, Date definitionTimeStamp) {
				all.put(country.getDesignation(), country);
			}};
		
		ISOFileAtlasModule m = new ISOFileAtlasModule();
		m.loadAtlas(context);
		
		assertEquals(246,  all.size());
		
		// No file present but loads from embedded
		Country portugal = all.get("PT");
		assertEquals(20, portugal.getChildren().size());
		
		Country brazil = all.get("BR");
		assertEquals(27, brazil.getChildren().size());
		
		// file present at filsystem location with a dummy state to make 58
		// 57 means the data from the embeded version is being red witch is an error
		Country usa = all.get("US");
		assertEquals(59, usa.getChildren().size());
		
	
		
		System.setProperty("middleheaven.atlas.data.path", "");
	}
}
