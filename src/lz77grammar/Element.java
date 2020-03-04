package lz77grammar;

import java.io.Serializable;

/**
 * Element interface for SignatureStore data structure.
 * @author Ashutosh Patra
 * 
 */
interface Element extends Serializable {
	/**
	 * @return The character an element produces (if applicable)
	 */
	public char getChar();
	/**
	 * @return The power an element produces (if applicable)
	 */
	public int getPow();
	/**
	 * @return The signature an element produces (if applicable)
	 */
	public int getSig();

	/**
	 * @return The mark of the element after block decomposition
	 */

	public boolean getMark();

	public void setSig(int signature);

	public void setPow(int power);

	public void setMark();

	public int getBlockSize();

	public void setBlockSize(int blockSize);

	// public int getBlockPosition();

	// public void setBlockPosition();

}
