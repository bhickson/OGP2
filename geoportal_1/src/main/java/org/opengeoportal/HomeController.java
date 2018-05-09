package org.opengeoportal;

import java.util.HashSet;
import java.util.Set;

import org.opengeoportal.config.ogp.OgpConfig;
import org.opengeoportal.config.ogp.OgpConfigRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.owasp.encoder.Encode;


@Controller
public class HomeController {
	@Autowired
	private OgpConfigRetriever ogpConfigRetriever;
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value={"/index", "/"}, method=RequestMethod.GET)
	public ModelAndView getHomePage(
			@RequestParam(value="ogpids", defaultValue = "") Set<String> layerIds,
			@RequestParam(value="collectionId", defaultValue = "") String collectionId,
			@RequestParam(value="bbox", defaultValue = "-180,-90,180,90") String bbox,
			@RequestParam(value="layer[]", defaultValue = "") Set<String> layers,
			@RequestParam(value="minX", defaultValue = "-180") String minx,
			@RequestParam(value="maxX", defaultValue = "180") String maxx,
			@RequestParam(value="minY", defaultValue = "-90") String miny,
			@RequestParam(value="maxY", defaultValue = "90") String maxy,
			@RequestParam(value="dev", defaultValue = "false") Boolean isDev) throws Exception {
		//@RequestParam("ogpids") Set<String> layerIds, ..should be optional.  also a param to set dev vs. prod
		//create the model to return
		ModelAndView mav = new ModelAndView("ogp_home"); 

		mav.addObject("dev", isDev);
		
		// Encode all nputs to prevent XSS attacks
		collectionId = Encode.forHtml(collectionId);
		bbox = Encode.forHtml(bbox);
		minx = Encode.forHtml(minx);
		maxx = Encode.forHtml(maxx);
		miny = Encode.forHtml(miny);
		maxy = Encode.forHtml(maxy);

		// Iterate Sets layerIds and layers to encode content
		Set<String> encoded_layerIds = new HashSet<String>();
		for(String s : layerIds) {
			encoded_layerIds.add(Encode.forHtml(s));
		}
		layerIds.clear();
		layerIds.addAll(encoded_layerIds);

		Set<String> encoded_layers = new HashSet<String>();
		for(String s : layers) {
			encoded_layers.add(Encode.forHtml(s));
		}
		layers.clear();
		layers.addAll(encoded_layers);

		//if ogpids exists, add them to the Model
		
		if (!layerIds.isEmpty()){
			mav.addObject("shareIds", getQuotedSet(layerIds));
			mav.addObject("shareBbox", bbox);
		} else if (!layers.isEmpty()){
			//support old style share just in case
			mav.addObject("shareIds", getQuotedSet(layers));
			mav.addObject("shareBbox", minx + "," + miny + "," + maxx + "," + maxy);
		} else if (!collectionId.isEmpty()){
			mav.addObject("shareIds", layerIds);
			mav.addObject("collectionId", collectionId);
			mav.addObject("shareBbox", bbox);
		} else {
			//default values
			mav.addObject("shareIds", layerIds);
			mav.addObject("shareBbox", bbox);	
		}
		
		
		
		addConfig(mav);
		
		//Debugging
		/*Iterator<Entry<String, Object>> iter = mav.getModelMap().entrySet().iterator();
		  while (iter.hasNext()){
			Entry<String, Object> stuff = iter.next();
			logger.info(stuff.getKey());
			logger.info((String) stuff.getValue());
		}*/

		return mav;

	}
	
	private Set<String> getQuotedSet(Set<String> uqSet){
		Set<String> quotedSet = new HashSet<String>();
		for (String item: uqSet){
			quotedSet.add("\"" + item + "\""); 
		}
		
		return quotedSet;
	}
	
	private void addConfig(ModelAndView mav){
		OgpConfig conf = ogpConfigRetriever.getConfig();
		
		mav.addObject("titlePrimary", conf.getPageTitlePrimary());
		mav.addObject("titleOffset", conf.getPageTitleOffset());
		
		mav.addObject("extraJs", conf.getJsLocalized());	//<script type="text/javascript" src="resources/javascript/dataTables.scroller.min.js"></script>
		mav.addObject("extraCss", conf.getCssLocalized());  //<link rel="stylesheet" href="resources/css/google.css" type="text/css" />
		
		mav.addObject("searchUrl", conf.getSearchUrl().toString());
		mav.addObject("analyticsId", conf.getAnalyticsId());
		
		mav.addObject("loginRepository", conf.getLoginConfig().getRepositoryId());
		mav.addObject("loginType", conf.getLoginConfig().getType());
		mav.addObject("loginUrl", conf.getLoginConfig().getUrl());
		mav.addObject("secureDomain", conf.getLoginConfig().getSecureDomain());
		
	}
}
