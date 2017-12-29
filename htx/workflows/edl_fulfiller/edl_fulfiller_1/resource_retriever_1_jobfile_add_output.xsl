<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2016-2017 Stephan Kreutzer

This file is part of edl_fulfiller_1 workflow, a submodule of the
digital_publishing_workflow_tools package.

edl_fulfiller_1 workflow is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

edl_fulfiller_1 workflow is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with edl_fulfiller_1 workflow. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="UTF-8"/>

  <xsl:template match="/resource-retriever-1-workflow-job/resources">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()|text()"/>
    </xsl:copy>
    <output-directory path="./resources"/>
  </xsl:template>

  <xsl:template match="@*|node()|text()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()|text()"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
