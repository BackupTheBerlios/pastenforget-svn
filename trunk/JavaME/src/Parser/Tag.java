package Parser;

import Collections.LinkedList;

public class Tag {
	private final String content;
	
	public Tag(String content) {
		this.content = content;
	}
	
	public LinkedList getAllTags() {
		int indexStart;
		int indexEnd;
		LinkedList lList = new LinkedList();
		String newContent = new String(this.content);
		while((indexStart = newContent.indexOf("<")) != -1) {
			newContent = newContent.substring(indexStart);
			indexEnd = newContent.indexOf(">") + 1;
			String tag = newContent.substring(0, indexEnd);
			newContent = newContent.substring(indexEnd);
			lList.add(tag);
		}
		return lList;
	}
}
