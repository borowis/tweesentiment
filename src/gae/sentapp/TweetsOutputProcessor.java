package gae.sentapp;

import java.util.HashMap;
import java.util.Map;

import twitter4j.Tweet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TweetsOutputProcessor {
	public String process(Map<Tweet, Boolean> tweets) {
		Map<String, Boolean> foundTweets_str = new HashMap<String, Boolean>();
		for (Tweet tweet : tweets.keySet()) {
			String strTweet = "@" + tweet.getFromUser() + " - " + tweet.getText();
			foundTweets_str.put(strTweet, tweets.get(tweet));
		}
		Gson gson = new Gson();
		JsonElement jsonTree = gson.toJsonTree(foundTweets_str, Map.class);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("tweets", jsonTree);
		return jsonObject.toString();
	}
}
