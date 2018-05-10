package ch.unifr.jmcs.patrec.ex04.algo;

public class AlgoFactory {

	private AlgoFactory() {}
	
	public static IBPMatcher bpMatcher() {
		return null; // TODO: impl...
	}
	
	public static IKnn knn(int k) {
		return new KnnImpl(k);
	}
	
}
