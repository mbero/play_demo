package controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.purl.wf4ever.rosrs.client.Person;
import org.purl.wf4ever.rosrs.client.ROSRService;
import org.purl.wf4ever.rosrs.client.ResearchObject;
import org.purl.wf4ever.rosrs.client.exception.ROException;
import org.purl.wf4ever.rosrs.client.exception.ROSRSException;
import org.purl.wf4ever.rosrs.client.search.dataclasses.FoundRO;
import org.purl.wf4ever.rosrs.client.search.dataclasses.SearchResult;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import rodl.tools.MBRODLUtilities;

import com.fasterxml.jackson.databind.JsonNode;
public class ROController extends Controller{

    
    public Result getRecentOUsNames() throws ROSRSException, ROException, URISyntaxException
    {
    	List<String> listOfRONames = new ArrayList<String>();
    	URI uri = new URI ("http://sandbox.wf4ever-project.org/rodl/sparql");
		List<URI> recentOUS = new ArrayList<URI>();
		try 
		{
			ROSRService rosrs = new ROSRService(uri.resolve("ROs/"), null);
			recentOUS = MBRODLUtilities.getMostRecentROs(uri, 10);
		    for (URI currentURI : recentOUS)
		    {
		    	 ResearchObject ro = new ResearchObject(currentURI, rosrs);  
		    	 String currentRoName = ro.getName();
		    	 listOfRONames.add(currentRoName);
			    }
		} catch (IOException e) {
			Logger.debug("getRecentOUs error", e);
			e.printStackTrace();
		}
		
    	JsonNode json = Json.toJson(listOfRONames);
    	return ok(json);
    }
    
    public Result getOUsByKeyword(String keyword)
    {
    	List<ResearchObject> listOfFoundedRo = new ArrayList<ResearchObject>();
    	SearchResult searchResult = null;
    	try {
    		 searchResult = MBRODLUtilities.getROsByKeyword(keyword, null);
		} catch (URISyntaxException e) {
			Logger.debug("wystapil blad -getOUsByKeyword" , e );
			e.printStackTrace();
		}
    	List<FoundRO> foundedROs = searchResult.getROsList();
    	for(FoundRO foundedRO : foundedROs)
    	{
    		ResearchObject ro = foundedRO.getResearchObject();
    		listOfFoundedRo.add(ro);
    	}
    	
    	JsonNode json = Json.toJson(listOfFoundedRo);
    	return ok(json);
    }

  
}
