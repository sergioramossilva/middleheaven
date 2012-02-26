/**
 * 
 */
package org.middleheaven.web.server.action;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.middleheaven.process.web.server.action.PathMatcher;

/**
 * 
 */
public class PathMatcherTest {


	@Test
	public void testNormal() {

		PathMatcher m = PathMatcher.newInstance("/results/*");

		assertTrue(m.match("/results/actor/"));

		Map<String, String> result = m.parse("/results/actor/");

		assertEquals(0, result.size());


		m = PathMatcher.newInstance("/results/*/actors");

		assertTrue(m.match("/results/23/actors"));
		assertFalse(m.match("/results/actors"));

		m = PathMatcher.newInstance("/*/actors");

		assertTrue(m.match("/23/actors"));
		assertTrue(m.match("/results/actors"));

	}

	@Test
	public void testPathMatcher() {


		PathMatcher m = PathMatcher.newInstance("/results/{id}/actor/{name}");

		Map<String, String> result = m.parse("/results/23/actor/batman");

		assertEquals(2, result.size());

		assertEquals("23", result.get("id"));
		assertEquals("batman", result.get("name"));


	}

	@Test
	public void testUrlRead() {


		PathMatcher m = PathMatcher.newInstance("/{head}/{category}/{mnemonic}.html");

		assertTrue(m.match("/academy/java-platform/os-alicerces-da-plataforma-java.html"));
		assertFalse(m.match("academy/java-platform/os-alicerces-da-plataforma-java.html"));
		assertFalse(m.match("/academy/java-platform"));
		assertFalse(m.match("/academy/"));


		Map<String, String> result = m.parse("/academy/java-platform/os-alicerces-da-plataforma-java.html");

		assertEquals(3, result.size());

		assertEquals("academy", result.get("head"));
		assertEquals("java-platform", result.get("category"));
		assertEquals("os-alicerces-da-plataforma-java", result.get("mnemonic"));


	}

	@Test
	public void testUrlReadStar() {


		PathMatcher m = PathMatcher.newInstance("architecture/*");

		assertTrue(m.match("architecture/configurations/arquitetura-com-domainstore-repositorio-e-query-object"));
		assertFalse(m.match("architecture"));

		m = PathMatcher.newInstance("architecture");

		assertFalse(m.match("architecture/configurations/arquitetura-com-domainstore-repositorio-e-query-object"));
		assertTrue(m.match("architecture"));

		m = PathMatcher.newInstance("architecture/*/{key}");

		assertTrue(m.match("architecture/configurations/arquitetura-com-domainstore-repositorio-e-query-object"));
		assertFalse(m.match("architecture"));

		Map<String, String> result = m.parse("architecture/configurations/arquitetura-com-domainstore-repositorio-e-query-object");
	
		assertEquals(1, result.size());
		assertTrue(result.containsKey("key"));
		assertEquals("arquitetura-com-domainstore-repositorio-e-query-object", result.get("key"));
	}
}
