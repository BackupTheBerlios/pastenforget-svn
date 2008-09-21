package core.hoster;

import java.util.Iterator;

import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator {
	private int counter = 0;
	private NodeList nl;
	
	
	public NodeListIterator(NodeList nl) {
		this.nl = nl;
	}
	
	public Object next() {
		return nl.item(counter++);
	}
	
	
	public boolean hasNext() {
		return counter < nl.getLength();
	}
	
	public void remove() {
	}
	
}
