package ch.unifr.jmcs.patrec.ex04.algo;

import ch.unifr.jmcs.patrec.ex04.algo.ext.HungarianAlgorithm;
import ch.unifr.jmcs.patrec.ex04.algo.impl.DiracCostFunctionImpl;

public class AlgoFactory {

	private AlgoFactory() {}
	
	public static IBPMatcher bpMatcher() {
		return new BPMatcherImpl(hungarian(), dirac());
	}
	
	public static IKnn knn(int k) {
		return new KnnImpl(k);
	}
	
	public static IHungarian hungarian() {
		return new HungarianAlgorithm();
	}
	
	public static ICostFunction dirac() {
		// TODO: optimize...
		double c_nInit = 3;
		double c_eInit = 2;
		return new DiracCostFunctionImpl(c_nInit, c_eInit);
	}
	
}
