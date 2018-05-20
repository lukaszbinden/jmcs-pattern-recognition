package ch.unifr.jmcs.patrec.ex04.algo.impl;

import ch.unifr.jmcs.patrec.ex04.algo.ICostFunction;
import ch.unifr.jmcs.patrec.ex04.algo.Node;

public class DiracCostFunctionImpl implements ICostFunction {

	// variables to optimize
	private double c_n;
	private double c_e;
	
	public DiracCostFunctionImpl(double c_nInit, double c_eInit) {
		this.c_n = c_nInit;
		this.c_e = c_eInit;
	}

	@Override
	public double nodeInsertion(Node n1) {
		return c_n + n1.numAdjacentEdges() * edgeInsertion();
	}

	@Override
	public double nodeDeletion(Node n1) {
		return c_n + n1.numAdjacentEdges() * edgeDeletion();
	}

	@Override
	public double edgeInsertion() {
		return c_e;
	}

	@Override
	public double edgeDeletion() {
		return c_e;
	}

	@Override
	public double nodeSubstitution(Node n1, Node n2) {
		double dirac = (n1.symbol().equalsIgnoreCase(n2.symbol())) ? 0 : 2 * c_n;
		return dirac + edgeAssignmentCost(n1, n2);
	}

	private double edgeAssignmentCost(Node n1, Node n2) {
		// TODO at work...
		
		
		return 0;
	}


}
