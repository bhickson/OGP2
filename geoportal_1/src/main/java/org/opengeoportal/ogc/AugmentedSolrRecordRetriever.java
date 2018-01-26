package org.opengeoportal.ogc;

import org.opengeoportal.solr.SolrRecord;

public interface AugmentedSolrRecordRetriever {

	OwsInfo getWmsInfo(String layerSlug) throws Exception;

	AugmentedSolrRecord getWmsPlusSolrInfo(String layerSlug) throws Exception;

	OwsInfo getOgcDataInfo(String layerSlug) throws Exception;

	AugmentedSolrRecord getOgcAugmentedSolrRecord(String layerSlug)
			throws Exception;

	AugmentedSolrRecord getOgcAugmentedSolrRecord(SolrRecord solrRecord)
			throws Exception;

}
