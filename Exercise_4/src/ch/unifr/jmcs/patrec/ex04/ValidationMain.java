package ch.unifr.jmcs.patrec.ex04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ValidationMain {

	public static void main(String[] args) throws Exception {
		System.out.println("main -->");
		System.out.println(Runtime.getRuntime().availableProcessors() + " processors available.");
		
		long start = System.currentTimeMillis();
		
		MoleculeDataSet trainSet = TrainingMain.load("data/train.txt");
		trainSet.loadAll();
		MoleculeDataSet testSet = load("data/validation/gxl");
		testSet.loadAll();
		ApproximateGEDExecutor executor = new ApproximateGEDExecutor(testSet, trainSet, false);
		
		// hyperparameters used for validation:
		// k = 7,	c_n = 3,	c_e = 2
		executor.execute(7, 3, 2);
		
		System.out.println("main <-- [" + ((double) (System.currentTimeMillis() - start) / 1000) + "ms]");
	}

	public static MoleculeDataSet load(String fileName) throws IOException {
		MoleculeDataSet dataSet = new MoleculeDataSet();
		
		Files.list(Paths.get(fileName)).forEach(file -> {
			dataSet.add(file.getFileName().toString(), MoleculeDataSet.DUMMY_CLASS_ID);
		});
		
		return dataSet;
	}
}
