package nlp.nyu;

//Wrapper for maximum-entropy tagging

//NYU - Natural Language Processing - Prof. Grishman

//invoke by:  java  MEtag dataFile  model  responseFile

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import opennlp.maxent.GISModel;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;

//reads line with tab separated features
//writes feature[0] (token) and predicted tag

public class MEtag {

 public static void main (String[] args) {
	if (args.length != 3) {
	    System.err.println ("MEtag requires 3 arguments:  dataFile model responseFile");
	    System.exit(1);
	}
	String dataFileName = args[0];
	String modelFileName = args[1];
	String responseFileName = args[2];
	try {
	    GISModel m = (GISModel) new SuffixSensitiveGISModelReader(new File(modelFileName)).getModel();
	    BufferedReader dataReader = new BufferedReader (new FileReader (dataFileName));
	    PrintWriter responseWriter = new PrintWriter (new FileWriter (responseFileName));
	    String line;
	    while ((line = dataReader.readLine()) != null) {
			if (line.equals("")) {
			    responseWriter.println();
			} else {
			    String[] features = line.split("\t");
			    String tag = m.getBestOutcome(m.eval(features));
			    responseWriter.println(features[0] + "\t" + tag);
			}
	    }
	    dataReader.close();
	    responseWriter.close();
	} catch (Exception e) {
	    System.out.print("Error in data tagging: ");
	    e.printStackTrace();
	}
 }

}
