<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2016-2017 Stephan Kreutzer

This file is part of onix_2_1_short_to_onix_3_0_short_1, a submodule of the
digital_publishing_workflow_tools package.

onix_2_1_short_to_onix_3_0_short_1 is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

onix_2_1_short_to_onix_3_0_short_1 is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with onix_2_1_short_to_onix_3_0_short_1. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:onix21="http://www.editeur.org/onix/2.1/short" xmlns="http://ns.editeur.org/onix/3.0/short" exclude-result-prefixes="onix21">
  <xsl:output method="xml" version="1.0" encoding="UTF-8"/>

  <xsl:template match="/onix21:ONIXmessage">
    <ONIXmessage release="3.0" xmlns="http://ns.editeur.org/onix/3.0/short">
      <xsl:comment> This file was created by onix_2_1_short_to_onix_3_0_short_1.xsl, which is free software licensed under the GNU Affero General Public License 3 or any later version (see http://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). </xsl:comment>
      <xsl:apply-templates/>
    </ONIXmessage>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:header">
    <header>
      <sender>
        <xsl:if test="./onix21:m174">
          <x298><xsl:value-of select="./onix21:m174/text()"/></x298>
        </xsl:if>
        <xsl:if test="./onix21:m175">
          <x299><xsl:value-of select="./onix21:m175/text()"/></x299>
        </xsl:if>
        <xsl:if test="./onix21:m283">
          <j272><xsl:value-of select="./onix21:m283/text()"/></j272>
        </xsl:if>
      </sender>
      <xsl:if test="./onix21:m182">
        <x307><xsl:value-of select="./onix21:m182/text()"/></x307>
      </xsl:if>
      <xsl:if test="./onix21:m183">
        <m183><xsl:value-of select="./onix21:m183/text()"/></m183>
      </xsl:if>
      <xsl:if test="./onix21:m184">
        <m184><xsl:value-of select="./onix21:m184/text()"/></m184>
      </xsl:if>
    </header>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product">
    <product>
      <a001><xsl:value-of select="./onix21:a001/text()"/></a001>
      <a002><xsl:apply-templates select="./onix21:a002"/></a002>
      <xsl:if test="./onix21:a194">
        <a194><xsl:value-of select="./onix21:a194/text()"/></a194>
      </xsl:if>
      <xsl:if test="./onix21:a197">
        <a197><xsl:value-of select="./onix21:a197/text()"/></a197>
      </xsl:if>
      <xsl:apply-templates select="./onix21:productidentifier"/>
      <xsl:apply-templates select="./onix21:workidentifier"/>
      <descriptivedetail>
        <x314>00</x314>
        <xsl:if test="./onix21:b012">
          <xsl:apply-templates select="./onix21:b012"/>
        </xsl:if>
        <xsl:if test="./onix21:b211">
          <xsl:apply-templates select="./onix21:b211"/>
        </xsl:if>
        <xsl:choose>
          <xsl:when test="./onix21:b028 and count(./onix21:title) = 0">
            <!-- Is deprecated in ONIX 2.1 -->
            <xsl:apply-templates select="./onix21:b028"/>
          </xsl:when>
          <xsl:when test="./onix21:title">
            <xsl:apply-templates select="./onix21:title"/>
          </xsl:when>
          <xsl:otherwise>
            <no-title/>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="./onix21:contributor"/>
        <xsl:if test="./onix21:n386">
          <n386/>
        </xsl:if>
        <xsl:apply-templates select="./onix21:b056"/>
        <xsl:if test="./onix21:b057">
          <b057><xsl:value-of select="./onix21:b057/text()"/></b057>
        </xsl:if>
        <xsl:apply-templates select="./onix21:language"/>
        <xsl:apply-templates select="./onix21:b255"/>
        <xsl:apply-templates select="./onix21:b061"/>
        <xsl:apply-templates select="./onix21:extent"/>
        <xsl:apply-templates select="./onix21:b064"/>
        <xsl:apply-templates select="./onix21:mainsubject"/>
        <xsl:apply-templates select="./onix21:subject"/>
        <xsl:apply-templates select="./onix21:audiencerange"/>
        <xsl:apply-templates select="./onix21:othertext"/>
      </descriptivedetail>
      <xsl:if test="./onix21:mediafile">
        <collateraldetail>
          <xsl:if test="./onix21:mediafile">
            <xsl:apply-templates select="./onix21:mediafile"/>
          </xsl:if>
        </collateraldetail>
      </xsl:if>
      <xsl:if test="./onix21:publisher or
                    ./onix21:b209 or
                    ./onix21:b083 or
                    ./onix21:b240 or
                    ./onix21:b394 or
                    ./onix21:b086 or
                    ./onix21:b003 or
                    ./onix21:salesrights">
        <publishingdetail>
          <xsl:apply-templates select="./onix21:publisher"/>
          <xsl:apply-templates select="./onix21:b240"/>
          <xsl:if test="./onix21:b209">
            <b209><xsl:value-of select="./onix21:b209/text()"/></b209>
          </xsl:if>
          <xsl:if test="./onix21:b083">
            <b083><xsl:value-of select="./onix21:b083/text()"/></b083>
          </xsl:if>
          <xsl:if test="./onix21:b394">
            <b394><xsl:value-of select="./onix21:b394/text()"/></b394>
          </xsl:if>
          <xsl:apply-templates select="./onix21:b086"/>
          <xsl:apply-templates select="./onix21:b003"/>
          <xsl:apply-templates select="./onix21:salesrights"/>
        </publishingdetail>
      </xsl:if>
      <xsl:if test="./onix21:relatedproduct">
        <relatedmaterial>
          <xsl:apply-templates select="./onix21:relatedproduct"/>
        </relatedmaterial>
      </xsl:if>
      <xsl:if test="./onix21:supplydetail">
        <productsupply>
          <xsl:if test="./onix21:supplydetail/onix21:j272">
            <marketpublishingdetail>
              <productcontact>
                <x482>incompatible</x482>
                <xsl:choose>
                  <xsl:when test="./onix21:supplydetail/onix21:j137">
                    <x484><xsl:value-of select="./onix21:supplydetail/onix21:j137/text()"/></x484>
                  </xsl:when>
                  <xsl:otherwise>
                    <!-- To productcontactidentifier -->
                    <j272-supplieridentifier-not-supported/>
                  </xsl:otherwise>
                </xsl:choose>
                <j272><xsl:value-of select="./onix21:supplydetail/onix21:j272/text()"/></j272>
              </productcontact>
              <j407>00</j407>
            </marketpublishingdetail>
          </xsl:if>
          <xsl:apply-templates select="./onix21:supplydetail"/>
        </productsupply>
      </xsl:if>
    </product>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:a002">
    <xsl:choose>
      <xsl:when test="./text() = '01'">
        <xsl:text>01</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '02'">
        <xsl:text>02</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '03'">
        <xsl:text>03</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '04'">
        <xsl:text>04</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '05'">
        <xsl:text>05</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '08'">
        <xsl:text>08</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>'</xsl:text><xsl:value-of select="./text()"/><xsl:text>' unknown</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:productidentifier">
    <productidentifier>
      <b221><xsl:value-of select="./onix21:b221/text()"/></b221>
      <xsl:if test="./onix21:b221/text() = '01' and ./onix21:b233">
        <b233><xsl:value-of select="./onix21:b233/text()"/></b233>
      </xsl:if>
      <b244><xsl:value-of select="./onix21:b244/text()"/></b244>
    </productidentifier>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:workidentifier">
    <productidentifier>
      <b221>
        <xsl:choose>
          <xsl:when test="./onix21:b201/text() = '01'">
            <xsl:text>01</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '02'">
            <xsl:text>02</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '06'">
            <xsl:text>06</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '11'">
            <xsl:text>incompatible</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '15'">
            <xsl:text>15</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '18'">
            <xsl:text>incompatible</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '32'">
            <xsl:text>incompatible</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:b201/text() = '33'">
            <xsl:text>incompatible</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>'</xsl:text><xsl:value-of select="./onix21:b201/text()"/><xsl:text>' unknown</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </b221>
      <xsl:if test="./onix21:b201/text() = '01' and ./onix21:b233">
        <b233><xsl:value-of select="./onix21:b233/text()"/></b233>
      </xsl:if>
      <b244><xsl:value-of select="./onix21:b244/text()"/></b244>
    </productidentifier>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b012">
    <b012>
      <xsl:choose>
        <xsl:when test="./text() = 'AA'">
          <xsl:text>AA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AB'">
          <xsl:text>AB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AC'">
          <xsl:text>AC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AD'">
          <xsl:text>AD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AE'">
          <xsl:text>AE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AF'">
          <xsl:text>AF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AG'">
          <xsl:text>AG</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AH'">
          <xsl:text>AH</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AI'">
          <xsl:text>AI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AJ'">
          <xsl:text>AJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AK'">
          <xsl:text>AK</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AL'">
          <xsl:text>AL</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'AZ'">
          <xsl:text>AZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BA'">
          <xsl:text>BA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BB'">
          <xsl:text>BB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BC'">
          <xsl:text>BC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BD'">
          <xsl:text>BD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BE'">
          <xsl:text>BE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BF'">
          <xsl:text>BF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BG'">
          <xsl:text>BG</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BH'">
          <xsl:text>BH</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BI'">
          <xsl:text>BI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BJ'">
          <xsl:text>BJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BK'">
          <xsl:text>BK</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BL'">
          <xsl:text>BL</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BM'">
          <xsl:text>BM</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BN'">
          <xsl:text>BN</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BO'">
          <xsl:text>BO</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BP'">
          <xsl:text>BP</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'BZ'">
          <xsl:text>BZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'CA'">
          <xsl:text>CA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'CB'">
          <xsl:text>CB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'CC'">
          <xsl:text>CC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'CD'">
          <xsl:text>CD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'CE'">
          <xsl:text>CE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'CZ'">
          <xsl:text>CZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DA'">
          <!-- ONIX 3.0 adds the requirement that it has to be on a physical carrier. -->
          <xsl:text>DA incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DB'">
          <xsl:text>DB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DC'">
          <xsl:text>DC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DD'">
          <!-- VI for DVD video, AI for DVD audio, DI for DVD-ROM. -->
          <xsl:text>DD incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DE'">
          <xsl:text>DE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DF'">
          <xsl:text>DF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DG'">
          <!-- Removed. See 'E*' codes. -->
          <xsl:text>DG incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DH'">
          <!-- Removed. See 'E*' codes. -->
          <xsl:text>DH incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DI'">
          <xsl:text>DI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DJ'">
          <xsl:text>DJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DK'">
          <xsl:text>DK</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DL'">
          <xsl:text>DL</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DM'">
          <xsl:text>DM</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DN'">
          <xsl:text>DN</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DO'">
          <!-- Removed. -->
          <xsl:text>DO incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'DZ'">
          <xsl:text>DZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FA'">
          <xsl:text>FA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FB'">
          <!-- FE or FF. -->
          <xsl:text>FB incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FC'">
          <xsl:text>FC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FD'">
          <xsl:text>FD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FE'">
          <xsl:text>FE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FF'">
          <xsl:text>FF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'FZ'">
          <xsl:text>FZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'MA'">
          <xsl:text>MA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'MB'">
          <xsl:text>MB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'MC'">
          <xsl:text>MC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'MZ'">
          <xsl:text>MZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PA'">
          <xsl:text>PA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PB'">
          <xsl:text>PB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PC'">
          <xsl:text>PC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PD'">
          <xsl:text>PD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PE'">
          <xsl:text>PE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PF'">
          <xsl:text>PF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PG'">
          <xsl:text>PG</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PH'">
          <xsl:text>PH</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PI'">
          <xsl:text>PI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PJ'">
          <xsl:text>PJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PK'">
          <xsl:text>PK</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PL'">
          <xsl:text>PL</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PM'">
          <xsl:text>PM</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PN'">
          <xsl:text>PN</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PO'">
          <xsl:text>PO</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PP'">
          <xsl:text>PP</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PQ'">
          <xsl:text>PQ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PR'">
          <xsl:text>PR</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PS'">
          <xsl:text>PS</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PT'">
          <xsl:text>PT</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'PZ'">
          <xsl:text>PZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VA'">
          <xsl:text>VA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VB'">
          <!-- Removed. -->
          <xsl:text>VB incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VC'">
          <!-- Removed. -->
          <xsl:text>VC incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VD'">
          <!-- Removed. -->
          <xsl:text>VD incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VE'">
          <!-- Removed. -->
          <xsl:text>VE incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VF'">
          <xsl:text>VF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VG'">
          <!-- Removed. -->
          <xsl:text>VG incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VH'">
          <!-- Removed. -->
          <xsl:text>VH incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VI'">
          <xsl:text>VI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VJ'">
          <xsl:text>VJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VK'">
          <xsl:text>VK</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VL'">
          <xsl:text>VL</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VM'">
          <xsl:text>VM</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VN'">
          <xsl:text>VN</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VO'">
          <xsl:text>VO</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VP'">
          <xsl:text>VP</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'VZ'">
          <xsl:text>VZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'WW'">
          <!-- Removed. -->
          <xsl:text>WW incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XA'">
          <xsl:text>XA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XB'">
          <xsl:text>XB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XC'">
          <!-- Make sure the additional requirements of ONIX 3.0 are met. -->
          <xsl:text>XC incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XD'">
          <xsl:text>XD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XE'">
          <!-- Make sure the additional requirements of ONIX 3.0 are met. -->
          <xsl:text>XE incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XF'">
          <xsl:text>XF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XG'">
          <xsl:text>XG</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XH'">
          <xsl:text>XH</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XI'">
          <xsl:text>XI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XJ'">
          <xsl:text>XJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XK'">
          <xsl:text>XK</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XL'">
          <xsl:text>XL incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XM'">
          <xsl:text>XM incompatible</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'XZ'">
          <xsl:text>XZ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZA'">
          <xsl:text>ZA</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZB'">
          <xsl:text>ZB</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZC'">
          <xsl:text>ZC</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZD'">
          <xsl:text>ZD</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZE'">
          <xsl:text>ZE</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZF'">
          <xsl:text>ZF</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZG'">
          <xsl:text>ZG</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZH'">
          <xsl:text>ZH</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZI'">
          <xsl:text>ZI</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZJ'">
          <xsl:text>ZJ</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZY'">
          <xsl:text>ZY</xsl:text>
        </xsl:when>
        <xsl:when test="./text() = 'ZZ'">
          <xsl:text>ZZ</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>'</xsl:text><xsl:value-of select="./text()"/><xsl:text>' unknown</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </b012>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b211">
    <xsl:if test="../onix21:b012/text() = 'DG'">
      <b333>
        <xsl:choose>
          <xsl:when test="./text() = '029'">
            <xsl:text>E101</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>'</xsl:text><xsl:value-of select="./text()"/><xsl:text>' not supported</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </b333>
    </xsl:if>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b028">
    <titledetail>
      <b202>00</b202>
      <titleelement>
        <x409>01</x409>
        <b203><xsl:value-of select="./text()"/></b203>
      </titleelement>
    </titledetail>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:title">
    <titledetail>
      <b202><xsl:value-of select="./onix21:b202/text()"/></b202>
      <titleelement>
        <x409>01</x409>
        <b203><xsl:value-of select="./onix21:b203/text()"/></b203>
        <xsl:if test="./onix21:b029">
          <b029><xsl:value-of select="./onix21:b029/text()"/></b029>
        </xsl:if>
      </titleelement>
    </titledetail>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:contributor">
    <contributor>
      <b034><xsl:number count="/onix21:ONIXmessage/onix21:product/onix21:contributor"/></b034>
      <b035><xsl:value-of select="./onix21:b035/text()"/></b035>
      <xsl:if test="./onix21:b252">
        <xsl:choose>
          <xsl:when test="./onix21:b035/text() = 'B06' or
                          ./onix21:b035/text() = 'B08' or
                          ./onix21:b035/text() = 'B10'">
            <x412><xsl:value-of select="./onix21:b252/text()"/></x412>
          </xsl:when>
          <xsl:otherwise>
            <b252-invalid/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="./onix21:b036">
          <b036><xsl:value-of select="./onix21:b036/text()"/></b036>
          <xsl:apply-templates select="./onix21:b037"/>
          <xsl:apply-templates select="./onix21:b039"/>
          <xsl:apply-templates select="./onix21:b040"/>
        </xsl:when>
        <xsl:otherwise>
          <no-contributor-name/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="./onix21:b044">
        <b044><xsl:value-of select="./onix21:b044/text()"/></b044>
      </xsl:if>
    </contributor>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:contributor/onix21:b037">
    <b037><xsl:value-of select="./text()"/></b037>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:contributor/onix21:b039">
    <b039><xsl:value-of select="./text()"/></b039>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:contributor/onix21:b040">
    <b040><xsl:value-of select="./text()"/></b040>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b056">
    <x419><xsl:value-of select="./text()"/></x419>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:language">
    <language>
      <b253><xsl:value-of select="./onix21:b253/text()"/></b253>
      <b252><xsl:value-of select="./onix21:b252/text()"/></b252>
    </language>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b255">
    <extent>
      <b218>00</b218>
      <b219><xsl:value-of select="./text()"/></b219>
      <b220>03</b220>
    </extent>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b061">
    <extent>
      <b218>05</b218>
      <b219><xsl:value-of select="./text()"/></b219>
      <b220>03</b220>
    </extent>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:extent">
    <extent>
      <b218><xsl:value-of select="./onix21:b218/text()"/></b218>
      <b219><xsl:value-of select="./onix21:b219/text()"/></b219>
      <b220><xsl:value-of select="./onix21:b220/text()"/></b220>
    </extent>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b064">
    <subject>
      <x425/>
      <b067>10</b067>
      <xsl:if test="../onix21:b200">
        <b068><xsl:value-of select="../onix21:b200/text()"/></b068>
      </xsl:if>
      <b069><xsl:value-of select="./text()"/></b069>
    </subject>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:mainsubject">
    <subject>
      <x425/>
      <b067><xsl:value-of select="./onix21:b191/text()"/></b067>
      <xsl:if test="./onix21:b171 and ./onix21:b067/text() = '24'">
        <b171><xsl:value-of select="./onix21:b171/text()"/></b171>
      </xsl:if>
      <xsl:if test="./onix21:b068">
        <b068><xsl:value-of select="./onix21:b068/text()"/></b068>
      </xsl:if>
      <xsl:if test="./onix21:b069">
        <b069><xsl:value-of select="./onix21:b069/text()"/></b069>
      </xsl:if>
      <xsl:if test="./onix21:b070">
        <b070><xsl:value-of select="./onix21:b070/text()"/></b070>
      </xsl:if>
    </subject>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:subject">
    <subject>
      <b067><xsl:value-of select="./onix21:b067/text()"/></b067>
      <xsl:if test="./onix21:b171 and ./onix21:b067/text() = '24'">
        <b171><xsl:value-of select="./onix21:b171/text()"/></b171>
      </xsl:if>
      <xsl:if test="./onix21:b068">
        <b068><xsl:value-of select="./onix21:b068/text()"/></b068>
      </xsl:if>
      <xsl:if test="./onix21:b069">
        <b069><xsl:value-of select="./onix21:b069/text()"/></b069>
      </xsl:if>
      <xsl:if test="./onix21:b070">
        <b070><xsl:value-of select="./onix21:b070/text()"/></b070>
      </xsl:if>
    </subject>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:audiencerange">
    <audiencerange>
      <b074><xsl:value-of select="./onix21:b074/text()"/></b074>
      <b075><xsl:value-of select="./onix21:b075/text()"/></b075>
      <xsl:if test="./onix21:b076">
        <b076><xsl:value-of select="./onix21:b076/text()"/></b076>
      </xsl:if>
      <xsl:if test="count(./onix21:b075) > 1 or count(./onix21:b076) > 1">
        <multiple-audience-ranges/>
      </xsl:if>
    </audiencerange>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:othertext">
    <othertext-incompatible/>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:mediafile">
    <supportingresource>
      <x436>
        <xsl:choose>
          <xsl:when test="./onix21:f114/text() = '01'">
            <xsl:text>28</xsl:text>
          </xsl:when>
          <xsl:when test="./onix21:f114/text() = '04'">
            <xsl:text>01</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>'</xsl:text><xsl:value-of select="./onix21:f144/text()"/><xsl:text>' not supported</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </x436>
      <x427>not supported</x427>
      <x437>not supported</x437>
      <resourceversion>
        <x441>not supported</x441>
        <xsl:if test="./onix21:f115">
          <resourceversionfeature>
            <x442>01</x442>
            <x439>
              <xsl:choose>
                <xsl:when test="./onix21:f115/text() = '02'">
                  <xsl:text>D501</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '03'">
                  <xsl:text>D502</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '04'">
                  <xsl:text>E107</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '05'">
                  <xsl:text>D504</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '06'">
                  <xsl:text>A105</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '07'">
                  <xsl:text>A103</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '08'">
                  <xsl:text>D105</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '09'">
                  <xsl:text>D503</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '10'">
                  <xsl:text>A106</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '11'">
                  <xsl:text>A107</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '12'">
                  <xsl:text>A104</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '13'">
                  <xsl:text>A111</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '14'">
                  <xsl:text>D104</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '15'">
                  <xsl:text>A108</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '16'">
                  <xsl:text>D103</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '17'">
                  <xsl:text>D102</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '18'">
                  <xsl:text>incompatible</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '19'">
                  <xsl:text>D108</xsl:text>
                </xsl:when>
                <xsl:when test="./onix21:f115/text() = '20'">
                  <xsl:text>D109</xsl:text>
                </xsl:when>
              </xsl:choose>
            </x439>
          </resourceversionfeature>
        </xsl:if>
        <x435><xsl:value-of select="./onix21:f117/text()"/></x435>
      </resourceversion>
    </supportingresource>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:publisher">
    <publisher>
      <b291><xsl:value-of select="./onix21:b291/text()"/></b291>
      <xsl:if test="./onix21:b241">
        <publisheridentifier>
          <x447><xsl:value-of select="./onix21:b241/text()"/></x447>
          <b244><xsl:value-of select="./onix21:b243/text()"/></b244>
        </publisheridentifier>
      </xsl:if>
      <xsl:if test="./onix21:b081">
        <b081><xsl:value-of select="./onix21:b081/text()"/></b081>
      </xsl:if>
      <xsl:if test="count(/onix21:ONIXmessage/onix21:product) = 1 and /onix21:ONIXmessage/onix21:product/onix21:productwebsite/onix21:b367/text() = '02'">
        <website>
          <b367>02</b367>
          <b295><xsl:value-of select="/onix21:ONIXmessage/onix21:product/onix21:productwebsite/onix21:f123/text()"/></b295>
        </website>
      </xsl:if>
    </publisher>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b240">
    <publisher>
      <b291>04</b291>
      <b081><xsl:value-of select="./text()"/></b081>
    </publisher>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b086">
    <publishingdate>
      <x448>20</x448>
      <b306><xsl:value-of select="./text()"/></b306>
    </publishingdate>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:b003">
    <publishingdate>
      <x448>01</x448>
      <b306><xsl:value-of select="./text()"/></b306>
    </publishingdate>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:salesrights">
    <salesrights>
      <b089><xsl:value-of select="./onix21:b089/text()"/></b089>
      <territory>
        <xsl:apply-templates select="./onix21:b388"/>
        <xsl:apply-templates select="./onix21:b390"/>
      </territory>
    </salesrights>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:salesrights/onix21:b388">
    <x450><xsl:value-of select="./text()"/></x450>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:salesrights/onix21:b390">
    <x449><xsl:value-of select="./text()"/></x449>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:relatedproduct">
    <relatedproduct>
      <x455><xsl:apply-templates select="./onix21:h208"/></x455>
      <xsl:apply-templates select="./onix21:productidentifier"/>
    </relatedproduct>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:relatedproduct/onix21:h208">
    <xsl:choose>
      <xsl:when test="./text() = '00'">
        <xsl:text>00</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '01'">
        <xsl:text>01</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '02'">
        <xsl:text>02</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '03'">
        <xsl:text>03</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '04'">
        <xsl:text>04</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '05'">
        <xsl:text>05</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '06'">
        <xsl:text>06</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '07'">
        <xsl:text>07</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '08'">
        <xsl:text>08</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '09'">
        <xsl:text>09</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '10'">
        <xsl:text>10</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '11'">
        <xsl:text>11</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '12'">
        <xsl:text>12</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '13'">
        <xsl:text>13</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '14'">
        <!-- Removed -->
        <xsl:text>14 incompatible</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '15'">
        <!-- Removed -->
        <xsl:text>15 incompatible</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '16'">
        <xsl:text>16</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '17'">
        <xsl:text>17</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '18'">
        <xsl:text>18</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '19'">
        <xsl:text>19</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '20'">
        <xsl:text>20</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '21'">
        <xsl:text>21</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '22'">
        <xsl:text>22</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '23'">
        <xsl:text>23</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '24'">
        <xsl:text>24</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '25'">
        <xsl:text>25</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '26'">
        <xsl:text>26</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '27'">
        <xsl:text>27</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '28'">
        <xsl:text>28</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '29'">
        <xsl:text>29</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '30'">
        <xsl:text>30</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '31'">
        <xsl:text>31</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '32'">
        <xsl:text>32</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '33'">
        <xsl:text>33</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '34'">
        <xsl:text>34</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '35'">
        <xsl:text>35</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '36'">
        <!-- Removed -->
        <xsl:text>36 incompatible</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '37'">
        <xsl:text>37</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '38'">
        <xsl:text>38</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '39'">
        <xsl:text>39</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '40'">
        <xsl:text>40</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '41'">
        <xsl:text>41</xsl:text>
      </xsl:when>
      <xsl:when test="./text() = '42'">
        <xsl:text>42</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>'</xsl:text><xsl:value-of select="./text()"/><xsl:text>' unknown</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:relatedproduct/onix21:productidentifier">
    <productidentifier>
      <b221><xsl:value-of select="./onix21:b221/text()"/></b221>
      <b244><xsl:value-of select="./onix21:b244/text()"/></b244>
    </productidentifier>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:supplydetail">
    <supplydetail>
      <supplier>
        <xsl:choose>
          <xsl:when test="./onix21:j292">
            <j292><xsl:value-of select="./onix21:j292/text()"/></j292>
          </xsl:when>
          <xsl:otherwise>
            <j292>00</j292>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="./onix21:supplieridentifier"/>
        <xsl:if test="./onix21:j137">
          <j137><xsl:value-of select="./onix21:j137/text()"/></j137>
        </xsl:if>
      </supplier>
      <xsl:choose>
        <xsl:when test="./onix21:j396">
          <j396><xsl:value-of select="./onix21:j396"/></j396>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="./onix21:j414">
              <j396>
                <xsl:choose>
                  <xsl:when test="./onix21:j414/text() = 'IP'">
                    <xsl:text>21</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:text>'</xsl:text><xsl:value-of select="./onix21:j414/text()"/><xsl:text>' not supported</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
              </j396>
            </xsl:when>
            <xsl:otherwise>
              <j396-j414-missing/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="./onix21:j143">
        <supplydate>
          <x461>02</x461>
          <b306><xsl:value-of select="./onix21:j143/text()"/></b306>
        </supplydate>
        <xsl:apply-templates select="./onix21:price"/>
      </xsl:if>
    </supplydetail>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:supplydetail/onix21:supplieridentifier">
    <supplieridentifier>
      <j345><xsl:value-of select="./onix21:j345/text()"/></j345>
      <b244><xsl:value-of select="./onix21:b244/text()"/></b244>
    </supplieridentifier>
  </xsl:template>

  <xsl:template match="/onix21:ONIXmessage/onix21:product/onix21:supplydetail/onix21:price">
    <price>
      <xsl:if test="./onix21:j148">
        <x462><xsl:value-of select="./onix21:j148/text()"/></x462>
      </xsl:if>
      <xsl:if test="./onix21:j263">
        <j263><xsl:value-of select="./onix21:j263/text()"/></j263>
      </xsl:if>
      <xsl:if test="./onix21:j149">
        <j149-not-supported/>
      </xsl:if>
      <xsl:if test="./onix21:j262">
        <j262><xsl:value-of select="./onix21:j262/text()"/></j262>
      </xsl:if>
      <xsl:if test="./onix21:j266">
        <j266><xsl:value-of select="./onix21:j266/text()"/></j266>
      </xsl:if>
      <xsl:if test="./onix21:j151">
        <j151><xsl:value-of select="./onix21:j151/text()"/></j151>
      </xsl:if>
      <xsl:if test="./onix21:j153">
        <tax>
          <x471><xsl:value-of select="./onix21:j153/text()"/></x471>
          <xsl:choose>
            <xsl:when test="./onix21:j154 or ./onix21:j156">
              <xsl:if test="./onix21:j154">
                <x472><xsl:value-of select="./onix21:j154/text()"/></x472>
              </xsl:if>
              <xsl:if test="./onix21:j156">
                <x474><xsl:value-of select="./onix21:j156/text()"/></x474>
              </xsl:if>
            </xsl:when>
            <xsl:otherwise>
              <!-- Either x472 or x474 is required by ONIX 3.0, but not in ONIX 2.1. -->
              <j154-or-j156-missing/>
            </xsl:otherwise>
          </xsl:choose>
        </tax>
      </xsl:if>
      <xsl:if test="./onix21:j152">
        <j152><xsl:value-of select="./onix21:j152/text()"/></j152>
      </xsl:if>
      <xsl:if test="./onix21:b251">
        <territory>
          <x449>
            <xsl:for-each select="./onix21:b251">
              <xsl:value-of select="./text()"/>
              <xsl:if test="position() != last()">
                <xsl:text> </xsl:text>
              </xsl:if>
            </xsl:for-each>
          </x449>
        </territory>
      </xsl:if>
      <xsl:if test="./onix21:j161">
        <pricedate>
          <xsl:choose>
            <xsl:when test="./onix21:j260">
              <xsl:choose>
                <xsl:when test="./onix21:j260/text() = '06'">
                  <x476>24</x476>
                </xsl:when>
                <xsl:otherwise>
                  <x476>14</x476>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
              <x476>14</x476>
            </xsl:otherwise>
          </xsl:choose>
          <b306><xsl:value-of select="./onix21:j161/text()"/></b306>
        </pricedate>
      </xsl:if>
      <xsl:if test="./onix21:j162">
        <pricedate>
          <xsl:choose>
            <xsl:when test="./onix21:j260">
              <xsl:choose>
                <xsl:when test="./onix21:j260/text() = '06'">
                  <x476>24</x476>
                </xsl:when>
                <xsl:otherwise>
                  <x476>15</x476>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
              <x476>15</x476>
            </xsl:otherwise>
          </xsl:choose>
          <b306><xsl:value-of select="./onix21:j162/text()"/></b306>
        </pricedate>
      </xsl:if>
    </price>
  </xsl:template>

  <!-- xsl:template match="node()|@*|text()">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*|text()"/>
    </xsl:copy>
  </xsl:template -->

  <xsl:template match="node()|@*|text()"/>

</xsl:stylesheet>
