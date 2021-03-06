package tmldaperplexity;

import java.io.*;
import java.util.*;
import java.math.*;

import perplexitymonths.BigDecimalUtils;
/*
 *  /media/toshibasecond/months/perplexity/predictedwordprobability
 *  /media/toshibasecond/months/perplexity/tmldaperplexity/predictedperplexitybigdecimal/
 *  /media/toshibasecond/months/perplexity/tmldaperplexity/predictedwordprobability
 */
public class MeasurePerplexityBigInteger {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		FileInputStream fstream = new FileInputStream
				(args[2]);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader
				(new InputStreamReader(in));
		String line = null;
		BigDecimal numBoundaries = BigDecimal.ZERO;
		BigDecimal avgPerplexity = BigDecimal.ZERO;
		Map<String,BigDecimal>wordProbabilities = new LinkedHashMap<String,BigDecimal>();
		while((line = br.readLine())!=null){
			String [] array = line.split("##,##");
			wordProbabilities.put(array[0], new BigDecimal(array[1]));
		}
		File dir = new File(args[0]); //path up to InvertedIndex
		String[] chld = dir.list();
		Arrays.sort(chld);



		for(int chldIndex = 0 ; chldIndex < chld.length ; chldIndex++){
			if(new File(args[0]+"/"+chld[chldIndex]).isDirectory()){
				File dirRegion = new File(args[0]+"/"+chld[chldIndex]);
				String[] chldRegion = dirRegion.list();
				Arrays.sort(chldRegion);
				new File(args[1]+"/"+chld[chldIndex]).mkdirs();
				for(int chldBoundaryIndex = 0 ; chldBoundaryIndex
						< chldRegion.length ; chldBoundaryIndex++){
					numBoundaries = numBoundaries.add(BigDecimal.ONE);
					FileInputStream fstreamInvIndexBD = new FileInputStream
							(args[0]+"/"+chld[chldIndex]+"/"+
									chldRegion[chldBoundaryIndex]);
					DataInputStream inInvIndexBD = new DataInputStream(fstreamInvIndexBD);
					BufferedReader brInvIndexBD = new BufferedReader
							(new InputStreamReader(inInvIndexBD));
					String lineInvIndexBD = null;
					BigDecimal sumofLogs = new BigDecimal("0");
					while((lineInvIndexBD = brInvIndexBD.readLine())!=null){
						int indexofColon = lineInvIndexBD.lastIndexOf(":");
						BigDecimal predictedProbabilityofWord = wordProbabilities.
								get(lineInvIndexBD.substring(0, indexofColon));
						if(predictedProbabilityofWord.compareTo(BigDecimal.ZERO)>0){
							BigDecimal logofProb = BigDecimalUtils
									.ln(predictedProbabilityofWord, 5);
							logofProb = logofProb.
									divide(BigDecimalUtils.ln(new BigDecimal("2"), 5),
											RoundingMode.HALF_UP);
							sumofLogs = sumofLogs.add(logofProb);
						}
					}
					sumofLogs = sumofLogs.multiply(new BigDecimal("-1"));
					BigDecimal perplexity = sumofLogs.pow(2);				
					String fileToBeMade = "";
					if(chldRegion[chldBoundaryIndex].length() == 65)
						fileToBeMade = chldRegion[chldBoundaryIndex].substring(0, 6);
					else
						fileToBeMade = chldRegion[chldBoundaryIndex].substring(0,5);
//					PrintWriter out  = new PrintWriter(new BufferedWriter
//							(new FileWriter(args[1]+"/"+chld[chldIndex]+"/"+
//									chldRegion[chldBoundaryIndex]+
//									"",true)));
					PrintWriter out  = new PrintWriter(new BufferedWriter
							(new FileWriter(args[1]+"/"+chld[chldIndex]+"/"+
									fileToBeMade+
									"",true)));
					out.write(perplexity+"\n");
					avgPerplexity = avgPerplexity.add(perplexity);
					out.close();
				}	
			}
		}
		avgPerplexity = avgPerplexity.divide(numBoundaries,RoundingMode.HALF_UP);
		System.out.println("number of Boundaries: "+numBoundaries);
		System.out.println("Average Perplexity: "+avgPerplexity);
	}

}
