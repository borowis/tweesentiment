package gae.sentapp;

import gae.sentapp.sentiment.TweetSentimentAnalyzer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Tweet;

@SuppressWarnings("serial")
public class TweetsAnalyzerServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger("gae.sentapp.sentiment");
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		TweetsFinder twFinder = new TweetsFinder();
		TweetSentimentAnalyzer twAnalyzer = new TweetSentimentAnalyzer();
		TweetsOutputProcessor twOutputProcessor = new TweetsOutputProcessor();
		String term = (String)req.getParameter("term");
		Set<Tweet> tweets = twFinder.twFind(term);
		Map<Tweet, Boolean> processedTweets = twAnalyzer.processTweets(tweets);
		String result = twOutputProcessor.process(processedTweets);
		
		resp.setContentType("application/json; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(result);
	}	
}
