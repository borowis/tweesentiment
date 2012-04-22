package gae.sentapp.sentiment;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class DAOHelper {
	private static final Logger LOG = Logger.getLogger("gae.sentapp.sentiment");
	public static final String ENTITY_TYPE = "Words";
	
	public static final String PR_COUNT = "prCount";
	public static final String PR_WORD = "prWord";
	
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public Integer searchInDataStore(String word1, String word2) {
		return searchInDataStore(word1 + "||" + word2);
	}
	
	public Integer searchInDataStore(String word) {
		LOG.log(Level.INFO, "word requested " + word);
		Query wordPhraseQuery = new Query(ENTITY_TYPE);
		wordPhraseQuery.addFilter(PR_WORD, FilterOperator.EQUAL, word);
		List<Entity> words = datastore.prepare(wordPhraseQuery).asList(FetchOptions.Builder.withDefaults());
		if (words.isEmpty()) { 
			return -1;
		} 
		Entity wordEntity = words.get(0);
		Long count = (Long) wordEntity.getProperty(PR_COUNT);
		return count.intValue();	
	}
	
	public void saveInDataStore(String word1, String word2, Integer hitCount) {
		saveInDataStore(word1 + "||" + word2, hitCount);
	}
	
	public void saveInDataStore(String word, Integer hitCount) {
		Entity entity = new Entity(ENTITY_TYPE, createKeyForWord(word));
		entity.setProperty(PR_COUNT, hitCount);
		entity.setProperty(PR_WORD, word);
		datastore.put(entity);
	}
	
	
	private String createKeyForWord(String word) {
        return word;
	}
}
