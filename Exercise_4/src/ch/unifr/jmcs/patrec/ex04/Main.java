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
import java.util.stream.Stream;

import ch.unifr.jmcs.patrec.ex04.algo.AlgoFactory;
import ch.unifr.jmcs.patrec.ex04.algo.IBPMatcher;
import ch.unifr.jmcs.patrec.ex04.algo.IKnn;
import net.sourceforge.gxl.GXLDocument;

public class Main {

	public static void main(String[] args) throws Exception {
		System.out.println("main -->");
		
		int k = getK(args);
		
		DataSet trainSet = load("data/train.txt");
		DataSet validSet = load("data/valid.txt");
		
		IBPMatcher bpMatcher = AlgoFactory.bpMatcher();
		IKnn knn = AlgoFactory.knn(k);
		List<String> predictions = new ArrayList<>();
		
		for (Entry<String, String> entry : validSet) {
			String fileId = entry.getKey();
			String classId = entry.getValue();
			Molecule validG = create(fileId, classId);
			Map<Integer, List<Molecule>> distances = new TreeMap<>(); // for sorting according to key
			
			trainSet.stream()
				.map(e -> create(e))
				.map(trainG -> bpMatcher.execute(validG, trainG))
				.forEach(result ->   
					distances.compute(result.distance(), (key, list) -> {
						if (list == null) {
							list = new ArrayList<>(); 
						}
						list.add(result.g2());
						return list;
					})
				);
				
			String classIdPredicted = knn.classify(validG, distances);
			
			predictions.add(fileId + "," + classIdPredicted + "," + classId);
		}
		
		Path file = Paths.get("predictions-" + LocalTime.now().toString().replaceAll(":", "") + ".csv");
		Files.write(file, predictions, Charset.forName("UTF-8"));
		
		System.out.println("main <--");
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

	public static DataSet load(String fileName) throws IOException {
		DataSet dataSet = new DataSet();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.map(line -> line.split(" ")).forEach(line -> dataSet.add(line[0], line[1]));
		}
		return dataSet;
	}
	
	private static int getK(String[] args) {
		int k = 1; // default
		if (args.length == 1) {
			try {
				k = Integer.parseInt(args[0]);
			} catch (Exception e) {
				// stick to default
			}
		}
		return k;
	}
}
