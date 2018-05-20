package ch.unifr.jmcs.patrec.ex04.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.sourceforge.gxl.GXL;
import net.sourceforge.gxl.GXLAttr;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLValue;

public class Node {

	private GXLNode impl;
	private GXLEdge[] edges;

	public Node(GXLNode impl) {
		this.impl = impl;
		
		GXLGraph graph = (GXLGraph) impl.getParent();
		int cnt = graph.getGraphElementCount();
		List<GXLEdge> edges = new ArrayList<>();
		IntStream.range(0, cnt).forEach(c -> {
			GXLGraphElement e  = graph.getGraphElementAt(c);
			if (e instanceof GXLEdge) {
				GXLEdge edge = (GXLEdge) e;
				if (edge.getAttribute(GXL.FROM).equals(impl.getID())) {
					edges.add(edge);
				}
			}
		});
		this.edges = edges.toArray(new GXLEdge[edges.size()]);
	}

	public String symbol() {
		GXLAttr symbol = impl.getAttr("symbol");
		GXLValue value = symbol.getValue();
		return ((GXLString) value).getValue();
	}
	
	public GXLEdge[] adjacentEdges() {
		return edges;
	}
	
	public int numAdjacentEdges() {
		return edges.length;
	}
	
	
	
}
