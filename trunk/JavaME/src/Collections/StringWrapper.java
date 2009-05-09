package Collections;

import java.io.UnsupportedEncodingException;

public class StringWrapper {
	private final String string;
	
	public StringWrapper() {
		this.string = new String();
	}
	
	public StringWrapper(String content) {
		this.string = new String(content);
	}

	public char charAt(int arg0) {
		return string.charAt(arg0);
	}

	public int compareTo(String arg0) {
		return string.compareTo(arg0);
	}

	public String concat(String str) {
		return string.concat(str);
	}

	public boolean endsWith(String suffix) {
		return string.endsWith(suffix);
	}

	public boolean equals(Object arg0) {
		return string.equals(arg0);
	}

	public boolean equalsIgnoreCase(String anotherString) {
		return string.equalsIgnoreCase(anotherString);
	}

	public byte[] getBytes() {
		return string.getBytes();
	}

	public byte[] getBytes(String enc) throws UnsupportedEncodingException {
		return string.getBytes(enc);
	}

	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		string.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	public int hashCode() {
		return string.hashCode();
	}

	public int indexOf(int arg0, int arg1) {
		return string.indexOf(arg0, arg1);
	}

	public int indexOf(int arg0) {
		return string.indexOf(arg0);
	}

	public int indexOf(String arg0, int arg1) {
		return string.indexOf(arg0, arg1);
	}

	public int indexOf(String str) {
		return string.indexOf(str);
	}

	public String intern() {
		return string.intern();
	}

	public int lastIndexOf(int arg0, int arg1) {
		return string.lastIndexOf(arg0, arg1);
	}

	public int lastIndexOf(int ch) {
		return string.lastIndexOf(ch);
	}

	public int length() {
		return string.length();
	}

	public boolean regionMatches(boolean arg0, int arg1, String arg2, int arg3,
			int arg4) {
		return string.regionMatches(arg0, arg1, arg2, arg3, arg4);
	}

	public String replace(char arg0, char arg1) {
		return string.replace(arg0, arg1);
	}

	public boolean startsWith(String prefix, int toffset) {
		return string.startsWith(prefix, toffset);
	}

	public boolean startsWith(String prefix) {
		return string.startsWith(prefix);
	}

	public String substring(int beginIndex, int endIndex) {
		return string.substring(beginIndex, endIndex);
	}

	public String substring(int beginIndex) {
		return string.substring(beginIndex);
	}

	public char[] toCharArray() {
		return string.toCharArray();
	}

	public String toLowerCase() {
		return string.toLowerCase();
	}

	public String toString() {
		return string.toString();
	}

	public String toUpperCase() {
		return string.toUpperCase();
	}

	public String trim() {
		return string.trim();
	}
	
	public String[] split(String seperator) {
		List list = new LinkedList();
		int index;
		String text = new String(this.string);
		while ((index = text.indexOf('\n')) != -1) {
			list.add(text.substring(0, index));
			text = text.substring(index + 1);
		}
		if (text.length() > 0) {
			list.add(text);
		}
		String[] array = new String[list.size()];
		Iterator it = list.getIterator();
		for(int i = 0; it.hasNext(); i++) {
			array[i] = (String)it.next();
		}
		return array;
	}

	
}
