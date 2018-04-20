<?xml version="1.0" encoding="utf-8" ?>

<!-- 
 iso2html.xsl - Transformation from ISO 19139 into HTML 
		Created by Kim Durante, Stanford University Libraries
		2018/04/20 - Modified for OGP usage by Ben Hickson
		 
		TODO: Needs full Data Quality section mapped
			Not sure if complete contactInfo is needed for each Responsible Party? 

		 
		 
		 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xlink="http://www.w3.org/1999/xlink"
		xmlns="http://www.isotc211.org/2005/gmd"
		xmlns:gmd="http://www.isotc211.org/2005/gmd" 
		xmlns:gco="http://www.isotc211.org/2005/gco" 
		xmlns:gts="http://www.isotc211.org/2005/gts" 
		xmlns:srv="http://www.isotc211.org/2005/srv" 
		xmlns:gml="http://www.opengis.net/gml"
		exclude-result-prefixes="gmd gco gml srv xlink gts">
		
	<xsl:output method="html" encoding="UTF-8" indent="yes"/>
	
	<xsl:template match="/">
		<A name="Top" />
		<h1>
			<xsl:value-of select="MD_Metadata/identificationInfo/MD_DataIdentification/citation/CI_Citation/title/CharacterString"/>
		</h1>
		<h2>Metadata:</h2>
		<ul>
			<xsl:if test="//identificationInfo">
				<li><a href="#iso-identification-info">Identification Information</a></li>
			</xsl:if>
						
			<xsl:if test="//referenceSystemInfo">
				<li><a href="#iso-spatial-reference-info">Spatial Reference Information</a></li>
			</xsl:if>

			<xsl:if test="//dataQualityInfo">
				<li><a href="#iso-data-quality-info">Data Quality Information</a></li>
			</xsl:if>

			<xsl:if test="//distributionInfo">
				<li><a href="#iso-distribution-info">Distribution Information</a></li>
			</xsl:if>

			<xsl:if test="//contentInfo">
					<li><a href="#iso-content-info">Content Information</a></li>
			</xsl:if>
			
			<xsl:if test="//spatialRepresentationInfo">
				<li><a href="#iso-spatial-representation-info">Spatial Representation Information</a></li>
			</xsl:if>
			
			<xsl:if test="//MD_Metadata">
				<li><a href="#iso-metadata-reference-info">Metadata Reference Information</a></li>
			</xsl:if>
		</ul>
		<!--<xsl:apply-templates/>-->
		<xsl:apply-templates select="MD_Metadata" />
	</xsl:template>
	

	<xsl:template match="MD_Metadata">
		<A name="iso-identification-info">
			<HR />
		</A>
		<div id="iso-identification-info">
			<dl>
				<dt class="metadataMajor">Identification Information:</dt>
				<dd class="metadataMajor-Content">
					<dl>
						<xsl:for-each select="identificationInfo/MD_DataIdentification/citation/CI_Citation">
							<dt>Citation</dt>
							<dd>
								<dl>
									<dt>Title</dt>
									<dd>
										<xsl:value-of select="title"/>
									</dd>
									<xsl:choose>
										<xsl:when test="citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='originator']">
											<xsl:for-each select="citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='originator']">
											<dt>Originator</dt>
											<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
											</xsl:for-each>
										</xsl:when>
										
										<xsl:when test="//identificationInfo/MD_DataIdentification/pointOfContact">
											<xsl:for-each select="//identificationInfo/MD_DataIdentification/pointOfContact">
											<xsl:if test="CI_ResponsibleParty/role/CI_RoleCode/@codeListValue='originator'">
											<dt>Originator</dt>
											<dd><xsl:value-of select="CI_ResponsibleParty/organisationName | CI_ResponsibleParty/individualName"/></dd>
											</xsl:if>
											</xsl:for-each>
										</xsl:when>
									</xsl:choose>
								 
									<xsl:for-each select="citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='publisher']">
											<dt>Publisher</dt>
											<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
											<xsl:if test="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/city">
											<dt>Place of Publication</dt>	 
											<dd><xsl:value-of select="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/city"/>
													<xsl:if test="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/administrativeArea">
															<xsl:text>,</xsl:text>
															<xsl:value-of select="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/administrativeArea"/>
													</xsl:if>
													<xsl:if test="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/country">
															<xsl:text>,</xsl:text>
															<xsl:value-of select="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/country"/>
													</xsl:if>
											</dd>		
											</xsl:if>
									</xsl:for-each>
									
									

									 <xsl:for-each select="date/CI_Date">
											 <xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'publication')">
												<dt>Publication Date</dt>
													 <dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
											 </xsl:if>
											 <xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'creation')">
													 <dt>Creation Date</dt>
													 <dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
											 </xsl:if>
											 <xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'revision')">
													 <dt>Revision Date</dt>
													 <dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
											 </xsl:if>
									 </xsl:for-each>
									
									<xsl:if test="edition">
											<dt>Edition</dt>
											<dd>
													<xsl:value-of select="edition"/>
											</dd>
									</xsl:if>
									
									<xsl:if test="identifier/MD_Identifier/code">
										<dt>Identifier</dt>
										<dd>
											<xsl:value-of select="identifier/MD_Identifier/code"/>
										</dd>
									</xsl:if>
									
									<xsl:for-each select="presentationForm/CI_PresentationFormCode/@codeListValue">
											<dt>Geospatial Data Presentation Form</dt>
											<dd><xsl:value-of select="."/></dd>
									</xsl:for-each> 
									
									<xsl:for-each select="collectiveTitle">
											<dt>Collection Title</dt>
											<dd><xsl:value-of select="."/></dd>
									</xsl:for-each> 
									
									<xsl:for-each select="otherCitationDetails">
											<dt>Other Citation Details</dt>
											<dd>
													<xsl:value-of select="."/>
											</dd>
									</xsl:for-each>
									
									<xsl:for-each select="series/CI_Series">
											<dt>Series</dt>
											<dd>
												<dl>
													<dd>
														<dt>Series Title</dt>
														<dd><xsl:value-of select="name"/></dd>
														<dt>Issue</dt>
														<dd><xsl:value-of select="issueIdentification"/></dd>
													</dd>
												</dl>
											</dd>
									</xsl:for-each>
								</dl>
							</dd>
						</xsl:for-each>
							

						<xsl:for-each select="identificationInfo/MD_DataIdentification/abstract">
							<dt>Abstract</dt>
							<dd><xsl:value-of select="."/></dd>
						</xsl:for-each>
						
						<xsl:for-each select="identificationInfo/MD_DataIdentification/purpose">
							<dt>Purpose</dt>
							<dd><xsl:value-of select="."/></dd>
						</xsl:for-each>
						
						<xsl:for-each select="identificationInfo/MD_DataIdentification/supplementalInformation">
							<dt>Supplemental Information</dt>
							<dd>
									<xsl:value-of select="."/>
							</dd>
						</xsl:for-each>
							
						<xsl:for-each select="identificationInfo/MD_DataIdentification/spatialResolution/MD_Resolution/equivalentScale/MD_RepresentativeFraction/denominator">
							<dt>Scale Denominator</dt>
							<dd>
								<xsl:value-of select="."/>
							</dd>
						</xsl:for-each>
						 
						<xsl:for-each select="identificationInfo/MD_DataIdentification/extent/EX_Extent/temporalElement/EX_TemporalExtent/extent">
							<dt>Temporal Extent</dt>
							<dd>
								<dl>
									<xsl:if test="ancestor-or-self::*/description">
									<dt>Currentness Reference</dt>
									<dd><xsl:value-of select="ancestor-or-self::*/description"/></dd>
									</xsl:if>
									<xsl:choose>
									<xsl:when test="gml:TimePeriod">
									<dt>Time Period</dt>
										<dd>
											<dl>
												<dt>Begin</dt>
												<dd>
													<xsl:value-of select="gml:TimePeriod/gml:beginPosition"/>
												</dd>
												<dt>End</dt>
												<dd>
													<xsl:value-of select="gml:TimePeriod/gml:endPosition"/>
												</dd>
											</dl>
										</dd>		
										</xsl:when>
										<xsl:when test="gml:TimeInstant">
												<dt>Time Instant</dt>
												<dd><xsl:value-of select="gml:TimeInstant/gml:timePosition"/></dd>
										</xsl:when>
									</xsl:choose>
								</dl>
							</dd>
						</xsl:for-each>
											
									
						<xsl:for-each select="identificationInfo/MD_DataIdentification/extent/EX_Extent/geographicElement/EX_GeographicBoundingBox">
							<dt>Bounding Box</dt>
							<dd>
								<dl>
									<dt>West</dt>
									<dd>
										<xsl:value-of select="westBoundLongitude"/>
									</dd>
									<dt>East</dt>
									<dd>
										<xsl:value-of select="eastBoundLongitude"/>
									</dd>
									<dt>North</dt>
									<dd>
										<xsl:value-of select="northBoundLatitude"/>
									</dd>
									<dt>South</dt>
									<dd>
										<xsl:value-of select="southBoundLatitude"/>
									</dd>
								</dl>
							</dd>
						</xsl:for-each>	
							
						<xsl:if test="identificationInfo/MD_DataIdentification/topicCategory/MD_TopicCategoryCode"> 
							<dt>ISO Topic Category</dt>
							<xsl:for-each select="identificationInfo/MD_DataIdentification/topicCategory/MD_TopicCategoryCode">
								<dd>
									<xsl:value-of select="."/>
								</dd>
							</xsl:for-each>
						</xsl:if> 
							

						<xsl:for-each select="identificationInfo/MD_DataIdentification/descriptiveKeywords/MD_Keywords">
							<xsl:choose>
								<xsl:when test="ancestor-or-self::*/type/MD_KeywordTypeCode[@codeListValue='theme']">
									<dt>Theme Keyword</dt>
									<xsl:for-each select="keyword">

										<dd>
											<xsl:value-of select="."/>
											<xsl:if test="position()=last()">
												<dl>
													<dt>Theme Keyword Thesaurus</dt>
													<dd> <xsl:value-of select="ancestor-or-self::*/thesaurusName/CI_Citation/title"/></dd>
												</dl>
											</xsl:if>
										</dd>
									</xsl:for-each>

								</xsl:when>	
								
								<xsl:when test="ancestor-or-self::*/type/MD_KeywordTypeCode[@codeListValue='place']">
									<dt>Place Keyword</dt>
									<xsl:for-each select="keyword">

										<dd>
											<xsl:value-of select="."/>
											<xsl:if test="position()=last()">
												<dl>
													<dt>Place Keyword Thesaurus</dt>
													<dd> <xsl:value-of select="ancestor-or-self::*/thesaurusName/CI_Citation/title"/></dd>
												</dl>
											</xsl:if>
										</dd>
									</xsl:for-each>
								</xsl:when>	
								
								<xsl:when test="ancestor-or-self::*/type/MD_KeywordTypeCode[@codeListValue='temporal']">
									<dt>Temporal Keyword</dt>
									<xsl:for-each select="keyword">

										<dd>
												<xsl:value-of select="."/>
										</dd>
									</xsl:for-each>
								</xsl:when> 
							</xsl:choose>												 
										
								
						</xsl:for-each>			
																	
						<xsl:for-each select="identificationInfo/MD_DataIdentification/resourceConstraints">
								
							<xsl:if test="MD_LegalConstraints">
									<dt>Legal Constraints</dt>
							</xsl:if>

							<xsl:if test="MD_SecurityConstraints">
									<dt>Security Constraints</dt>
							</xsl:if>
							
							<xsl:if test="MD_Constraints">
									<dt>Resource Constraints</dt>
							</xsl:if>
							
							<dd>
								<dl>
									<xsl:if test="*/useLimitation">
									<dt>Use Limitation</dt>
									<dd>
										<xsl:value-of select="*/useLimitation"/>
									</dd>
									</xsl:if>
									<xsl:if test="*/accessConstraints">
									<dt>Access Restrictions</dt>
									<dd>
										<xsl:value-of select="*/accessConstraints/MD_RestrictionCode/@codeListValue"/>
									</dd>
									</xsl:if>
									<xsl:if test="*/useConstraints">
									<dt>Use Restrictions</dt>
									<dd>
										<xsl:value-of select="*/useConstraints/MD_RestrictionCode/@codeListValue"/>
									</dd>
									</xsl:if>	
									<xsl:if test="*/otherConstraints">
									<dt>Other Restrictions</dt>
									<dd>
										<xsl:value-of select="*/otherConstraints"/>
									</dd>
									</xsl:if>
								</dl>
							</dd>
						</xsl:for-each>
				 
							
						<xsl:for-each select="identificationInfo/MD_DataIdentification/status">
								<dt>Status</dt>
								<dd><xsl:value-of select="MD_ProgressCode/@codeListValue"/></dd>
						</xsl:for-each>
							
						<xsl:for-each select="identificationInfo/MD_DataIdentification/resourceMaintenance/MD_MaintenanceInformation/maintenanceAndUpdateFrequency">
							<dt>Maintenance and Update Frequency</dt>
							<dd>
								<xsl:value-of select="MD_MaintenanceFrequencyCode/@codeListValue"/>
							</dd>
						</xsl:for-each>									
							
				 <xsl:for-each select="identificationInfo/MD_DataIdentification/aggregationInfo/MD_AggregateInformation/associationType/DS_AssociationTypeCode[@codeListValue='largerWorkCitation']">
					<dt>Collection</dt>
							<dd>
								<dl>
									<dt>Collection Title</dt>
									<dd><xsl:value-of select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/title"/></dd>
									<dt>URL</dt>
									<dd><xsl:value-of select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/identifier/MD_Identifier/code"/></dd>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='originator']">
									<dt>Originator</dt>
											<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
									</xsl:for-each>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='publisher']">
									<dt>Publisher</dt>
											<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
									</xsl:for-each>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/date">
										<xsl:if test="contains(descendant-or-self::*/dateType/CI_DateTypeCode/@codeListValue,'publication')">
												<dt>Publication Date</dt>
												<dd><xsl:value-of select="CI_Date/date"/></dd>
										</xsl:if>
										<xsl:if test="contains(descendant-or-self::*/dateType/CI_DateTypeCode/@codeListValue,'creation')">
												<dt>Creation Date</dt>
												<dd><xsl:value-of select="CI_Date/date"/></dd>
										</xsl:if>
										<xsl:if test="contains(descendant-or-self::*/dateType/CI_DateTypeCode/@codeListValue,'revision')">
												<dt>Revision Date</dt>
												<dd><xsl:value-of select="CI_Date/date"/></dd>
										</xsl:if>
									</xsl:for-each>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/series/CI_Series">
											<dt>Series</dt>
											<dd>
													<dl>
															<dd>
																	<dt>Series Title</dt>
																	<dd><xsl:value-of select="ancestor-or-self::*/name"/></dd>
																	<dt>Issue</dt>
																	<dd><xsl:value-of select="ancestor-or-self::*/issueIdentification"/></dd>
															</dd>
													</dl>
											</dd>
									</xsl:for-each>
								</dl>
							</dd>
						</xsl:for-each>
							
						<xsl:for-each select="identificationInfo/MD_DataIdentification/aggregationInfo/MD_AggregateInformation/associationType/DS_AssociationTypeCode[@codeListValue='crossReference']">
							<dt>Cross Reference</dt>
							<dd>
								<dl>
									<dt>Title</dt>
									<dd><xsl:value-of select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/title"/></dd>
									<dt>URL</dt>
									<dd><xsl:value-of select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/identifier/MD_Identifier/code"/></dd>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='originator']">
										<dt>Originator</dt>
										<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
									</xsl:for-each>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='publisher']">
										<dt>Publisher</dt>
										<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
									</xsl:for-each>
									<xsl:for-each select="ancestor-or-self::*/aggregateDataSetName/CI_Citation/date">
										<xsl:if test="contains(descendant-or-self::*/dateType/CI_DateTypeCode/@codeListValue,'publication')">
											<dt>Publication Date</dt>
											<dd><xsl:value-of select="CI_Date/date"/></dd>
										</xsl:if>
										<xsl:if test="contains(descendant-or-self::*/dateType/CI_DateTypeCode/@codeListValue,'creation')">
											<dt>Creation Date</dt>
											<dd><xsl:value-of select="CI_Date/date"/></dd>
										</xsl:if>
										<xsl:if test="contains(descendant-or-self::*/dateType/CI_DateTypeCode/@codeListValue,'revision')">
											<dt>Revision Date</dt>
											<dd><xsl:value-of select="CI_Date/date"/></dd>
										</xsl:if>
									</xsl:for-each>
								</dl>
							</dd>
						</xsl:for-each> 
						<dt>Language</dt>
						<dd>
							<xsl:value-of select="identificationInfo/MD_DataIdentification/language"/>
						</dd>
			 
						<xsl:if test="identificationInfo/MD_DataIdentification/credit">
							<dt>Credit</dt>
							<dd><xsl:value-of select="identificationInfo/MD_DataIdentification/credit"/></dd>
						</xsl:if>
						
						<xsl:for-each select="identificationInfo/MD_DataIdentification/pointOfContact">
							<dt>Point of Contact</dt>
							<dd>
								<dl>
									<xsl:for-each select="CI_ResponsibleParty">
										<dt>Contact</dt>
										<dd>
											<xsl:value-of select="organisationName | individualName"/>
										</dd>
										<xsl:if test="positionName">
											<dt>Position Name</dt>
											<dd>
													<xsl:value-of select="positionName"/>
											</dd>	
										</xsl:if>
										
										<xsl:if test="contactInfo/CI_Contact/address/CI_Address/deliveryPoint">
											<dt>Delivery Point</dt>
											<dd>
													<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/deliveryPoint"/>
											</dd>
										</xsl:if>	 
										
										<xsl:if test="contactInfo/CI_Contact/address/CI_Address/city">
											<dt>City</dt>
											<dd>
													<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/city"/>
											</dd>
										</xsl:if>	
										<xsl:if test="contactInfo/CI_Contact/address/CI_Address/administrativeArea">
											<dt>Administrative Area</dt>
											<dd>
													<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/administrativeArea"/>
											</dd>
										</xsl:if>
										<xsl:if test="contactInfo/CI_Contact/address/CI_Address/postalCode">
											<dt>Postal Code</dt>
											<dd><xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/postalCode"/></dd>
										</xsl:if>
										<xsl:if test="contactInfo/CI_Contact/address/CI_Address/country">
											<dt>Country</dt>
											<dd>
													<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/country"/>
											</dd>
										</xsl:if>
										<xsl:if test="contactInfo/CI_Contact/address/CI_Address/electronicMailAddress">
											<dt>Email</dt>
											<dd>
													<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/electronicMailAddress"/>
											</dd>
										</xsl:if>
										<xsl:if test="contactInfo/CI_Contact/phone/CI_Telephone/voice">
											<dt>Phone</dt>
											<dd>
													<xsl:value-of select="contactInfo/CI_Contact/phone/CI_Telephone/voice"/>
											</dd>
										</xsl:if>		
									</xsl:for-each>
								</dl>
							</dd>
						</xsl:for-each>
					</dl>
				</dd>
			</dl>
		</div>
					
		<!-- Spatial Reference Info -->
		<A name="iso-spatial-reference-info">
			<HR />
		</A>			
		<xsl:if test="referenceSystemInfo">
			<div id="iso-spatial-reference-info">
				<dt class="metadataMajor">Spatial Reference Information</dt>
				<dd class="metadataMajor-Content">
					<dl>
						<dt>Reference System Identifier</dt>
						<dd>
							<dl>
								<dt>Code</dt>
								<dd><xsl:value-of select="referenceSystemInfo/MD_ReferenceSystem/referenceSystemIdentifier/RS_Identifier/code"/></dd>
								<dt>Code Space</dt>
								<dd><xsl:value-of select="referenceSystemInfo/MD_ReferenceSystem/referenceSystemIdentifier/RS_Identifier/codeSpace"/></dd>
								<dt>Version</dt>
								<dd><xsl:value-of select="referenceSystemInfo/MD_ReferenceSystem/referenceSystemIdentifier/RS_Identifier/version"/></dd>
							</dl>
						</dd>
					</dl>
				</dd>
			</div>
		</xsl:if>
					
		<!-- Data Quality Info -->
		<A name="iso-data-quality-info">
			<HR />
		</A>	
		<xsl:if test="dataQualityInfo/DQ_DataQuality">
			<div id="iso-data-quality-info">
				<dt class="metadataMajor">Data Quality Information</dt>
				<dd class="metadataMajor-Content">
					<dl>
						<xsl:if test="DQ_Scope/level">
							<dt>Hierarchy Level</dt>
							<dd>
									<xsl:value-of select="DQ_Scope/level/MD_ScopeCode[@codeListValue]"/>
							</dd>
						</xsl:if>
						<xsl:for-each select="dataQualityInfo/DQ_DataQuality/report">
							<xsl:if test="DQ_QuantitativeAttributeAccuracy">
								<dt>Quantitative Attribute Accuracy Report</dt>
								<dd>
									<dl>
										<xsl:if test="DQ_QuantitativeAttributeAccuracy/evaluationMethodDescription/text()">
											<dt>Evaluation Method</dt>
											<dd><xsl:value-of select="DQ_QuantitativeAttributeAccuracy/evaluationMethodDescription"/></dd>
										</xsl:if>
										<xsl:if test="DQ_QuantitativeAttributeAccuracy/result/text()">
											<dt>Result</dt>
											<dd><xsl:value-of select="DQ_QuantitativeAttributeAccuracy/result"/></dd>
										</xsl:if>
									</dl>
								</dd>
							</xsl:if>
								
							<xsl:if test="DQ_AbsoluteExternalPositionalAccuracy">
								<dt>Absolute External Positional Accuracy</dt>
								<dd>
									<dl>
										<xsl:if test="DQ_AbsoluteExternalPositionalAccuracy/evaluationMethodDescription/text()">
											<dt>Evaluation Method</dt>
											<dd><xsl:value-of select="DQ_AbsoluteExternalPositionalAccuracy/evaluationMethodDescription"/></dd>
										</xsl:if>
										<xsl:if test="DQ_AbsoluteExternalPositionalAccuracy/result/text()">
											<dt>Result</dt>
											<dd><xsl:value-of select="DQ_AbsoluteExternalPositionalAccuracy/result"/></dd>
										</xsl:if>
									</dl>
								</dd>
							</xsl:if>
							
							<xsl:if test="DQ_CompletenessCommission">
								<dt>Completeness Commission</dt>
								<dd>
									<dl>
										<xsl:if test="DQ_CompletenessCommission/evaluationMethodDescription/text()">
											<dt>Evaluation Method</dt>
											<dd><xsl:value-of select="DQ_CompletenessCommission/evaluationMethodDescription"/></dd>
										</xsl:if>
										<xsl:if test="DQ_CompletenessCommission/result/text()">
											<dt>Result</dt>
											<dd><xsl:value-of select="DQ_CompletenessCommission/result"/></dd>
										</xsl:if>
									</dl>
								</dd>
							</xsl:if>
						</xsl:for-each>		
						<xsl:for-each select="dataQualityInfo/DQ_DataQuality/lineage/LI_Lineage">
								<dt>Lineage</dt>
								<dd>
									<dl>
										<xsl:if test="statement">
											<dt>Statement</dt>
											<dd>
												<xsl:value-of select="statement"/>
											</dd>
										</xsl:if>
										<xsl:for-each select="processStep/LI_ProcessStep">
											<dt>Process Step</dt>
											<dd>
												<dl>
													<xsl:if test="description">
													<dt>Description</dt>
													<dd>
														<xsl:value-of select="description"/>
													</dd>
													</xsl:if>
													
													<xsl:for-each select="CI_ResponsibleParty">
														<dt>Processor</dt>
														<dd>
																<xsl:value-of select="individualName | organisationName"/>
														</dd>
													</xsl:for-each>

													<xsl:if test="dateTime">
														<dt>Process Date</dt>
														<dd>
																<xsl:value-of select="dateTime"/>
														</dd>
													</xsl:if>
												</dl>
											</dd>
										</xsl:for-each>
										<xsl:for-each select="source/LI_Source/sourceCitation"> 
											<dt>Source</dt>
											<dd>
												<dl>
													<dt>Title</dt>
													<dd>
														<xsl:value-of select="CI_Citation/title"/>
													</dd>
													<xsl:for-each select="CI_Citation/date/CI_Date">
														<xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'publication')">
																<dt>Publication Date</dt>
																<dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
														</xsl:if>
														<xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'creation')">
																<dt>Creation Date</dt>
																<dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
														</xsl:if>
														<xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'revision')">
																<dt>Revision Date</dt>
																<dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
														</xsl:if>
													</xsl:for-each>
													<xsl:for-each select="CI_Citation/citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='originator']">
														<dt>Originator</dt>
														<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
													</xsl:for-each>
													
													<xsl:for-each select="CI_Citation/citedResponsibleParty/CI_ResponsibleParty/role/CI_RoleCode[@codeListValue='publisher']">
														<dt>Publisher</dt>
														<dd><xsl:value-of select="ancestor-or-self::*/organisationName | ancestor-or-self::*/individualName"/></dd>
														<xsl:if test="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/city">
															<dt>Place of Publication</dt>	 
															<dd><xsl:value-of select="ancestor-or-self::*/contactInfo/CI_Contact/address/CI_Address/city"/></dd>		
														</xsl:if>
													</xsl:for-each>
													
													<xsl:if test="CI_Citation/identifier/MD_Identifier/code">
														<dt>Identifier</dt>
														<dd><xsl:value-of select="CI_Citation/identifier/MD_Identifier/code"/></dd>
													</xsl:if>
													<xsl:if test="ancestor-or-self::*/description">
														<dt>Description</dt>
														<dd><xsl:value-of select="ancestor-or-self::*/description"/></dd>
													</xsl:if>
												</dl>
											</dd>
										</xsl:for-each>
									</dl>
								</dd>
						</xsl:for-each>
							
					</dl>
				</dd>
			</div>		 
		</xsl:if>		
					
		<!-- Distribution -->
		<A name="iso-distribution-info">
			<HR />
		</A>			
		<xsl:if test="distributionInfo">
			<div id="iso-distribution-info">
				<dt class="metadataMajor">Distribution Information</dt>
				<dd class="metadataMajor-Content">
					<dl>
						<xsl:if test="distributionInfo/MD_Distribution/distributionFormat/MD_Format">
							<dt>Format Name</dt>
							<dd>
									<xsl:value-of select="distributionInfo/MD_Distribution/distributionFormat/MD_Format/name"/>
							</dd>
							<xsl:if test="distributionInfo/MD_Distribution/distributionFormat/MD_Format/version/text()">
								<dt>Format Version</dt>
								<dd>
									<xsl:value-of select="distributionInfo/MD_Distribution/distributionFormat/MD_Format/version"/>
								</dd>
							</xsl:if>		
						</xsl:if>		
							
						<xsl:for-each select="distributionInfo/MD_Distribution/distributor/MD_Distributor">
						<dt>Distributor</dt>
						<dd>
							<xsl:value-of select="distributorContact/CI_ResponsibleParty/organisationName"/>
						</dd>
						</xsl:for-each>
							
						<xsl:for-each select="distributionInfo/MD_Distribution/transferOptions/MD_DigitalTransferOptions">
							<dt>Online Access</dt>
							<dd>
								<xsl:value-of select="onLine/CI_OnlineResource/linkage/URL"/>
							</dd>
							<dt>Protocol</dt>
							<dd>
								<xsl:value-of select="onLine/CI_OnlineResource/protocol"/>
							</dd>
							<dt>Name</dt>
							<dd>
								<xsl:value-of select="onLine/CI_OnlineResource/name"/>
							</dd>
							<xsl:if test="onLine/CI_OnlineResource/function/CI_OnLineFunctionCode/@codeListValue">
							<dt>Function</dt>
							<dd>
								<xsl:value-of select="onLine/CI_OnlineResource/function/CI_OnLineFunctionCode/@codeListValue"/>
							</dd>
							</xsl:if>
							<xsl:if test="distributionInfo/MD_Distribution/transferOptions/MD_DigitalTransferOptions/transferSize">
								<dt>Transfer Size</dt>
								<dd><xsl:value-of select="distributionInfo/MD_Distribution/transferOptions/MD_DigitalTransferOptions/transferSize"/></dd>
							</xsl:if>
						</xsl:for-each>
					</dl>
				</dd>
			</div>
		</xsl:if>

		<!-- Content Info -->
		<A name="iso-content-info">
			<HR />
		</A>	
		<xsl:if test="contentInfo">
			<div id="iso-content-info">
				<dt class="metadataMajor">Content Information</dt>
				<dd class="metadataMajor-Content">
					<dl>
						<xsl:if test="contentInfo/MD_FeatureCatalogueDescription">
							<dt>Feature Catalog Description</dt>
							<dd>
								<dl>
									<dt>Compliance Code</dt>
									<dd><xsl:value-of select="contentInfo/MD_FeatureCatalogueDescription/complianceCode"/></dd>
									<dt>Language</dt>
									<dd><xsl:value-of select="contentInfo/MD_FeatureCatalogueDescription/language"/></dd>
									<dt>Included With Dataset</dt>
									<dd><xsl:value-of select="contentInfo/MD_FeatureCatalogueDescription/includedWithDataset"/></dd>
									<dt>Feature Catalog Citation</dt>
									<dd>
										<dl>
											<dt>Title</dt>
											<dd>
												<xsl:value-of select="contentInfo/MD_FeatureCatalogueDescription/featureCatalogueCitation/CI_Citation/title"/>
											</dd>
											
											<xsl:for-each select="contentInfo/MD_FeatureCatalogueDescription/featureCatalogueCitation/CI_Citation/date/CI_Date">
												<xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'publication')">
													<dt>Publication Date</dt>
													<dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
												</xsl:if>
												<xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'creation')">
													<dt>Creation Date</dt>
													<dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
												</xsl:if>
												<xsl:if test="contains(dateType/CI_DateTypeCode/@codeListValue,'revision')">
													<dt>Revision Date</dt>
													<dd><xsl:value-of select="ancestor-or-self::*/date/CI_Date/date"/></dd>
												</xsl:if>
											</xsl:for-each>
											<dt>Feature Catalog Identifier</dt>
											<dd><xsl:value-of select="contentInfo/MD_FeatureCatalogueDescription/featureCatalogueCitation/CI_Citation/identifier/MD_Identifier/code"/></dd>
										</dl>
									</dd>
								</dl>
							</dd>
						</xsl:if>
						<xsl:if test="contentInfo/MD_ImageDescription">
							<dt>Content Type</dt>
							<dd>
								<xsl:value-of select="contentInfo/MD_ImageDescription/contentType/MD_CoverageContentTypeCode[@codeListValue]"/>
							</dd>
						</xsl:if>
					</dl>
				</dd>
			</div>
		</xsl:if>		 
		
		<!-- Spatial Representation -->	 
		<A name="iso-spatial-representation-info">
			<HR />
		</A>	
		<xsl:if test="spatialRepresentationInfo">
			<div id="iso-spatial-representation-info">
				<dt class="metadataMajor">Spatial Representation Information</dt>
				<dd class="metadataMajor-Content">
					<dl>
						<xsl:choose>
							<xsl:when test="spatialRepresentationInfo/MD_VectorSpatialRepresentation">
								<dt>Vector</dt>
								<dd>
									<dl>
										<dt>Topology Level</dt>
										<dd>
											<xsl:value-of select="spatialRepresentationInfo/MD_VectorSpatialRepresentation/topologyLevel/MD_TopologyLevelCode[@codeListValue]"/>
										</dd>
										<dt>Vector Object Type</dt>
										<dd>
											<xsl:value-of select="spatialRepresentationInfo/MD_VectorSpatialRepresentation/geometricObjects/MD_GeometricObjects/geometricObjectType/MD_GeometricObjectTypeCode[@codeListValue]"/>
										</dd>
										<dt>Vector Object Count</dt>
										<dd>
											<xsl:value-of select="spatialRepresentationInfo/MD_VectorSpatialRepresentation/geometricObjects/MD_GeometricObjects/geometricObjectCount"/>
										</dd>
									</dl>		 
								</dd>
							</xsl:when>
							
							<xsl:when test="spatialRepresentationInfo/MD_GridSpatialRepresentation">
								<dt>Raster</dt>
								<dd>
									<dl>
										<xsl:if test="spatialRepresentationInfo/MD_GridSpatialRepresentation/numberOfDimensions">
												<dt>Number of Dimensions</dt>
												<dd>
														<xsl:value-of select="spatialRepresentationInfo/MD_GridSpatialRepresentation/numberOfDimensions"/>
												</dd>
										</xsl:if>
										<dd>
											<dl>
												<xsl:for-each select="spatialRepresentationInfo/MD_GridSpatialRepresentation/axisDimensionProperties/MD_Dimension">
													<xsl:if test="dimensionName/MD_DimensionNameTypeCode/@codeListValue='column'">
														<dt>Column Count</dt>
														<dd>
															<xsl:value-of select="dimensionSize"/>
														</dd>
													</xsl:if>
														
													<xsl:if test="dimensionName/MD_DimensionNameTypeCode/@codeListValue='row'">
														<dt>Row Count</dt>
														<dd>
															<xsl:value-of select="dimensionSize"/>
														</dd>
													</xsl:if>
												</xsl:for-each>
												
												<xsl:if test="spatialRepresentationInfo/MD_GridSpatialRepresentation/cellGeometry/MD_CellGeometryCode">
													<dt>Cell Geometry Type</dt>
													<dd>
														<xsl:value-of select="spatialRepresentationInfo/MD_GridSpatialRepresentation/cellGeometry/MD_CellGeometryCode/@codeListValue"/>
													</dd>
												</xsl:if>
												
												<xsl:if test="spatialRepresentationInfo/MD_GridSpatialRepresentation/cornerPoints">
													<dt>Corner Points</dt>
													<dd>
														<dl>
															<xsl:for-each select="spatialRepresentationInfo/MD_GridSpatialRepresentation/cornerPoints/gml:Point">
																<dt>Point</dt>
																<dd><xsl:value-of select="gml:pos"/></dd>
															</xsl:for-each>
														</dl>
													</dd>
														
													<xsl:for-each select="spatialRepresentationInfo/MD_GridSpatialRepresentation/centerPoint/gml:Point">
														<dt>Center Point</dt>
														<dd><xsl:value-of select="gml:pos"/></dd>
													</xsl:for-each>
												</xsl:if>
											</dl>
										</dd>
									</dl>		 
								</dd>
							</xsl:when>
							<xsl:when test="spatialRepresentationInfo/MD_Georectified">
								<dt>Raster</dt>
								<dd>
									<dl>
										<xsl:if test="spatialRepresentationInfo/MD_Georectified/numberOfDimensions">
												<dt>Number of Dimensions</dt>
												<dd>
														<xsl:value-of select="spatialRepresentationInfo/MD_Georectified/numberOfDimensions"/>
												</dd>
										</xsl:if>
											
										<xsl:for-each select="spatialRepresentationInfo/MD_Georectified/axisDimensionProperties/MD_Dimension">
											<xsl:if test="dimensionName/MD_DimensionNameTypeCode/@codeListValue='column'">
													<dt>Column Count</dt>
														<dd>
														<xsl:value-of select="dimensionSize"/>
														</dd>
											</xsl:if>

											<xsl:if test="dimensionName/MD_DimensionNameTypeCode/@codeListValue='row'">
												<dt>Row Count</dt>
												<dd>
													<xsl:value-of select="dimensionSize"/>
												</dd>
											</xsl:if>
										</xsl:for-each>
											
										<xsl:if test="spatialRepresentationInfo/MD_Georectified/cellGeometry/MD_CellGeometryCode">
											<dt>Cell Geometry Type</dt>
											<dd>
												<xsl:value-of select="spatialRepresentationInfo/MD_Georectified/cellGeometry/MD_CellGeometryCode/@codeListValue"/>
											</dd>
										</xsl:if>
											
										<xsl:if test="spatialRepresentationInfo/MD_Georectified/cornerPoints">
											<dt>Corner Points</dt>
											<dd>
												<dl>
													<xsl:for-each select="spatialRepresentationInfo/MD_Georectified/cornerPoints/gml:Point">
														<dt>Point</dt>
														<dd><xsl:value-of select="gml:pos"/></dd>
													</xsl:for-each>
												</dl>
											</dd>
											
											<xsl:for-each select="spatialRepresentationInfo/MD_Georectified/centerPoint/gml:Point">
												<dt>Center Point</dt>
												<dd><xsl:value-of select="gml:pos"/></dd>
											</xsl:for-each>
										</xsl:if>
									</dl>		 
								</dd>
							</xsl:when>
						</xsl:choose>
					</dl>
				</dd>
			</div>
		</xsl:if>						

		<!-- Metadata Reference Info -->
		<A name="iso-metadata-reference-info">
			<HR />
		</A>
		<div id="iso-metadata-reference-info">
			<dt class="metadataMajor">Metadata Reference Information</dt>		
			<dd class="metadataMajor-Content">
				<dl>			
					<dt>Hierarchy Level</dt>
					<dd>
						<xsl:value-of select="hierarchyLevelName"/>
					</dd>		
					<dt>Metadata File Identifier</dt>
					<dd>
						<xsl:value-of select="fileIdentifier"/>
					</dd>			 
					<xsl:if test="parentIdentifier">
						<dt>Parent Identifier</dt>
						<dd>
							<xsl:value-of select="parentIdentifier"/>
						</dd>	
					</xsl:if>
					<xsl:if test="dataSetURI">
						<dt>Dataset URI</dt>
						<dd>
							<xsl:value-of select="dataSetURI"/>
						</dd> 
					</xsl:if>	
					<xsl:for-each select="metadataMaintenance/MD_MaintenanceInformation/contact">						
						<dt>Metadata Point of Contact</dt>						 
						<dd>
							<dl>
								<xsl:for-each select="CI_ResponsibleParty">
									<dt>Name</dt>
									<dd>
										<xsl:value-of select="organisationName | individualName"/>
									</dd>
									<xsl:if test="positionName">
										<dt>Position Name</dt>
										<dd>
											<xsl:value-of select="positionName"/>
										</dd>	
									</xsl:if>
									<xsl:if test="contactInfo/CI_Contact/address/CI_Address/deliveryPoint">
										<dt>Delivery Point</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/deliveryPoint"/>
										</dd>
									</xsl:if>	 
									<xsl:if test="contactInfo/CI_Contact/address/CI_Address/city">
										<dt>City</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/city"/>
										</dd>
									</xsl:if>	
									<xsl:if test="contactInfo/CI_Contact/address/CI_Address/administrativeArea">
										<dt>Administrative Area</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/administrativeArea"/>
										</dd>
									</xsl:if>
									<xsl:if test="contactInfo/CI_Contact/address/CI_Address/postalCode">
										<dt>Postal Code</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/postalCode"/>
										</dd>
									</xsl:if>
									<xsl:if test="contactInfo/CI_Contact/address/CI_Address/country">
										<dt>Country</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/country"/>
										</dd>
									</xsl:if>
									<xsl:if test="contactInfo/CI_Contact/address/CI_Address/electronicMailAddress">
										<dt>Email</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/address/CI_Address/electronicMailAddress"/>
										</dd>
									</xsl:if>
									<xsl:if test="contactInfo/CI_Contact/phone/CI_Telephone/voice">
										<dt>Phone</dt>
										<dd>
											<xsl:value-of select="contactInfo/CI_Contact/phone/CI_Telephone/voice"/>
										</dd>
									</xsl:if>		
								</xsl:for-each>
							</dl>
						</dd>
					</xsl:for-each>
					<dt>Metadata Date Stamp</dt>
					<dd>
						<xsl:value-of select="dateStamp"/>
					</dd>
					<dt>Metadata Standard Name</dt>		 
					<dd>
						<xsl:value-of select="metadataStandardName"/>
					</dd>
					<dt>Metadata Standard Version</dt>
					<dd>
						<xsl:value-of select="metadataStandardVersion"/>
					</dd>		
					<xsl:if test="characterSet/MD_CharacterSetCode[@codeListValue]/text()">
						<dt>Character Set</dt>
						<dd>
							<xsl:value-of select="characterSet/MD_CharacterSetCode[@codeListValue]"/>
						</dd>	
					</xsl:if>
				</dl>
			</dd>				
		</div>
	</xsl:template> 
</xsl:stylesheet>
