package seasonDetection;

import java.io.*;
import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/*
 * /media/toshibasecond/healthmonitoringovertime/healthMajorAreas
 *  /media/toshibasecond/healthmonitoringovertime/weeks/
 */
public class AggregateRegionWeek {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//region,date,tweetId,tweetText
		Map<String,Map<DateTime,List<Tweet>>> regionalDateWiseTweets =
				new HashMap<String,Map<DateTime,List<Tweet>>>();
		try{
			FileInputStream fileInputStream = new FileInputStream
					(args[0]);
			DataInputStream in = new DataInputStream(fileInputStream);
			BufferedReader br = new BufferedReader
					(new InputStreamReader(in,"UTF-8"));
			String strLine;


			while((strLine = br.readLine())!=null){
				String [] array = strLine.split("\t");
				String tweetId = array[0];

				String dateTemp = array[2];
				String date = array[2].substring(0,dateTemp.indexOf(" "));
				//				DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ssZ");
				DateTimeFormatter formatter = DateTimeFormat.forPattern
						("yyyy-MM-dd");
				DateTime dt = formatter.parseDateTime(date);
				String text = array[3];
				int indexofDelim = array[3].indexOf("##,##");
				String tweetText = text.substring(0, indexofDelim);
				String region = text.substring(indexofDelim+5);


				Map<DateTime,List<Tweet>> dateTweets;

				if(regionalDateWiseTweets.containsKey(region)){
					dateTweets = regionalDateWiseTweets.get(region);
					List<Tweet>listofTweets;
					if(dateTweets.containsKey(dt)){
						listofTweets = dateTweets.get(dt);
					}
					else{
						listofTweets = new ArrayList<>();
					}
					Tweet tweet = new Tweet();
					tweet.text = tweetText;
					tweet.tweetId = Long.parseLong(tweetId);
					listofTweets.add(tweet);
					dateTweets.put(dt, listofTweets);	
				}
				else{
					dateTweets = new LinkedHashMap<DateTime,List<Tweet>>();
					Tweet tweet = new Tweet();
					tweet.text = tweetText;
					tweet.tweetId = Long.parseLong(tweetId);
					List<Tweet>listofTweets = new ArrayList<>();
					listofTweets.add(tweet);
					dateTweets.put(dt, listofTweets);
				}
				regionalDateWiseTweets.put(region, dateTweets);
			}
			Iterator it = regionalDateWiseTweets.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pairs = (Map.Entry) it.next();
				String region = (String) pairs.getKey();
				Map<String,List<Tweet>> dtTweet = (Map<String, List<Tweet>>)
						pairs.getValue();
				Iterator itDt = dtTweet.entrySet().iterator();

				File theDir = new File(args[1]+region);
				if(!(theDir.exists()))
					theDir.mkdirs();

				while(itDt.hasNext()){
					Map.Entry dtPairs = (Map.Entry)itDt.next();	
					DateTime dt = (DateTime) dtPairs.getKey();
					List<Tweet> tweetList = (List<Tweet>) dtPairs.getValue();
					//					LocalDate dtTemp = dt.toLocalDate();
					PrintWriter out  = new PrintWriter(new BufferedWriter
							(new FileWriter(args[1]+region+"/"+
									dt.getYearOfCentury()+"_"+
									dt.getWeekOfWeekyear(),true)));
					for(Tweet tweet : tweetList){
						out.println(tweet.tweetId+"##,##"+tweet.text);
					}
					out.close();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
