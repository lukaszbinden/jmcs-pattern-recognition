package ch.unifr.jmcs.patrec.ex04;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("main -->");
		long start = System.currentTimeMillis();
		
		Integer k = getK(args);
		
		MoleculeDataSet trainSet = load("data/train.txt");
		trainSet.loadAll();
		MoleculeDataSet validSet = load("data/valid.txt");
		validSet.loadAll();
		ApproximateGEDExecutor executor = new ApproximateGEDExecutor(validSet, trainSet);
		
		int[] kValues;
		if (k == null) {
			kValues = new int[] {1,3,5,7,9,11,63};
		} else {
			kValues = new int[] {k};
		}
		
		double[] c_nValues = {3, 4, 5, 0.5, 58};
		double[] c_eValues = {2, 3, 4, 0.1, 87};
		List<String> stats = new CopyOnWriteArrayList<>();
		stats.add("k,c_n,c_e,acc");
		
		IntStream.of(kValues).parallel().forEach(kV -> 
			DoubleStream.of(c_nValues).forEach(c_n ->
				DoubleStream.of(c_eValues).forEach(c_e -> {
					double predictionAccuracy = executor.execute(kV, c_n, c_e);
					System.out.println("Prediction accuracy for [k=" + kV + ", c_n=" + c_n + ", c_e=" + c_e + "]: " + predictionAccuracy + "%");
					stats.add(kV + "," + c_n + "," + c_e + "," + predictionAccuracy);
				})
			)
		);
		
		Path file = Paths.get("stats-" + LocalTime.now().toString().replaceAll(":", "") + ".csv");
		Files.write(file, stats, Charset.forName("UTF-8"));
		
		System.out.println("main <-- [" + ((double) (System.currentTimeMillis() - start) / 1000) + "ms]");
	}

	public static MoleculeDataSet load(String fileName) throws IOException {
		MoleculeDataSet dataSet = new MoleculeDataSet();
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
