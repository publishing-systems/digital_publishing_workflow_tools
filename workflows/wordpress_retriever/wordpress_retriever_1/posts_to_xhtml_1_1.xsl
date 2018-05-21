<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2018 Stephan Kreutzer

This file is part of wordpress_retriever_1 workflow, a submodule of the
digital_publishing_workflow_tools package.

wordpress_retriever_1 workflow is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

wordpress_retriever_1 workflow is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with wordpress_retriever_1 workflow. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"/>

  <xsl:template match="/wordpress">
    <html version="-//W3C//DTD XHTML 1.1//EN" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="en" lang="en">
      <head>
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
        <xsl:comment> This file was created by posts_to_xhtml_1_1.xsl of wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see http://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). </xsl:comment>
        <title>TODO</title>
      </head>
      <body>
        <h1>TODO</h1>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post">
    <div>
      <h2><xsl:value-of select="./title//text()"/></h2>
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content">
    <xsl:choose>
      <xsl:when test="/wordpress/wordpress-posts/wordpress-post/content/p">
        <xsl:apply-templates/>
      </xsl:when>
      <xsl:otherwise>
        <p>
          <xsl:apply-templates/>
        </p>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content/p">
    <p>
      <xsl:apply-templates/>
    </p>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//text()">
    <xsl:value-of select="."/>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//a">
    <a href="{@href}"><xsl:apply-templates/></a>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//code">
    <code><xsl:apply-templates/></code>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//pre">
    <pre><xsl:apply-templates/></pre>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//ul">
    <ul><xsl:apply-templates/></ul>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//ol">
    <ol><xsl:apply-templates/></ol>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//ul/li |
                       /wordpress/wordpress-posts/wordpress-post/content//ol/li">
    <li><xsl:apply-templates/></li>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//dl">
    <dl><xsl:apply-templates/></dl>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//dl/dt">
    <dt><xsl:apply-templates/></dt>
  </xsl:template>

  <xsl:template match="/wordpress/wordpress-posts/wordpress-post/content//dl/dd">
    <dd><xsl:apply-templates/></dd>
  </xsl:template>

  <xsl:template match="node()|@*|text()"/>

</xsl:stylesheet>
