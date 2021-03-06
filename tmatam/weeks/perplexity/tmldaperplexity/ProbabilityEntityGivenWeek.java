package tmldaperplexity;
import java.io.*;
import java.util.*;
public class ProbabilityEntityGivenWeek {
	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Map<String,Map<String,Double>>tokenStatsMap = new
				HashMap<String,Map<String,Double>>();
		FileInputStream fstreamProbEnGivWordBD = new FileInputStream
				(args[0]);
		DataInputStream inProbEnGivWordBD = new DataInputStream
				(fstreamProbEnGivWordBD);
		BufferedReader brProbEnGivWordBD = new BufferedReader
				(new InputStreamReader(inProbEnGivWordBD));
		String lineProbEnGivWordBD = null;
		Map<String,Double>weeklyEntityStats =
				new LinkedHashMap<String,Double>();
		boolean firstline = true;
		double numberofTweets = countLines
				(args[0]);
		while((lineProbEnGivWordBD = brProbEnGivWordBD.readLine())!=null){
			int indexofDelimiter = lineProbEnGivWordBD.indexOf("##,##");
			String tweetId = lineProbEnGivWordBD.substring(0, indexofDelimiter);
			String stats = lineProbEnGivWordBD.substring(indexofDelimiter+5);
			String [] statsArray = stats.split(",");

			for(int i = 0 ; i < statsArray.length ; i++){
				String entityCount = statsArray[i];
				int indexofColon = entityCount.indexOf(":");
				String entity = entityCount.substring(0,indexofColon);
				double count = Double.parseDouble
						(entityCount.substring(indexofColon+1));
				if(firstline){
					weeklyEntityStats.put(entity, count/numberofTweets);
				}
				else{
					weeklyEntityStats.put(entity,
							weeklyEntityStats.get(entity)+count/numberofTweets);
				}
			}
			if(firstline)
			{
				firstline = false;
			}
		}

		PrintWriter out  = new PrintWriter(new BufferedWriter
				(new FileWriter(args[1]+
						"",true)));
		Iterator it = weeklyEntityStats.entrySet().iterator();
		int index = 0;
		while(it.hasNext()){
			Map.Entry pairs = (Map.Entry)it.next();
			String entity = (String) pairs.getKey();
			double value = (double)pairs.getValue();
			if(index < weeklyEntityStats.size()-1)
			out.write(entity+":"+value+",");
			else
				out.write(entity+":"+value);
			index++;
		}
		out.close();
	}

}
