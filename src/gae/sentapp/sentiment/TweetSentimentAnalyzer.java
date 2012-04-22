package gae.sentapp.sentiment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.Tweet;

public class TweetSentimentAnalyzer {
	private static final Logger LOG = Logger.getLogger("gae.sentapp.sentiment");
	private SEHelper seHelper = new SEHelper();
	private DAOHelper daoHelper = new DAOHelper();
	
	public String[] strip(Tweet tweet) {
		String text = tweet.getText();
		// normalize text
		text = text.replaceAll("[^а-яА-Я]", " ").replaceAll(" {2,}", " ");
//		LOG.log(Level.INFO, "tweet is " + text);
		String[] stripped = text.split(" ");
		String[] transformed = new String[stripped.length];
		int j = 0;
		for (int i = 0; i < stripped.length; i++) {
			if (stripped[i].length() > 3) {
				transformed[j++] = stripped[i];
			}
		}
		String[] transformed2 = new String[j]; 
		System.arraycopy(transformed, 0, transformed2, 0, j);
		return transformed2;
	}
	
	public Double computePMIForTweet(String[] tweet) {
		Double sum = Double.valueOf(0.0);
		for (int i = 0; i < tweet.length - 2; i++) {
			String word1 = tweet[i];
			String word2 = tweet[i + 1];
			Double pmi = computePMI(word1, word2); 
			sum += (pmi != null) ? pmi : 0.0;
		}
		LOG.log(Level.INFO, "overall sentiment is " + sum);
		return sum;
	}
	
	public Map<Tweet, Boolean> processTweets(Set<Tweet> tweets) {
		Map<Tweet, Boolean> result = new HashMap<Tweet, Boolean>();
		for (Tweet tweet : tweets) {
			String[] strippedTweet = strip(tweet);
			if (strippedTweet == null || strippedTweet.length < 3) { continue; }
			if (computePMIForTweet(strippedTweet) > 0) {
				result.put(tweet, Boolean.TRUE);
			} else {
				result.put(tweet, Boolean.FALSE);
			}
		}
		return result;
	}
	
	public Double computePMI(String word1, String word2) {
		final String excel = "прекрасно";
		final String bad = "отвратительно";
		Integer excelfreq = getWordFreq(excel);
		Integer badfreq = getWordFreq(bad);
		
		Integer phexcelfreq = getWordFreq(word1 + " " + word2, excel);
		Integer phbadfreq = getWordFreq(word1 + " " + word2, bad);
		
		LOG.log(Level.INFO, "word1 word2 + excellent freq is " + phexcelfreq);
		LOG.log(Level.INFO, "word1 word2 + poor freq is " + phbadfreq);
		
		if (phexcelfreq < 4 && phbadfreq < 4) {
			return null;
		}
		
		if (phbadfreq == 0.0) {
			return 0.0;
		}
		
		return Math.log(Double.valueOf(phexcelfreq) * Double.valueOf(badfreq) /
						Double.valueOf(phbadfreq) * Double.valueOf(excelfreq));
	}
	
	public Integer getWordFreq(String word) {
		Integer dbCount = daoHelper.searchInDataStore(word);
		Integer result;
		if (dbCount == -1) {
			result = seHelper.getWordFreq(word);
			daoHelper.saveInDataStore(word, result);
		} else {
			result = dbCount;
		}
		return result;
	}
	
	public Integer getWordFreq(String word1, String word2) {
		Integer dbCount = daoHelper.searchInDataStore(word1, word2);
		Integer result;
		if (dbCount == -1) {
			result = seHelper.getWordFreq(word1, word2);
			daoHelper.saveInDataStore(word1, word2, result);
		} else {
			result = dbCount;
		}
		return result;		
	}
}
