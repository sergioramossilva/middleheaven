package org.middleheaven.web.processing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.middleheaven.global.Culture;


public class TestHttpProcessingUtils {

	
	@Test
	public void testProcesingUtils(){
		
		
		List<Culture> cultures = HttpProcessingUtils.acceptableLanguages("da , pt, en ; q=0.7, en-gb ; q=0.8");
		
		assertEquals(4, cultures.size());
		
		assertEquals(0,cultures.indexOf(Culture.valueOf("da")));
		assertEquals(1,cultures.indexOf(Culture.valueOf("pt")));
		assertEquals(2,cultures.indexOf(Culture.valueOf("en", "GB")));
		assertEquals(3,cultures.indexOf(Culture.valueOf("en")));
		
	}
}
