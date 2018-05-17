package ch.unifr.jmcs.patrec.ex04.algo;

public class AlgoFactory {

	private AlgoFactory() {}
	
	public static IBPMatcher bpMatcher() {
		return new BPMatcherImpl();
	}
	
	public static IKnn knn(int k) {
		return new KnnImpl(k);
	}
	
}
