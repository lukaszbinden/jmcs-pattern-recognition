package ch.unifr.jmcs.patrec.ex04;

public class Playground {

	public static void main(String[] args) throws Exception {
		
		MoleculeDataSet validSet = ValidationMain.load("data/validation/gxl");
		validSet.loadAll();
		
	}
	
}
