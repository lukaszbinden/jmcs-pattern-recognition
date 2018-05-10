package ch.unifr.jmcs.patrec.ex04;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class DataSet implements Iterable<Entry<String, String>> {

	private Map<String, String> entries = new HashMap<>();

	@Override
	public Iterator<Entry<String, String>> iterator() {
		return entries.entrySet().iterator();
	}
	
	public void add(String fileId, String classId) {
		entries.put(requireNonNull(fileId), requireNonNull(classId));
	}
	
	public String classId(String fileId) {
		return entries.get(fileId);
	}
	
	public Stream<Entry<String, String>> stream() {
		return entries.entrySet().stream();
	}
	
}
