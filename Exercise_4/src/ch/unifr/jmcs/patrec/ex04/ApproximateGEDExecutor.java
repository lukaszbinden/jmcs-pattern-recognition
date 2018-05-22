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

import ch.unifr.jmcs.patrec.ex04.algo.AlgoFactory;
import ch.unifr.jmcs.patrec.ex04.algo.IBPMatcher;
import ch.unifr.jmcs.patrec.ex04.algo.IKnn;

public class ApproximateGEDExecutor {

	private MoleculeDataSet validSet;
	private MoleculeDataSet trainSet;

	public ApproximateGEDExecutor(MoleculeDataSet validSet, MoleculeDataSet trainSet) {
		this.validSet = validSet;
		this.trainSet = trainSet;
	}
	
	public double execute(int k, double c_n, double c_e) {
		IBPMatcher bpMatcher = AlgoFactory.bpMatcher(c_n, c_e);
		IKnn knn = AlgoFactory.knn(k);
		List<String> predictions = new ArrayList<>();
		predictions.add("graphId,classIdPredicted,classIdActual");
		int predictedCorrectly = 0;
		
		for (Entry<String, String> entry : validSet) {
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
			if (classIdPredicted.equalsIgnoreCase(classId)) {
				predictedCorrectly++;
			}
			predictions.add(fileId + "," + classIdPredicted + "," + classId);
		}
		
		double predictionAccuracy = ((double) predictedCorrectly / validSet.size()) * 100;
		
		try {	
			Path file = Paths.get("predictions-k" + k + "-c_n" + c_n + "-c_e" + c_e + ".csv");
			Files.write(file, predictions, Charset.forName("UTF-8"));
//			file = Paths.get("predictions.csv");
//			Files.write(file, predictions, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return predictionAccuracy;
	}
	
}
