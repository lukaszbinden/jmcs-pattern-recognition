package ch.unifr.jmcs.patrec.ex04.algo;

public interface ICostFunction {

	double nodeInsertion(Node n1);
	
	double nodeDeletion(Node n1);
	
	double nodeSubstitution(Node n1, Node n2);
	
	double edgeInsertion();
	
	double edgeDeletion();
	
}
