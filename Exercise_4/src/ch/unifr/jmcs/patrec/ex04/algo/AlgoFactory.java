package ch.unifr.jmcs.patrec.ex04.algo;

import ch.unifr.jmcs.patrec.ex04.algo.ext.HungarianAlgorithm;
import ch.unifr.jmcs.patrec.ex04.algo.impl.DiracCostFunctionImpl;

public class AlgoFactory {

	private AlgoFactory() {}
	
	public static IBPMatcher bpMatcher(double c_n, double c_e) {
		return new BPMatcherImpl(hungarian(), dirac(c_n, c_e));
	}
	
	public static IKnn knn(int k) {
		return new KnnImpl(k);
	}
	
	public static IHungarian hungarian() {
		return new HungarianAlgorithm();
	}
	
	public static ICostFunction dirac(double c_n, double c_e) {
		return new DiracCostFunctionImpl(c_n, c_e);
	}
	
}
