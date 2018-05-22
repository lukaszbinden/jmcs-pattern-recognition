package ch.unifr.jmcs.patrec.ex04.algo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.unifr.jmcs.patrec.ex04.Molecule;

class KnnImpl implements IKnn {

	private static final String ACTIVE = "a";
	private static final String INACTIVE = "i";
	
	private int k;

	KnnImpl(int k) {
		this.k = k;
	}
	
	@Override
	public String classify(Molecule molecule, Map<Integer, List<Molecule>> distances) {
		List<Molecule> kNN = kNearest(distances, k).collect(Collectors.toList());
		
		//System.out.println("kNN for " + molecule.getFileid() + ": " + kNN.stream().map(m -> m.getFileid()).collect(Collectors.joining(",")));
		
		int numA = 0, numI = 0;
		for (Molecule m : kNN) {
			if (m.getClassId().equalsIgnoreCase(ACTIVE)) {
				numA++;
			} else if (m.getClassId().equalsIgnoreCase(INACTIVE)) {
				numI++;
			} else {
				throw new IllegalArgumentException(m.toString());
			}
		}
		// System.out.println("kNN = " + k + ": active=" + numA + ", inactive=" + numI);
		
		if (numA == numI) {
			System.out.println("TIE occured: [molecule=" + molecule + ", k=" + k + ", kNN=" + kNN + "]");
			// tie resolution: use 1-NN
			Molecule tieBreaker = kNearest(distances, 1).findFirst().get();
			System.out.println("Break tie with 1-NN: " + tieBreaker);
			return tieBreaker.getClassId();
		}
		
		return numA > numI ? ACTIVE : INACTIVE;
	}

	private Stream<Molecule> kNearest(Map<Integer, List<Molecule>> distances, int kNearest) {
//		AtomicReference<String> refLog = new AtomicReference<>("");
//		distances.entrySet().stream().limit(k).forEach(entry -> {
//			refLog.set(refLog.get() + "distance " + entry.getKey() + " has " + entry.getValue().size() + " molecule(s).\n");
//		}); 
//		System.out.println("k=" + k + ":\n" + refLog.get());
		
		return distances.values().stream().flatMap(ms -> ms.stream()).limit(kNearest);
	}

	public static void main(String[] args) {
		Map<Integer, List<String>> distances = new TreeMap<>();
		distances.put(3, Arrays.asList("a", "b", "c"));
		distances.put(1, Arrays.asList("aa", "bb", "cc"));
		distances.put(6, Arrays.asList("aaa", "bbb", "ccc"));
		
		List<String> kNN = distances.values().stream().flatMap(ms -> ms.stream()).limit(4).collect(Collectors.toList());
		System.out.println(kNN);
	}
	
}
