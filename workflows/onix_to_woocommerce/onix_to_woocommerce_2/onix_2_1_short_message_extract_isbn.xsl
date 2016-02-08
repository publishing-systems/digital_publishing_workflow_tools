<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2016 Stephan Kreutzer

This file is part of onix_to_woocommerce_2 workflow, a submodule of the
digital_publishing_workflow_tools package.

onix_to_woocommerce_2 workflow is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

onix_to_woocommerce_2 workflow is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with onix_to_woocommerce_2 workflow. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" encoding="UTF-8" media-type="text/plain"/>

  <xsl:template match="ONIXmessage/product">
    <xsl:choose>
      <xsl:when test="/ONIXmessage/product/productidentifier[./b221/text() = '15']/b244">
        <!-- ISBN-13 -->
        <xsl:value-of select="/ONIXmessage/product/productidentifier[./b221/text() = '15']/b244//text()"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="text()|@*"/>

</xsl:stylesheet>
