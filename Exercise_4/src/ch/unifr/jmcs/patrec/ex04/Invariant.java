package ch.unifr.jmcs.patrec.ex04;

public class Invariant {

	public static void check(boolean condition) {
		if (!condition) {
			throw new IllegalStateException();
		}
	}
	
}
