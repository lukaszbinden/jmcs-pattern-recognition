package ch.unifr.jmcs.patrec.ex04.algo;

import ch.unifr.jmcs.patrec.ex04.Molecule;

class BPMatcherImpl implements IBPMatcher {

	private IHungarian hungarian;
	
	public BPMatcherImpl(IHungarian hungarian) {
		this.hungarian = hungarian;
	}
	
	@Override
	public BPResult execute(Molecule g1, Molecule g2) {

		// 1. build the cost matrix (sl. 10-18) for g1 and some graph g2
		double[][] costMatrix = buildCostMatrix(g1, g2);
		
		// 2. find optimal assignment using Hungarian algorithm. get back the optimal assignment between g1 and g2
		int[][] optimalAssignment = hungarian.execute(costMatrix);
		
		// 3. complete the edit path and compute d(g1,g2)
		
		
		
		// 4. return d(g1,g2)
		
		return null;
	}

	private double[][] buildCostMatrix(Molecule g1, Molecule g2) {

		int n = 1;
		int m = 2;
		
		double[][] costMatrix = new double[n + m][n + m];
		
		
		return costMatrix;
	}

}
