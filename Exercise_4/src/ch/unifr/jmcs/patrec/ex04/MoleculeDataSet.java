package ch.unifr.jmcs.patrec.ex04;

import static java.util.Objects.requireNonNull;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import net.sourceforge.gxl.GXLDocument;

public class MoleculeDataSet implements Iterable<Entry<String, String>> {

	public static final String DUMMY_CLASS_ID = "dummyId";
	
	private Map<String, String> entries = new HashMap<>();
	private Map<String, Molecule> molecules = new HashMap<>();

	@Override
	public Iterator<Entry<String, String>> iterator() {
		return entries.entrySet().iterator();
	}
	
	public void add(String fileId, String classId) {
		entries.put(requireNonNull(fileId), classId);
	}
	
	public String classId(String fileId) {
		return entries.get(fileId);
	}
	
	public Stream<Entry<String, String>> stream() {
		return entries.entrySet().stream();
	}
	
	public int size() {
		return entries.size();
	}
	
	public Molecule get(String fileId) {
		return molecules.get(fileId);
	}

	public void loadAll() {
		for (Entry<String, String> entry : this) {
			String fileId = entry.getKey();
			String classId = entry.getValue();
			Molecule m = create(fileId, classId);
			Invariant.check(!molecules.containsKey(fileId));
			molecules.put(fileId, m);
		}
	}
	
	private static Molecule create(String fileId, String classId) {
		try {
			String basePath = "data/gxl/";
			if (!fileId.endsWith(".gxl")) {
				fileId += ".gxl";
			} else {
				basePath = "data/validation/gxl/";
			}
			GXLDocument gxl = new GXLDocument(Paths.get(basePath + fileId).toFile());
			return new Molecule(gxl, fileId, classId);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}		
}
