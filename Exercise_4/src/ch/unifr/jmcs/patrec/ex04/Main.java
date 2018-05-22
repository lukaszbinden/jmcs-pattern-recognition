package ch.unifr.jmcs.patrec.ex04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("main -->");
		long start = System.currentTimeMillis();
		
		Integer k = getK(args);
		
		DataSet trainSet = load("data/train.txt");
		DataSet validSet = load("data/valid.txt");
		
		int[] kValues;
		if (k == null) {
			kValues = new int[] {1,3,5,7,9,11};
		} else {
			kValues = new int[] {k};
		}
		
		double c_n = 3;
		double c_e = 2;
		
		IntStream.of(kValues).forEach(kV -> {
			double predictionAccuracy = ApproximateGEDExecutor.execute(kV, validSet, trainSet, c_n, c_e);
			System.out.println("Prediction accuracy for [k=" + kV + ", c_n=" + c_n + ", c_e=" + c_e + "]: " + predictionAccuracy + "%");
		});
		
		System.out.println("main <-- [" + ((double) (System.currentTimeMillis() - start) / 1000) + "ms]");
	}

	public static DataSet load(String fileName) throws IOException {
		DataSet dataSet = new DataSet();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.map(line -> line.split(" ")).forEach(line -> dataSet.add(line[0], line[1]));
		}
		return dataSet;
	}
	
	private static Integer getK(String[] args) {
		Integer k = null; // default
		if (args.length == 1) {
			try {
				k = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				throw e;
			}
			System.out.println("override default k with k=" + k);
		} else {
			//System.out.println("k argument not provided, use default k=1");
		}
		return k;
	}
}
