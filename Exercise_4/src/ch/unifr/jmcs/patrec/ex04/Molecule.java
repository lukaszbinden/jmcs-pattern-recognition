package ch.unifr.jmcs.patrec.ex04;

import net.sourceforge.gxl.GXLDocument;

public class Molecule {

	private GXLDocument graph;
	private String fileid;
	private String classId;
	
	public Molecule(GXLDocument graph, String fileid, String classId) {
		this.graph = graph;
		this.fileid = fileid;
		this.classId = classId;
	}
	
	public GXLDocument getGraph() {
		return graph;
	}
	
	public String getClassId() {
		return classId;
	}
	
	public String getFileid() {
		return fileid;
	}
	
	@Override
	public String toString() {
		return Molecule.class.getSimpleName() + "-" + graph.toString() + "-" + fileid + "-" + classId;
	}
}
