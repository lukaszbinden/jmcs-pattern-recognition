package ch.unifr.jmcs.patrec.ex04.algo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.unifr.jmcs.patrec.ex04.Molecule;

class KnnImpl implements IKnn {

	private static final String ACTIVE = "a";
	private static final String INACTIVE = "i";
	
	private int k;

	KnnImpl(int k) {
		this.k = k;
	}
	
	@Override
	public String classify(Molecule validG, Map<Integer, List<Molecule>> distances) {

		//  TODO: double check this line...
		List<Molecule> kNN = distances.values().stream().flatMap(ms -> ms.stream()).limit(k).collect(Collectors.toList());
//		distances.keySet().stream().limit(k).forEach(action);
		
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
		
		if (numA == numI) {
			throw new IllegalStateException("numA == numI : " + numA + " validG= "+ validG + " kNN=" + kNN);
		}
		
		return numA > numI ? ACTIVE : INACTIVE;
	}

}
