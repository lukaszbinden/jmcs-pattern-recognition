package ch.unifr.jmcs.patrec.ex04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import net.sourceforge.gxl.GXLDocument;

public class Main {

	public static void main(String[] args) throws Exception {
		
		DataSet trainIds = load("data/train.txt");
		DataSet validIds = load("data/valid.txt");
		
		for (String validId : validIds) {
			GXLDocument validG = new GXLDocument(Paths.get("gxl/" + validId + ".gxl").toFile());
			for (String trainId : trainIds) {
				
				
				
			}
			
			
		}
		
	}

	private static DataSet load(String fileName) throws IOException {
		DataSet dataSet = new DataSet();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.map(line -> line.split(" ")).forEach(line -> dataSet.add(line[0], line[1]));
		}
		return dataSet;
	}
	
}
