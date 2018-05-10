package ch.unifr.jmcs.patrec.ex04.algo;

import ch.unifr.jmcs.patrec.ex04.Molecule;

/**
 * Bipartite graph matcher.
 * 
 */
public interface IBPMatcher {

	BPResult execute(Molecule g1, Molecule g2);
	
	class BPResult {
		
		private int distance;
		private Molecule g1;
		private Molecule g2;
		
		public int distance() {
			return distance;
		}
		
		public Molecule g1() {
			return g1;
		}
		
		public Molecule g2() {
			return g2;
		}
	}
}
