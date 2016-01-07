<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2016 Stephan Kreutzer

This file is part of onix_to_woocommerce_1, a submodule of the
digital_publishing_workflow_tools package.

onix_to_woocommerce_1 is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

onix_to_woocommerce_1 is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with onix_to_woocommerce_1. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" encoding="UTF-8" media-type="text/plain"/>

  <xsl:template match="ONIXmessage">
    <xsl:text>{</xsl:text>
      <xsl:apply-templates/>
    <xsl:text>}</xsl:text>
  </xsl:template>

  <xsl:template match="/ONIXmessage/product">
    <xsl:choose>
      <xsl:when test="count(./contributor/b035[text() = 'A01']) &lt; 1">
        <xsl:text>Error!</xsl:text>
      </xsl:when>
      <xsl:when test="not(count(./title[./b202/text() = '01']) = 1)">
        <xsl:text>Error!</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>"product":</xsl:text>
        <xsl:text>{</xsl:text>
          <xsl:text>"title":"</xsl:text>
            <xsl:for-each select="/ONIXmessage/product/contributor[./b035/text() = 'A01']">
              <xsl:if test="not(position() = 1)">
                <xsl:text>, </xsl:text>
              </xsl:if>
              <xsl:value-of select="./b036//text()"/>
            </xsl:for-each>
            <xsl:text>: »</xsl:text>
              <xsl:value-of select="/ONIXmessage/product/title[./b202/text() = '01']/b203//text()"/>
            <xsl:text>«",</xsl:text>
          <xsl:text>"type":"variable",</xsl:text>
          <xsl:text>"sold_individually":true,</xsl:text>
          <xsl:text>"visible":true,</xsl:text>
          <xsl:text>"catalog_visibility":"visible",</xsl:text>
          <xsl:text>"description":"</xsl:text>
            <!-- Translator(s): contributor, b035: B06, b252, b036. -->
            <xsl:apply-templates select="/ONIXmessage/product/othertext[./d102/text() = '01']/d104" mode="copyXhtmlContent"/>
          <xsl:text>",</xsl:text>
          <xsl:text>"sku":"</xsl:text>
          <xsl:choose>
            <xsl:when test="/ONIXmessage/product/productidentifier[./b221/text() = '15']/b244">
              <!-- ISBN-13 -->
              <xsl:value-of select="/ONIXmessage/product/productidentifier[./b221/text() = '15']/b244//text()"/>
            </xsl:when>
          </xsl:choose>
          <xsl:text>",</xsl:text>
          <xsl:text>"images":</xsl:text>
          <xsl:text>[</xsl:text>
            <xsl:apply-templates select="/ONIXmessage/product/mediafile[./f114/text() = '04' and ./f116/text() = '06']"/>
          <xsl:text>],</xsl:text>
          <xsl:if test="/ONIXmessage/product/mediafile[./f114/text() = '04' and ./f116/text() = '06']">
            <xsl:text>"featured_src":"https:\/\/example.com\/wp-content\/uploads\/</xsl:text>
            <xsl:value-of select="/ONIXmessage/product/mediafile[./f114/text() = '04' and ./f116/text() = '06'][1]/f117//text()"/>
            <xsl:text>",</xsl:text>
          </xsl:if>
          <xsl:text>"categories":</xsl:text>
          <xsl:text>[</xsl:text>
            <xsl:apply-templates select="/ONIXmessage/product/publisher[./b291/text() = '01']/b081"/>
          <xsl:text>]</xsl:text>
        <xsl:text>}</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="/ONIXmessage/product/publisher[./b291/text() = '01']/b081">
    <xsl:if test="not(position() = 1)">
      <xsl:text>,</xsl:text>
    </xsl:if>
    <xsl:text>"</xsl:text>
      <xsl:value-of select="/ONIXmessage/product/publisher/b081"/>
    <xsl:text>"</xsl:text>
  </xsl:template>

  <xsl:template match="/ONIXmessage/product/mediafile[./f114/text() = '04' and ./f116/text() = '06']">
    <xsl:if test="not(position() = 1)">
      <xsl:text>,</xsl:text>
    </xsl:if>
    <xsl:text>{</xsl:text>
      <xsl:text>"src":"https:\/\/example.com\/wp-content\/uploads\/</xsl:text>
        <xsl:value-of select="./f117//text()"/>
        <xsl:text>",</xsl:text>
      <xsl:text>"title":"</xsl:text>
        <xsl:for-each select="/ONIXmessage/product/contributor[./b035/text() = 'A01']">
          <xsl:if test="not(position() = 1)">
            <xsl:text>, </xsl:text>
          </xsl:if>
          <xsl:value-of select="./b036//text()"/>
        </xsl:for-each>
        <xsl:text>: »</xsl:text>
        <xsl:value-of select="/ONIXmessage/product/title[./b202/text() = '01']/b203//text()"/>
        <xsl:text>«",</xsl:text>
      <xsl:text>"alt":"</xsl:text>
        <xsl:for-each select="/ONIXmessage/product/contributor[./b035/text() = 'A01']">
          <xsl:if test="not(position() = 1)">
            <xsl:text>, </xsl:text>
          </xsl:if>
          <xsl:value-of select="./b036//text()"/>
        </xsl:for-each>
        <xsl:text>: »</xsl:text>
        <xsl:value-of select="/ONIXmessage/product/title[./b202/text() = '01']/b203//text()"/>
        <xsl:text>«"<!--,--></xsl:text>
      <!--xsl:text>"position":0</xsl:text-->
    <xsl:text>}</xsl:text>
  </xsl:template>

  <xsl:template match="*" mode="copyXhtmlContent">
    <xsl:apply-templates select="./*|text()" mode="xhtmlContent"/>
  </xsl:template>
  <xsl:template match="*" mode="xhtmlContent">
    <xsl:text>&lt;</xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:apply-templates select="@*" mode="xhtmlContent"/>
    <xsl:text>&gt;</xsl:text>
    <xsl:apply-templates mode="xhtmlContent"/>
    <xsl:text>&lt;/</xsl:text>
    <xsl:value-of select="name()"/>
    <xsl:text>&gt;</xsl:text>
  </xsl:template>
  <xsl:template match="@*" mode="xhtmlContent">
    <xsl:text> </xsl:text>
    <xsl:value-of select="local-name()"/>
    <xsl:text>="</xsl:text>
    <xsl:value-of select="."/>
    <xsl:text>"</xsl:text>
  </xsl:template>

  <xsl:template match="text()|@*"/>

</xsl:stylesheet>
