package ch.unifr.jmcs.patrec.ex04;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataSet implements Iterable<String> {

	private Map<String, String> entries = new HashMap<>();

	@Override
	public Iterator<String> iterator() {
		return entries.keySet().iterator();
	}
	
	public void add(String fileId, String classId) {
		entries.put(requireNonNull(fileId), requireNonNull(classId));
	}
	
	public String classId(String fileId) {
		return entries.get(fileId);
	}
	
}
