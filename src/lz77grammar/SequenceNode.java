package lz77grammar;

import java.io.Serializable;

/**
 * SequenceNode object used to represent a compressed sequence in our SignatureStore data structure.
 * @author Ashutosh Patra
 * 
 */
class SequenceNode implements TreePrinterNode, Serializable {

	private static final long serialVersionUID = -8681618095183150263L;
	
	private SequenceNode left;
	private SequenceNode right;
	Element element;
	int size;
	String text;
	
	SequenceNode(SequenceNode left, SequenceNode right, Element element, int position, int size) {
		this.left = left;
		this.right = right;
		this.element = element;
		this.size = size;
	}

	public int size(){
		if (this.left.equals(null) && this.right.equals(null)){
			return 1;
		}
		int size = 0;
		if (!(this.left.equals(null))){
			size += this.left.size();
		}
		if (!(this.right.equals(null))){
			size += this.right.size();
		}
		
		return size;
	}


	@Override
	public SequenceNode getLeft() {
		return left;
	}

	@Override
	public SequenceNode getRight() {
		return right;
	}

	/**
	 * Text representation of current node, used in TreePrinter.
	 */
	@Override
	public String getText() {
		if(text != null) {
			return text;
		}
		return Integer.toString(element.getSig());
	}

}