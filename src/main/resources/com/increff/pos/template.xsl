<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="fo">
    <xsl:template match="OrderFopObject">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="22cm" page-width="17cm" margin-left="1cm"
                                       margin-right="1cm">
                    <fo:region-body margin-top="0.5cm" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <fo:external-graphic  src="src/main/webapp/static/assets/images/increff.png" content-height="scale-to-fit" height="0.50in" />
                    </fo:block>
                    <fo:block>&#160;</fo:block>
                    <fo:block>&#160;</fo:block>
                    <fo:block>&#160;</fo:block>
                    <fo:block>&#160;</fo:block>
                    <fo:block font-size="18pt" font-weight="bold" text-align="center">
                        Invoice
                    </fo:block>
                    <fo:block>&#160;</fo:block>

                    <fo:block font-size="12pt" text-align-last="justify">
                        <fo:inline font-weight="bold" space-after="5mm">
                            Order No:  
                        </fo:inline>
                        <fo:inline space-after="5mm">
                            <xsl:value-of select="orderId"/> 
                        </fo:inline> 
                        <fo:leader leader-pattern="space" />
                        <fo:inline font-weight="bold" space-after="5mm">
                            Created On
                        </fo:inline>
                    </fo:block>

                    <fo:block font-size="12pt" text-align="right">
                        <fo:inline space-after="5mm">
                            <xsl:value-of select="time"/>
                        </fo:inline>
                    </fo:block>
                    
                    <fo:block>&#160;</fo:block>
                    <fo:block>&#160;</fo:block>
                    <fo:block>
                        <fo:table table-layout="fixed" width="90%">
                            <fo:table-column column-number="1" column-width="5cm" />
                            <fo:table-column column-number="2" column-width="2.5cm" />
                            <fo:table-column column-number="3" column-width="3.5cm" />
                            <fo:table-column column-number="4" column-width="4cm" />

                            <fo:table-header >
                                <fo:table-row border="solid 0.3mm black">
                                        <fo:table-cell text-align="left" border="solid 0.3mm black">
                                            <fo:block font-weight="bold" margin-left="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                Product Name
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="right" border="solid 0.3mm black">
                                            <fo:block font-weight="bold" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                Quantity
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="right" border="solid 0.3mm black">
                                            <fo:block font-weight="bold" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                Selling Price
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell text-align="right" border="solid 0.3mm black">
                                             <fo:block font-weight="bold" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                Amount
                                             </fo:block>
                                        </fo:table-cell>
                                </fo:table-row>
                            </fo:table-header>

                            <fo:table-body >    
                                <xsl:for-each select="./orders/orderItem">
                                    <fo:table-row border="solid 0.2mm black">

                                        <fo:table-cell text-align="left" border="solid 0.2mm black">
                                            <fo:block font-size="10pt" margin-left="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                <xsl:value-of select="productName" />
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right" border="solid 0.2mm black">
                                            <fo:block font-size="10pt" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                <xsl:value-of select="quantity" />
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right" border="solid 0.2mm black">
                                            <fo:block font-size="10pt" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                <xsl:value-of select="sellingPrice" />
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right" border="solid 0.2mm black">
                                            <fo:block font-size="10pt" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                <xsl:value-of select="amount" />
                                            </fo:block>
                                        </fo:table-cell>

                                    </fo:table-row>
                                </xsl:for-each>

                               <fo:table-row border="solid 0.3mm black">

                                        <fo:table-cell text-align="left">
                                            <fo:block font-weight="bold" margin-left="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                Total Amount
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right">
                                            <fo:block>
                                                
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right">
                                            <fo:block>
                                               
                                            </fo:block>
                                        </fo:table-cell>

                                        <fo:table-cell text-align="right">
                                            <fo:block font-weight="bold" margin-right="0.1cm" margin-top="0.05cm" margin-bottom="0.05cm">
                                                <xsl:value-of select="totalAmount" />
                                            </fo:block>
                                        </fo:table-cell>

                                    </fo:table-row>


                            </fo:table-body>

                        </fo:table>
                    </fo:block>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>