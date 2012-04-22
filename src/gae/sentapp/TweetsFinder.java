package gae.sentapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TweetsFinder {
	private static int MAX_PAGES = 2;
	private static int RPP = 100;
	private Twitter twitter = new TwitterFactory().getInstance(); 
	public Set<Tweet> twFind(String term) {
		Set<Tweet> foundTweets = new HashSet<Tweet>();
        try {
        	Query query = new Query(term);
        	query.setRpp(RPP);
        	for (int i = 1; i <= MAX_PAGES; i++) {
        		query.setPage(i); // loads result page by page
        		QueryResult result = twitter.search(query); 
        		List<Tweet> tweets = result.getTweets();
        		for (Tweet tweet : tweets) {
    				foundTweets.add(tweet);
        		}
        	}
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        return foundTweets;
	}
}
