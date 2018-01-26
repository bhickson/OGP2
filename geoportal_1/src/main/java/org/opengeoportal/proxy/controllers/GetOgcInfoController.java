package org.opengeoportal.proxy.controllers;


import org.opengeoportal.metadata.LayerInfoRetriever;
import org.opengeoportal.ogc.AugmentedSolrRecord;
import org.opengeoportal.ogc.AugmentedSolrRecordRetriever;
import org.opengeoportal.ogc.OwsInfo;
import org.opengeoportal.solr.SolrRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/info")
public class GetOgcInfoController {

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AugmentedSolrRecordRetriever augmentedSolrRecordRetriever;

	@Autowired
	LayerInfoRetriever layerInfoRetriever;
	
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	public @ResponseBody AugmentedSolrRecord getLayerInfo(@RequestParam("ogpid") String layerSlug) throws Exception {
		return augmentedSolrRecordRetriever.getOgcAugmentedSolrRecord(layerSlug);

	}
	
	@RequestMapping(value="wmsInfo", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody OwsInfo wmsInfo(@RequestParam("ogpid") String layerSlug) throws Exception {
		return augmentedSolrRecordRetriever.getWmsInfo(layerSlug);
	}
	
	@RequestMapping(value="ogcData", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody OwsInfo ogcDataInfo(@RequestParam("ogpid") String layerSlug) throws Exception {
		return augmentedSolrRecordRetriever.getOgcDataInfo(layerSlug);
	}
	
	@RequestMapping(value="ogp", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody SolrRecord ogcSolrInfo(@RequestParam("ogpid") String layerSlug, @RequestParam(value="full", defaultValue="false") Boolean includeMetadata) throws Exception {
		SolrRecord record = layerInfoRetriever.getAllLayerInfo(layerSlug);
		if (includeMetadata){
			return record;
		}
	       	else {
			//record.setFgdcText(null);
			
			return record;
		}

	}

}
