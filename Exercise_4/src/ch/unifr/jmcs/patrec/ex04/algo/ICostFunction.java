package ch.unifr.jmcs.patrec.ex04.algo;

import net.sourceforge.gxl.GXLEdge;

public interface ICostFunction {

	double nodeInsertion(Node n1);
	
	double nodeDeletion(Node n1);
	
	double nodeSubstitution(Node n1, Node n2);
	
	double edgeSubstitution(GXLEdge e1, GXLEdge e2);
	
	double edgeInsertion();
	
	double edgeDeletion();
	
}
