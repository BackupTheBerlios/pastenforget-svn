package Collections;

public interface List {

	public void add(Object o);
	public void remove(int index);
	public void remove(Object o);
	public Object get(int index);
	public Object[] toArray();
	public int size();
	public Iterator getIterator();
}
