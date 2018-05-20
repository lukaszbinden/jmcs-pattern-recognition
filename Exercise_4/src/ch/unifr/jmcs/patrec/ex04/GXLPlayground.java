package ch.unifr.jmcs.patrec.ex04;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import net.sourceforge.gxl.GXL;
import net.sourceforge.gxl.GXLAttr;
import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGXL;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLValue;

public class GXLPlayground {

	public static void main(String[] args) throws Exception {
		
		DataSet playSet = Main.load("data/play.txt");
		
		for (Entry<String, String> entry : playSet) {
			String fileId = entry.getKey();
			GXLDocument gxl = new GXLDocument(Paths.get("data/gxl/" + fileId + ".gxl").toFile());
			System.out.println(gxl);
			GXLGXL gxlgxl = gxl.getDocumentElement();
			Invariant.check(gxlgxl.getGraphCount() == 1);
			GXLGraph graph = gxlgxl.getGraphAt(0);
			int graphElementCount = graph.getGraphElementCount();
			IntStream.range(0, graphElementCount).forEach(i -> {
				GXLGraphElement elem  = graph.getGraphElementAt(i);
				System.out.println("ELEMENT: " + elem + " --> ");
				if (elem instanceof GXLNode) {
					GXLNode node = (GXLNode) elem;
					GXLAttr symbol = node.getAttr("symbol");
					GXLValue value = symbol.getValue();
					System.out.println("NODE: " + symbol.getName() + " = " + ((GXLString) value).getValue()
							+ ", ID = " + node.getID());
					
					
					GXLGraph graph2 = (GXLGraph) node.getParent();
					int cnt = graph2.getGraphElementCount();
					List<GXLEdge> edges = new ArrayList<>();
					IntStream.range(0, cnt).forEach(c -> {
						GXLGraphElement e  = graph2.getGraphElementAt(c);
						if (e instanceof GXLEdge) {
							GXLEdge edge = (GXLEdge) e;
							if (edge.getAttribute(GXL.FROM).equals(node.getID())) {
								edges.add(edge);
							}
						}
					});
					
					System.out.println(edges);
					
					
				} 
				System.out.println("ELEMENT: <-- ");
			});
			
			System.out.println(graph);
			
		}
		
	}
	
//	for (Entry<String, String> entry : playSet) {
//		String fileId = entry.getKey();
//		GXLDocument gxl = new GXLDocument(Paths.get("data/gxl/" + fileId + ".gxl").toFile());
//		System.out.println(gxl);
//		GXLGXL gxlgxl = gxl.getDocumentElement();
//		Invariant.check(gxlgxl.getGraphCount() == 1);
//		GXLGraph graph = gxlgxl.getGraphAt(0);
//		int graphElementCount = graph.getGraphElementCount();
//		IntStream.range(0, graphElementCount).forEach(i -> {
//			GXLGraphElement elem  = graph.getGraphElementAt(i);
//			System.out.println("ELEMENT: " + elem + " --> ");
//			if (elem instanceof GXLNode) {
//				GXLNode node = (GXLNode) elem;
//				GXLAttr symbol = node.getAttr("symbol");
//				GXLValue value = symbol.getValue();
//				System.out.println("NODE: " + symbol.getName() + " = " + ((GXLString) value).getValue()
//						+ ", ID = " + node.getID());
//				
//			} else if (elem instanceof GXLEdge) {
//				GXLEdge edge = (GXLEdge) elem;
//				System.out.println("EDGE: " + edge + " FROM: " + edge.getAttribute(GXL.FROM) + " TO: " + edge.getAttribute(GXL.TO));
//				
//				List<GXLElement> node = IntStream.range(0, graph.getChildCount())
//					.mapToObj(c -> graph.getChildAt(c))
//					.filter(child -> child instanceof GXLNode)
//					.filter(child -> child.getAttribute(GXL.ID).equals(edge.getAttribute(GXL.FROM)))
//					.collect(Collectors.toList());
//				System.out.println("FROM NODE: " + node.get(0).getAttribute(GXL.ID));
//				
//				
//			}
//			System.out.println("ELEMENT: <-- ");
//		});
//		
//		System.out.println(graph);
//		
//	}
}
