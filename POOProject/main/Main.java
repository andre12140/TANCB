package main;

import graph.Graph;
import graph.Node;
import graph.NodeInfo;
import classifier.TAN;
import metrics.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Main class
 */
public class Main {

    public static void main(String[] args) {

        if(args.length!=3){
            throw new IllegalArgumentException("You must specify the training and testing data filenames, and the type of score");
        }
        StringBuffer firstLine = new StringBuffer();
        ArrayList<Integer>[] trainingData = readFileData(args[0], firstLine);

        ArrayList<Integer>[] testData = readFileData(args[1],null);
        String typeScore = args[2]; //LL or MDL

        String aux = firstLine.substring(1,firstLine.length()-1);
        List<String> featureNames = Arrays.asList(aux.split(", "));

        ArrayList<Integer>[] trainingFeatures = Arrays.copyOf(trainingData, trainingData.length-1);
        ArrayList<Integer> trainingY = trainingData[trainingData.length-1];

        ArrayList<Integer>[] testFeatures = Arrays.copyOf(testData, testData.length-1);
        ArrayList<Integer> testY = testData[testData.length-1];

        TAN tan = new TAN(typeScore);

        long startTimeToFit = System.currentTimeMillis();
        tan.fit(trainingFeatures, trainingY);
        long endTimeToFit = System.currentTimeMillis();
        long timeDurationFit = endTimeToFit-startTimeToFit;

        tan.setNodeNames(featureNames);

        Graph g = tan.getTree();

        long startTimeToPred = System.currentTimeMillis();
        ArrayList<Integer> y_pred = tan.predict(testFeatures);
        long endTimeToPred = System.currentTimeMillis();
        long timeDurationPred = endTimeToPred-startTimeToPred;

        int[] y_predArr = y_pred.stream().mapToInt(Integer::intValue).toArray();
        int[] testYArr = testY.stream().mapToInt(Integer::intValue).toArray();


        System.out.print("Classifier:" + '\t');             //First Print
        for(Node<NodeInfo> n : g.getSortedVertices()){
            if(n.getIndex()==0){
                System.out.println("\t" + n.getData());
                continue;
            }
            System.out.println("\t"+"\t"+"\t"+ "\t" +n.getData());
        }

        System.out.println("Time to build: " + "\t" + timeDurationFit + " ms time");
        System.out.println("Testing the classifier: ");
        int count = 1;
        for(int i : y_predArr){
            System.out.println("-> instance "+count+":" + "\t" + i);
            count++;
        }

        System.out.println("Time to test: " + "\t" + timeDurationPred + " ms time");

        Accuracy acc = new Accuracy(testYArr,y_predArr);
        Specificity sp = new Specificity(testYArr,y_predArr);
        Sensitivity se = new Sensitivity(testYArr,y_predArr);
        F1Score f1 = new F1Score(testYArr,y_predArr);

        System.out.println("Resume:"+"\n"+ "\tAccuracy:" + acc.getScore());

        StringBuilder sb = new StringBuilder();
        sb.append("\tValues for: [");

        for(int i=0; i<tan.getcNode().getData().getR();i++){
            sb.append("Class ").append(i).append(", ");
        }
        sb.append("Weighted average]");
        System.out.println(sb);

        System.out.println("\tSpecificity:" + sp.getScore());
        System.out.println("\tSensitivity:" + se.getScore());
        System.out.println("\tF1Score:" + f1.getScore());

        ConfusionMatrix cm = ConfusionMatrix.of(testYArr,y_predArr);

        System.out.println("\tConfusion Matrix:");
        System.out.println(cm);
    }

    /**
     * Reads data from .CSV files
     * @param fileName Path of the file to read
     * @param sb empty StringBuffer
     * @return Data read
     */
    public static ArrayList<Integer>[] readFileData(String fileName, StringBuffer sb){
        int n = 0, N=0;

        String line;
        String cvsSplitBy = ",";
        BufferedReader br = null;
        String[] firstLine;
        ArrayList<Integer>[] data = new ArrayList[0];

        try {
            br = new BufferedReader(new FileReader(fileName));
            firstLine = br.readLine().split(cvsSplitBy);
            n = firstLine.length - 1;
            data = new ArrayList[n+1];
            if(sb != null){
                sb.append(Arrays.toString(firstLine));
            }
            while ((line = br.readLine()) != null) {
                // use comma as separator
                int[] values = Arrays.stream(line.split(cvsSplitBy)).mapToInt(Integer::parseInt).toArray();

                // Gravar os dados
                int aux=0;
                for(int value : values){
                    if(data[aux]==null){
                        data[aux] = new ArrayList<Integer>();
                    }
                    data[aux].add(value);
                    aux++;
                }
                N++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

}
