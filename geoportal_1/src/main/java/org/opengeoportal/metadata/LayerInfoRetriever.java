package org.opengeoportal.metadata;

import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.opengeoportal.solr.SolrRecord;

public interface LayerInfoRetriever {
	public List<SolrRecord> fetchAllLayerInfo(Set<String> layerSlugs) throws Exception;
	SolrRecord getAllLayerInfo(String layerSlug) throws Exception;
	SolrServer getSolrServer();
	List<SolrRecord> fetchAllowedRecords(Set<String> layerSlugSet) throws Exception;

}
