package gae.sentapp.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SEHelper {
	private static final Logger LOG = Logger.getLogger("gae.sentapp.sentiment");
	
	public Integer getWordFreq(String word) {
		String countString = null;
		try {
			countString = seBingProviderCount(word);
		} catch(MalformedURLException mex) {
			throw new RuntimeException("malformed url exception", mex);
		} catch(UnsupportedEncodingException uee) {
			
		}
		if (countString != null) {
			return parseCountString(countString);
		}
		return 0;
	}
	
	public Integer getWordFreq(String word1, String word2) {
		String countString = null;
		try {
			countString = seBingProviderCount(word1, word2);
		} catch(MalformedURLException mex) {
			throw new RuntimeException("malformed url exception", mex);
		} catch(UnsupportedEncodingException uee) {
			
		}
		if (countString != null) {
			return parseCountString(countString);
		}
		return 0;
	}
	
	
	public String seBingProviderCount(String word) throws MalformedURLException, UnsupportedEncodingException {
		String urlStr = word;
		urlStr = "http://www.bing.com/search?q=" + URLEncoder.encode(urlStr, "UTF-8");
		URL url = new URL(urlStr);
		return urlConnector(url);
	}
	
	public String seBingProviderCount(String word1, String word2) throws MalformedURLException, UnsupportedEncodingException {
		String urlStr = word1 + "+NEAR%3A10+" + word2;
		urlStr = "http://www.bing.com/search?q=" + URLEncoder.encode(urlStr, "UTF-8");
		URL url = new URL(urlStr);
		return urlConnector(url);
	}
	
	private Integer parseCountString(String count) {
		final String SEPARATOR_TODAY = ",";
		// parse string to get search count
		Pattern regex = Pattern.compile("\\d+-\\d+ of ((?:\\d{1,3},?)+)");
		Matcher m = regex.matcher(count);
		if (m.find()) {
			String resultCount = m.group(1);
			return Integer.valueOf(resultCount.replaceAll(SEPARATOR_TODAY, ""));
		}	
		return 0;
	}
	
	private String urlConnector(URL url) throws MalformedURLException {
		final String MAGIC_TEXT = "class=sb_count";
		String theString = null;
		BufferedReader in = null;
		try {
	        URLConnection conn =  url.openConnection();
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
	        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String str;
	
	        while ((str = in.readLine()) != null) {
	        	String replaced = str.replaceAll("[^ _=a-zA-Z]", "");
	        	if (replaced.contains(MAGIC_TEXT)) {
	        		theString = str;
	        		break;
	        	}
	        }
	        
	        in.close();
		} catch(IOException ioe) {
			throw new RuntimeException("can't read url properly!", ioe);
		}
		return theString;
	}
}
