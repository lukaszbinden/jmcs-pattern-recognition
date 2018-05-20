package ch.unifr.jmcs.patrec.ex04.algo;

import ch.unifr.jmcs.patrec.ex04.Molecule;

class BPMatcherImpl implements IBPMatcher {

	private IHungarian hungarian;
	private ICostFunction diracCost;
	
	public BPMatcherImpl(IHungarian hungarian, ICostFunction diracCost) {
		this.hungarian = hungarian;
		this.diracCost = diracCost;
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

		int n = g1.getNumNodes();
		int m = g2.getNumNodes();
		
		double[][] costMatrix = new double[n + m][n + m];
		
		// substitution costs
		for (int i=0; i<n; i++) {
			Node g1n = g1.getNodes()[i];
			for (int j=0; j<m; j++) {
				Node g2n = g2.getNodes()[j];
				double c_ij = diracCost.nodeSubstitution(g1n, g2n);
				costMatrix[i][j] = c_ij;
			}
		}
		
		// deletion costs
		for (int i=0; i<n; i++) {
			Node g1n = g1.getNodes()[i];
			for (int j=0; j<n; j++) {
				double c_iepsilon = (i == j) ? diracCost.nodeDeletion(g1n) : Double.MAX_VALUE;
				costMatrix[i][j] = c_iepsilon;
			}
		}
		
		// insertion costs
		for (int i=0; i<m; i++) {
			Node g2n = g2.getNodes()[i];
			for (int j=0; j<m; j++) {
				double c_epsilonj = (i == j) ? diracCost.nodeInsertion(g2n) : Double.MAX_VALUE;
				costMatrix[i][j] = c_epsilonj;
			}
		}
		
		// dummy assignments
		// not necessary -> default double value is 0
		
		return costMatrix;
	}

}
