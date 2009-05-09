package Collections;
import java.lang.ArrayIndexOutOfBoundsException;


public final class LinkedList implements List {
	private int size = 0;
	
	private ListElement startElement;
	
	public LinkedList() { }
	
	public LinkedList(Object[] array) {
		for(int i = 0; i < array.length; i++) {
			this.add(array[i]);
		}
	}
	
	public int size() {
		return this.size;
	}
	
	public void add(Object content) {
		if(this.startElement == null) {
			this.startElement = new ListElement(content, null);
		} else {
			ListElement element = this.startElement;
			while(element.getNext() != null) {
				element = element.getNext();
			}
			element.setNext(new ListElement(content, element));
		}
		this.size++;
	}
	
	public Object get(int index) {
		if(index >= 0 && index < this.size) {
			ListElement element = this.startElement;
			for(int i = 0; i < index; i++) {
				element = element.getNext();
			}
			return element.getContent();
		} else {
			throw new ArrayIndexOutOfBoundsException(index + " >= " + this.size);
		}
	}
	
	public Object[] toArray() {
		int size = this.size();
		Object[] array = new Object[size];
		ListElement element = this.startElement;
		for(int i = 0; i < size; i++) {
			array[i] = element.getContent();
			element = element.getNext();
		}
		return array;
	}

	public void remove(int index) {
		if(index >= 0 && index < this.size) {
			ListElement element = this.startElement;
			for(int i = 0; i < index; i++) {
				element = element.getNext();
			}
			ListElement next = element.getNext();
			ListElement previous = element.getPrevious();
			if(previous == null) {
				this.startElement = next;
			} else {
				previous.setNext(next);
			}
			this.size--;
		} else {
			throw new ArrayIndexOutOfBoundsException(index + " >= " + this.size);
		}
	}

	public void remove(Object o) {
		ListElement element = this.startElement;
		while(element != null) {
			if(element.getContent().equals(o)) {
				ListElement next = element.getNext();
				ListElement previous = element.getPrevious();
				if(previous == null) {
					this.startElement = next;
				} else {
					previous.setNext(next);
				}
				this.size--;
				return;
			}
			element = element.getNext();
		}
		throw new ArrayIndexOutOfBoundsException("element not in list");
	}
	
	public Iterator getIterator() {
		return new Iterator() {
			private ListElement listElement = startElement;
			
			public boolean hasNext() {
				return this.listElement != null;
			}
			
			public Object next() {
				Object content = this.listElement.getContent();
				this.listElement = this.listElement.getNext();
				return content;
			}
		};
	}
	
}
