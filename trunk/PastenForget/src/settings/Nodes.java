package settings;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Nodes extends ArrayList<Node> {
	private static final long serialVersionUID = 2374807541803198101L;
	private final NodeList nodeList;
	
	public Nodes(NodeList nodeList) {
		this.nodeList = nodeList;
	}
	
	@Override
	public Node get(int index) {
		return this.nodeList.item(index);
	}
	
	@Override
	public int size() {
		return this.nodeList.getLength();
	}
}
