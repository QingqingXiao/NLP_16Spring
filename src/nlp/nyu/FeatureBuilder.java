package nlp.nyu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FeatureBuilder {
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
		    System.err.println ("FeatureBuilder requires 3 arguments:  dataFile outputPath training");
		    System.exit(1);
		}
		String dataFile = args[0];
		String outputPath = args[1];
		boolean training = (args[2].equals("training"))? true: false;
		BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
		List<String> lines = new ArrayList<String>();
		String line = "";
		while((line = dataReader.readLine()) != null) {
			lines.add(line);
		}
		dataReader.close();
		PrintWriter writer = new PrintWriter(new FileWriter(outputPath));
		String pre = "#";
		String preWord = "";
		String next = "";
		String nextWord = "";
		for (int i=0; i<lines.size()-1; i++) {
			if (lines.get(i).isEmpty()) {
				pre = "#";
				writer.write("\n");
				continue;
			}
			String[] lineArr = lines.get(i).split("\t");
			String nextLine = lines.get(i+1);
			next = nextLine.isEmpty() ? "" : nextLine.split("\t")[1];
			nextWord = nextLine.isEmpty() ? "" : nextLine.split("\t")[0];
			writer.write(lineArr[0] + "\tpos=" + lineArr[1]);
			if (!pre.equals("#")) {
				writer.write("\tprevious=" + pre + "\tpreviousWord=" + preWord);
			}
			if (!next.equals("")) {
				writer.write("\tnext=" + next + "\tnextWord=" + nextWord);
			}
			addAdvancedFeature(lineArr[0], writer);
			if (training) {
				writer.write("\t" + lineArr[2]);
			}
			writer.write("\n");
			pre = lineArr[1];
			preWord = lineArr[0];
		}
		writer.write("\n");
		writer.flush();
		writer.close();
	}
	
	private static void addAdvancedFeature(String word, PrintWriter writer) {
		String[] end_specials = {"ly", "ing", "able"};
		for (String special : end_specials) {
			if (word.endsWith(special)) {
				writer.write("\tEND_WITH_" + special.toUpperCase());
			}
		}
//		if (Character.isUpperCase(word.charAt(0))) {
//			writer.write("\tCAPITALIZED");
//		}
	}

	static String getField(String line, int pos) {
		String[] words = line.split(" ");
		if(pos >= words.length - 1) {
			return "#";
		}
		return words[pos];
	}
}
