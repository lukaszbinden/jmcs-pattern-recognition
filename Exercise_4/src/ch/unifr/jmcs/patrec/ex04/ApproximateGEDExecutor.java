package ch.unifr.jmcs.patrec.ex04;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import ch.unifr.jmcs.patrec.ex04.algo.AlgoFactory;
import ch.unifr.jmcs.patrec.ex04.algo.IBPMatcher;
import ch.unifr.jmcs.patrec.ex04.algo.IKnn;

public class ApproximateGEDExecutor {

	private final MoleculeDataSet validSet;
	private final MoleculeDataSet trainSet;
	private final boolean isTraining;

	public ApproximateGEDExecutor(MoleculeDataSet validSet, MoleculeDataSet trainSet, boolean isTraining) {
		this.validSet = validSet;
		this.trainSet = trainSet;
		this.isTraining = isTraining;
	}
	
	public Double execute(int k, double c_n, double c_e) {
		IBPMatcher bpMatcher = AlgoFactory.bpMatcher(c_n, c_e);
		IKnn knn = AlgoFactory.knn(k);
		List<String> predictions = new CopyOnWriteArrayList<>();
		if (isTraining) {
			predictions.add("graphId,classIdPredicted,classIdActual");
		}
		AtomicInteger predictedCorrectly = new AtomicInteger(0);
		
		Consumer<Entry<String, String>> process = (entry) -> {
			String fileId = entry.getKey();
			String classId = entry.getValue();
			Molecule validG = validSet.get(fileId);
			Map<Integer, List<Molecule>> distances = new TreeMap<>(); // for sorting according to key
			
			trainSet.stream()
				.map(e -> trainSet.get(e.getKey()))
				.map(trainG -> bpMatcher.execute(validG, trainG))
				.forEach(result -> {  
					//System.out.println(fileId + " <-> " + result.g2().getFileid() + " => " + result.distance());
					distances.compute(result.distance(), (key, list) -> {
						if (list == null) {
							list = new ArrayList<>(); 
						}
						list.add(result.g2());
						return list;
					});
				});
				
			String classIdPredicted = knn.classify(validG, distances);
			if (isTraining) {
				if (classIdPredicted.equalsIgnoreCase(classId)) {
					predictedCorrectly.incrementAndGet();
				}
				predictions.add(fileId + "," + classIdPredicted + "," + classId);
			} else {
				predictions.add(fileId + "," + classIdPredicted);
			}
		};
		
		Stream<Entry<String, String>> stream = validSet.stream();
		if (!isTraining) {
			stream = stream.parallel();
		}
		stream.forEach(process);
		
		Double predictionAccuracy = null;
		if (isTraining) {
			predictionAccuracy = ((double) predictedCorrectly.intValue() / validSet.size()) * 100;
		}
		
		try {	
			Path file = Paths.get("predictions-k" + k + "-c_n" + c_n + "-c_e" + c_e + ".csv");
			Files.write(file, predictions, Charset.forName("UTF-8"));
			if (!isTraining) {
				file = Paths.get("molecules_predictions.txt");
				Files.write(file, predictions, Charset.forName("UTF-8"));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return predictionAccuracy;
	}
	
}
