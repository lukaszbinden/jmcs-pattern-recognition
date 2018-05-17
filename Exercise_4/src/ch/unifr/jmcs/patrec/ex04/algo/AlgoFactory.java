package ch.unifr.jmcs.patrec.ex04.algo;

import ch.unifr.jmcs.patrec.ex04.algo.ext.HungarianAlgorithm;

public class AlgoFactory {

	private AlgoFactory() {}
	
	public static IBPMatcher bpMatcher() {
		return new BPMatcherImpl(hungarian());
	}
	
	public static IKnn knn(int k) {
		return new KnnImpl(k);
	}
	
	public static IHungarian hungarian() {
		return new HungarianAlgorithm();
	}
	
}
