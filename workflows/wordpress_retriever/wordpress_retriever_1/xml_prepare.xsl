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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/array">
    <xsl:comment> This file was generated by xml_prepare.xsl of wordpress_retriever_1 workflow, a submodule of the digital_publishing_workflow_tools package, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). </xsl:comment><xsl:text>&#xA;</xsl:text>
    <wordpress-posts>
      <xsl:apply-templates select="/array/object"/>
    </wordpress-posts>
  </xsl:template>

  <xsl:template match="/array/object">
    <wordpress-post>
      <title><xsl:value-of select="./object[@name='title']/object[@name='rendered']//text()"/></title>
      <content><xsl:value-of select="./object[@name='content']/object[@name='rendered']//text()"/></content>
    </wordpress-post>
  </xsl:template>

  <xsl:template match="text()|@*"/>

</xsl:stylesheet>
