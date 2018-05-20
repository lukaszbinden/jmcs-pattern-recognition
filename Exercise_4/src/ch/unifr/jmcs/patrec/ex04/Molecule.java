package ch.unifr.jmcs.patrec.ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import ch.unifr.jmcs.patrec.ex04.algo.Node;
import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLGXL;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLNode;

public class Molecule {

	private GXLDocument graph;
	private String fileid;
	private String classId;
	private Node[] nodes;
	
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
	
	public int getNumNodes() {
		return getNodes().length;
	}
	
	public Node[] getNodes() {
		if (nodes == null) {
			List<Node> list = new ArrayList<>();
			GXLGXL gxlgxl = graph.getDocumentElement();
			Invariant.check(gxlgxl.getGraphCount() == 1);
			GXLGraph graph = gxlgxl.getGraphAt(0);
			int graphElementCount = graph.getGraphElementCount();
			IntStream.range(0, graphElementCount).forEach(i -> {
				GXLGraphElement elem  = graph.getGraphElementAt(i);
				if (elem instanceof GXLNode) {
					GXLNode node = (GXLNode) elem;
					list.add(new Node(node));				
				} 
			});
			this.nodes = list.toArray(new Node[list.size()]);
		}
		return nodes;
	}
	
	@Override
	public String toString() {
		return Molecule.class.getSimpleName() + "-" + graph.toString() + "-" + fileid + "-" + classId;
	}
}
