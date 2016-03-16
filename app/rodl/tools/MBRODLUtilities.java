package rodl.tools;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.FacetField;
import org.purl.wf4ever.rosrs.client.exception.SearchException;
import org.purl.wf4ever.rosrs.client.search.SearchServer;
import org.purl.wf4ever.rosrs.client.search.SearchServer.SortOrder;
import org.purl.wf4ever.rosrs.client.search.SolrSearchServer;
import org.purl.wf4ever.rosrs.client.search.dataclasses.FacetValue;
import org.purl.wf4ever.rosrs.client.search.dataclasses.FoundRO;
import org.purl.wf4ever.rosrs.client.search.dataclasses.SearchResult;
import org.purl.wf4ever.rosrs.client.search.dataclasses.solr.FacetEntry;
import org.purl.wf4ever.rosrs.client.search.utils.SolrQueryBuilder;

import play.Play;
import rodl.services.MyQueryFactory;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Various utility methods for RODL, not related directly to its REST APIs.
 * 
 * @author piotrekhol
 * 
 */
public final class MBRODLUtilities {

    /** Logger. */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(MBRODLUtilities.class);


    /**
     * Private constructor.
     */
    private MBRODLUtilities() {
        //nope
    }


    /**
     * Get most recent Research Objects.
     * 
     * @param cnt
     *            number of ROs to get
     * @param sparqlEndpoint
     *            sparql endpoint URI
     * @return list of research objects
     * @throws IOException
     *             when cannot connect to SPARQL endpoint
     */
    public static List<URI> getMostRecentROs(URI sparqlEndpoint, int cnt)
            throws IOException {
       /* QueryExecution x = QueryExecutionFactory.sparqlService(sparqlEndpoint.toString(),
            MyQueryFactory.getxMostRecentROs(cnt));*/
    	Play.application().classloader().getResource("xMostRecentROs.sparql");
    	//String stringFromInputStream = IOUtils.toString(is, "UTF-8");
    	
    	QueryExecution x = QueryExecutionFactory.sparqlService(sparqlEndpoint.toString(),MyQueryFactory.getxMostRecentROs(cnt));
    	ResultSet results = x.execSelect();
        List<URI> roHeaders = new ArrayList<>();
        while (results.hasNext()) {
            QuerySolution solution = results.next();
            if (solution.getResource("ro") == null) {
                continue;
            }
            roHeaders.add(URI.create(solution.getResource("ro").getURI()));
        }
        return roHeaders;
    }
    /**
     * Searching RO's by keyword from parameter
     * @return
     * @throws URISyntaxException 
     */
    public static SearchResult getROsByKeyword(String keyword, List<FacetValue> selectedFacetValues) throws URISyntaxException
    {
    	
    	SearchServer searchServer = createSearchServer();
    	List<FoundRO> found;
    	 SearchResult searchResult = null;
    	boolean emptySearch = keyword == null || keyword.isEmpty();
        
    	try {
        String query = emptySearch ? buildQuery("*:*", selectedFacetValues) : buildQuery( SolrQueryBuilder.escapeString(keyword), selectedFacetValues);
        searchResult = searchServer.search(query, null, null, getSortFields());
            
            found = searchResult.getROsList();
        } catch (SearchException e) {
        	play.Logger.debug("Wystapil blad",e.getCause());
        	found = new ArrayList<>();
        }
    	return searchResult;
    }
    
    /**
     * Build a query using AND and OR predicates.
     * 
     * @param keywords
     *            the keywords to look for
     * @param facetValues
     *            the facets to filter by
     * @return the query
     */
    private static String buildQuery(String keywords, List<FacetValue> facetValues) {
        Map<String, String> queryMap = new HashMap<>();
        if (facetValues != null) {
            for (FacetValue value : facetValues) {
                if (queryMap.containsKey(value.getParamName())) {
                    String queryPart = queryMap.get(value.getParamName()) + " OR " + value.getQuery();
                    queryMap.put(value.getParamName(), queryPart);
                } else {
                    queryMap.put(value.getParamName(), value.getQuery());
                }
            }
        }
        String finalQuery = keywords;
        for (String key : queryMap.keySet()) {
            finalQuery += " AND (" + queryMap.get(key) + ")";
        }
        return finalQuery;
    }
    private static  Map<String, SearchServer.SortOrder> getSortFields() {
      SortOption sortOption = new SortOption(new FacetEntry(new FacetField("created"), "Creation date"),
    	            SortOrder.DESC);
        Map<String, SearchServer.SortOrder> sortFields = new HashMap<>();
        if (sortOption != null) {
            sortFields.put(sortOption.getFacetEntry().getFieldName(), sortOption.getSortOrder());
        }
        return sortFields;
    }
    
    /**
     * Initialize a search service client depending on the field {@link SearchType}.
     * 
     * @return opensearch, sparql or Solr endpoint, default is Solr
     * @throws URISyntaxException 
     */
    private static SearchServer createSearchServer() throws URISyntaxException {
    	URI searchEndpointURI = new URI("http://sandbox.wf4ever-project.org/solr/");
    	return new SolrSearchServer(searchEndpointURI);
    }
    
    public static String getROSketch(URI sparqlEndpoint, String ro)
    {
    	String sketchURI="N/A";
    	QueryExecution x;
		try {
			x = QueryExecutionFactory.sparqlService(sparqlEndpoint.toString(),
			        MyQueryFactory.getROSketchQuery(ro));
	    	ResultSet results = x.execSelect();
	    	if (results.hasNext()) {
	    		sketchURI=results.next().getResource("resource").getURI().toString();
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	        
    	return (sketchURI);
    	
    }
    
}
