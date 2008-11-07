<?xml version="1.0" encoding="ISO-8859-1" ?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
	xmlns:err="http://www.w3.org/2005/xqt-errors"
	exclude-result-prefixes="xs xdt err fn">

	<xsl:output method="xml"  indent="yes"/>
	
	<xsl:template match="@*|node()">
	   <xsl:copy>
	      <xsl:apply-templates select="@*|node()"/>
	   </xsl:copy>
	</xsl:template>


	<xsl:template match="source">
			<pre name="source" class="java" >
		       <xsl:apply-templates/>
			</pre>
	</xsl:template>
	

	<xsl:template match="estrang">
			<i><xsl:apply-templates/></i>
	</xsl:template>
	
	<xsl:template match="figure">
		<center>
			<xsl:apply-templates/>
		</center>
	</xsl:template>
</xsl:stylesheet>
