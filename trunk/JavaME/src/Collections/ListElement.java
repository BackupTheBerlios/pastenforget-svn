package Collections;

public class ListElement {
	private final Object content;
	private ListElement next = null;
	private final ListElement previous;
	
	public ListElement(Object content, ListElement previous) {
		this.content = content;
		this.previous = previous;
	}
	
	public void setNext(ListElement next) {
		this.next = next;
	}
	
	public ListElement getNext() {
		return this.next;
	}
	
	public ListElement getPrevious() {
		return this.previous;
	}
	
	public Object getContent() {
		return this.content;
	}
	
}
