package ch.unifr.jmcs.patrec.ex04.algo.impl;

import ch.unifr.jmcs.patrec.ex04.algo.ICostFunction;
import ch.unifr.jmcs.patrec.ex04.algo.Node;
import net.sourceforge.gxl.GXLEdge;

class DiracCost implements ICostFunction {

	// variables to optimize
	private double c_n;
	private double c_e;

	public DiracCost(double c_nInit, double c_eInit) {
		this.c_n = c_nInit;
		this.c_e = c_eInit;
	}
	
	public double getC_e() {
		return c_e;
	}
	
	public double getC_n() {
		return c_n;
	}
	
	@Override
	public double nodeInsertion(Node n1) {
		return c_n;
	}

	@Override
	public double nodeDeletion(Node n1) {
		return c_n;
	}

	@Override
	public double nodeSubstitution(Node n1, Node n2) {
		return n1.symbol().equalsIgnoreCase(n2.symbol()) ? 0 : 2 * c_n;
	}
	
	@Override
	public double edgeSubstitution(GXLEdge e1, GXLEdge e2) {
		// Da die Edges keine Label haben, sind die Edge label substitution cost immer null
		return 0;
	}
	
	@Override
	public double edgeInsertion() {
		return c_e;
	}

	@Override
	public double edgeDeletion() {
		return c_e;
	}

}
