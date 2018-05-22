package ch.unifr.jmcs.patrec.ex04;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import ch.unifr.jmcs.patrec.ex04.algo.AlgoFactory;
import ch.unifr.jmcs.patrec.ex04.algo.IBPMatcher;
import ch.unifr.jmcs.patrec.ex04.algo.IKnn;
import net.sourceforge.gxl.GXLDocument;

public class ApproximateGEDExecutor {

	public static double execute(int k, DataSet validSet, DataSet trainSet, double c_n, double c_e) {
		IBPMatcher bpMatcher = AlgoFactory.bpMatcher(c_n, c_e);
		IKnn knn = AlgoFactory.knn(k);
		List<String> predictions = new ArrayList<>();
		predictions.add("graphId,classIdPredicted,classIdActual");
		int predictedCorrectly = 0;
		
		for (Entry<String, String> entry : validSet) {
			String fileId = entry.getKey();
			String classId = entry.getValue();
			Molecule validG = create(fileId, classId);
			Map<Integer, List<Molecule>> distances = new TreeMap<>(); // for sorting according to key
			
			trainSet.stream()
				.map(e -> create(e))
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
			Path file = Paths.get("predictions-" + LocalTime.now().toString().replaceAll(":", "") + "-k" + k + "-.csv");
			Files.write(file, predictions, Charset.forName("UTF-8"));
//			file = Paths.get("predictions.csv");
//			Files.write(file, predictions, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return predictionAccuracy;
	}
	
	private static Molecule create(Entry<String, String> entry) {
		return create(entry.getKey(), entry.getValue());
	}
	
	private static Molecule create(String fileId, String classId) {
		try {
			GXLDocument gxl = new GXLDocument(Paths.get("data/gxl/" + fileId + ".gxl").toFile());
			return new Molecule(gxl, fileId, classId);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
}
