package core.hoster;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator<Node> {
	private int counter = 0;
	private final NodeList nl;
	
	
	public NodeListIterator(NodeList nl) {
		this.nl = nl;
	}
	
	public Node next() {
		return nl.item(this.counter++);
	}
	
	
	public boolean hasNext() {
		return this.counter < this.nl.getLength();
	}
	
	public void remove() {
	}
	
}
