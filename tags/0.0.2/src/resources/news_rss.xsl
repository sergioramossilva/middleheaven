<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
	xmlns:err="http://www.w3.org/2005/xqt-errors"
	exclude-result-prefixes="xs xdt err fn">

	<xsl:output method="xml"  indent="yes"/>
	
	<xsl:template match="*|text()|node()">
		<xsl:copy>
		       <xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="news">
		<feed xmlns="http://www.w3.org/2005/Atom" >	
	
				<author>
				<name>MiddleHeaven Project</name>
				<uri>http://middleheaven.sourceforge.net/</uri>
				</author>
			 	
			 	<id>http://middleheaven.sourceforge.net/</id>
			 	<title>MiddleHeaven Project - News Feed</title>
				<link href="http://middleheaven.sourceforge.net" />
				<updated>2006-01-05 3:05:47</updated> 
				
			 	<xsl:apply-templates select="entry"/>

		</feed>
	</xsl:template>
	
	<xsl:template match="entry">
			<entry>
				<title> <xsl:value-of value="@name"/></title>
				<link href="http://middleheaven.sourceforge.net/news.rss#46"/>
				<id>http://middleheaven.sourceforge.net/news.rss</id>
				<updated></updated>
				<summary><xsl:value-of value="p"/></summary>
			</entry>
	</xsl:template>
	
</xsl:stylesheet>
