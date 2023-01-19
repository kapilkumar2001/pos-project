<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="fo">
    <xsl:template match="OrderFopObject">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="20cm" page-width="10.5cm" margin-left="0.2cm"
                                       margin-right="0.2cm">
                    <fo:region-body margin-top="0.5cm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple">
                <fo:flow flow-name="xsl-region-body">

                    <fo:block>
                        <fo:table table-layout="fixed" width="90%">
                            <fo:table-column column-number="1" column-width="4cm" />
                            <fo:table-column column-number="2" column-width="2cm" />
                            <fo:table-column column-number="3" column-width="2cm" />
                            <fo:table-column column-number="4" column-width="2cm" />

                            <fo:table-header  height="6cm">
                                <fo:table-row border="solid 0.5mm black">
                                        <fo:table-cell text-align="center">
                                            <fo:block>
                                                Product Name
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center">
                                            <fo:block>
                                                Mrp
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center">
                                            <fo:block>
                                               Quantity
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="center">
                                             <fo:block>
                                                 Amount
                                             </fo:block>
                                        </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body  height="6cm">
                                
                                <xsl:for-each select="./orders/orderItem">
                                    <fo:table-row border="solid 0.2mm black">

                                        <fo:table-cell text-align="left">
                                            <fo:block>
                                                <xsl:value-of select="productName" />
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right">
                                            <fo:block>
                                                <xsl:value-of select="quantity" />
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right">
                                            <fo:block>
                                                <xsl:value-of select="sellingPrice" />
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right">
                                            <fo:block>
                                                <xsl:value-of select="amount" />
                                            </fo:block>
                                        </fo:table-cell>

                                    </fo:table-row>
                                </xsl:for-each>
                               
                            </fo:table-body>

                        </fo:table>
                    </fo:block>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>