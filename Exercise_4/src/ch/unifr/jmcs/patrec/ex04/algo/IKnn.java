package ch.unifr.jmcs.patrec.ex04.algo;

import java.util.List;
import java.util.Map;

import ch.unifr.jmcs.patrec.ex04.Molecule;

public interface IKnn {

	String classify(Molecule validG, Map<Integer, List<Molecule>> distances);

}
