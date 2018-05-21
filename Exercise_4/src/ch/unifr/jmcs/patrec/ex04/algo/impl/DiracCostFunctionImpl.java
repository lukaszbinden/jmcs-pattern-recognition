package ch.unifr.jmcs.patrec.ex04.algo.impl;

import ch.unifr.jmcs.patrec.ex04.algo.ICostFunction;
import ch.unifr.jmcs.patrec.ex04.algo.Node;
import net.sourceforge.gxl.GXLEdge;

public class DiracCostFunctionImpl implements ICostFunction {

	private DiracCost diracCost;
	
	public DiracCostFunctionImpl(double c_nInit, double c_eInit) {
		this.diracCost = new DiracCost(c_nInit, c_eInit);
	}

	@Override
	public double nodeInsertion(Node n1) {
		return diracCost.nodeInsertion(n1) + n1.numAdjacentEdges() * edgeInsertion();
	}

	@Override
	public double nodeDeletion(Node n1) {
		return diracCost.nodeDeletion(n1) + n1.numAdjacentEdges() * edgeDeletion();
	}

	@Override
	public double edgeInsertion() {
		return diracCost.edgeInsertion();
	}

	@Override
	public double edgeDeletion() {
		return diracCost.edgeDeletion();
	}

	@Override
	public double edgeSubstitution(GXLEdge e1, GXLEdge e2) {
		return diracCost.edgeSubstitution(e1, e2);
	}
	
	@Override
	public double nodeSubstitution(Node n1, Node n2) {
		double dirac = diracCost.nodeSubstitution(n1, n2);
		return dirac + edgeAssignmentCost(n1.adjacentEdges(), n2.adjacentEdges());
	}

	private double edgeAssignmentCost(GXLEdge[] adjacentEdges1, GXLEdge[] adjacentEdges2) {
		// see slide 10-20
		// Da die Edges keine Label haben, sind die Edge label substitution cost immer 0. 
		// Daraus folgt, dass in diesem Fall die Cost Matrix für Edges nicht notwendig ist.
		// Sei E_1 die Anzahl an Edges in Node 1 und E_2 die Anzahl an Edges in Node 2, 
		// dann sind die Edge Kosten bei der Substitution:
	    // | E_1 - E_2 | * Ce
		return Math.abs(adjacentEdges1.length - adjacentEdges2.length) * diracCost.getC_e();
	}

}
