package lz77grammar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
  * SignatureStore data structure used to convert sequences into unique signatures. Using algorithms discussed
  * In report associated with this project. All functions are discussed in report.
  
  *   Copyright 2019 Callum Onyenaobiya

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 * @author Callum Onyenaobiya
 */
@SuppressWarnings("unused") class SignatureStore implements Serializable {

	private static final long serialVersionUID = -872470916710859680L;

	private Random prior;

	private BidiMap<Signature, Character> u; // Universe
	private BidiMap<Signature, Pair> p; // Pairs
	private BidiMap<Signature, Power> r; // Power

	private Map<Signature, Float> signatures;
	
	private SequenceStore sequenceStore;

	private int max_sig;

	public SignatureStore() {
		prior = new Random();
		signatures = new HashMap<>();

		u = new DualHashBidiMap<>();
		p = new DualHashBidiMap<>();
		r = new DualHashBidiMap<>();

		sequenceStore = new SequenceStore();
		
		max_sig = 0;

	}
	
	/**
	 * Recursively builds a parse tree of sequence represented by given signature.
	 * @param s Signature representing sequence
	 * @return Parse tree reprsenting sequence of signatures.
	 */
	private SequenceNode viewTree(Signature s) {
		SequenceNode node = null;
		if(u.containsKey(s)) {
			node = new SequenceNode(null, null, s, 0, 0);
			node.text = Character.toString(u.get(s));
		} else if(p.containsKey(s)) {
			node = new SequenceNode(viewTree(p.get(s).getL()), viewTree(p.get(s).getR()), s, 0, 0);
		} else if(r.containsKey(s)) {
			node = new SequenceNode(null, null,s,0,0);
			node.text = r.get(s).toString();
		}
		return node;
	}
	
	
	/**
	 * @param text Converts sequence into unique signature using algorithms discussed in report.
	 * @return List of compressed binary trees which build signature.
	 */
	List<SequenceNode> storeSequence(String text) {
		List<SequenceNode> sequence = new ArrayList<SequenceNode>();
		sequence.add(sequenceStore.sequenceToTree(group(text)));
		int i = 1;
		while(true) {
			sequence.add(2*(i)-1, sequenceStore.sequenceToTree(elpow(sequenceStore.treeToSequence(sequence.get(2*(i)-2)))));
			if(sequence.get(2*(i)-1).size == 0) {
				break;
			}
			sequence.add(2*(i), sequenceStore.sequenceToTree(shrink(sequenceStore.treeToSequence(sequence.get(2*(i)-2)))));
			if(sequence.get(2*(i)).size == 0) {
				break;
			}
			i++;
		}
		TreePrinter treeprinter = new TreePrinter();
		treeprinter.print(viewTree(new Signature(sequence.get(sequence.size() - 1).element.getSig())));
		sequenceStore.addSequence(sequence);
		return sequence;
	}
	
	/**
	 * Builds new signature from two existing signatures.
	 * @param a
	 * @param b
	 * @return List of compressed binary trees which build signature.
	 */
	List<SequenceNode> concatenate(List<SequenceNode> a, List<SequenceNode> b) {
		List<Element> groups = fixGroups(ListUtils.union(sequenceStore.treeToSequence(a.get(0)),
				sequenceStore.treeToSequence(b.get(0))));
		List<Element> elpowgroups = elpow(groups);
		
		SequenceNode s = sequenceStore.sequenceToTree(groups);
		SequenceNode elpows = sequenceStore.sequenceToTree(elpowgroups);
		
		List<SequenceNode> sequence = new ArrayList<SequenceNode>();
		if(s.size <= 1) {
			sequence.add(s);
			sequence.add(elpows);
			return sequence;
		}
		while (s.size >= 1) {
			sequence.add(s);
			sequence.add(sequenceStore.sequenceToTree(elpow(sequenceStore.treeToSequence(s))));
			s = sequenceStore.sequenceToTree(shrink(sequenceStore.treeToSequence(s)));
		}
		sequence.add(s);
		TreePrinter treeprinter = new TreePrinter();
		treeprinter.print(viewTree(new Signature(sequence.get(sequence.size() - 1).element.getSig())));
		sequenceStore.addSequence(sequence);
		return sequence;
	}

	/**
	 * @author Ashutosh Patra
	 * 
	 * @param a
	 * @param b
	 * @return List of compressed binary trees which build signature.
	 */
	List<SequenceNode> ranConcatenate(List<SequenceNode> a, List<SequenceNode> b) {
		List<Element> groups = fixGroups(ListUtils.union(sequenceStore.treeToSequence(a.get(0)),
				sequenceStore.treeToSequence(b.get(0))));
		List<Element> elpowgroups = elpow(groups);
		
		SequenceNode s = sequenceStore.sequenceToTree(groups);
		SequenceNode elpows = sequenceStore.sequenceToTree(elpowgroups);
		
		List<SequenceNode> sequence = new ArrayList<SequenceNode>();
		if(s.size <= 1) {
			sequence.add(s);
			sequence.add(elpows);
			return sequence;
		}
		while (s.size >= 1) {

			sequence.add(s);
			sequence.add(sequenceStore.sequenceToTree(elpow(sequenceStore.treeToSequence(s))));
			s = sequenceStore.sequenceToTree(shrink(sequenceStore.treeToSequence(s)));
		}
		
		sequence.add(s);
		TreePrinter treeprinter = new TreePrinter();
		treeprinter.print(viewTree(new Signature(sequence.get(sequence.size() - 1).element.getSig())));
		sequenceStore.addSequence(sequence);
		return sequence;
	}

	/**
	 * @author Ashutosh Patra
	 * 
	 * @param text
	 * @return
	 */
	List<List<SequenceNode>> ranSplit(List<SequenceNode> s, int i) {
		
		List<Element> sequence = sequenceStore.treeToSequence(s.get(0));

		List<Element> a = sequence.subList(0,i);
		List<Element> b = sequence.subList(i+1,sequence.size());

		List<Element> elpowA = elpow(a);
		List<Element> elpowB = elpow(b);

		SequenceNode s1 = sequenceStore.sequenceToTree(a);
		SequenceNode s2 = sequenceStore.sequenceToTree(b);

		SequenceNode elpowS1 = sequenceStore.sequenceToTree(elpowA);
		SequenceNode elpowS2 = sequenceStore.sequenceToTree(elpowB);

		List< List<SequenceNode>> result = new ArrayList<List<SequenceNode>>();
		List<SequenceNode> sequenceOne = new ArrayList<SequenceNode>();
		List<SequenceNode> sequenceTwo = new ArrayList<SequenceNode>();
		if (s1.size <= 1){
			sequenceOne.add(s1);
			sequenceOne.add(elpowS1);
			result.add(sequenceOne);
		}
		else{

			while (s1.size >= 1) {

				sequenceOne.add(s1);
				sequenceOne.add(sequenceStore.sequenceToTree(elpow(sequenceStore.treeToSequence(s1))));
				s1 = sequenceStore.sequenceToTree(shrink(sequenceStore.treeToSequence(s1)));
			}
			result.add(sequenceOne);
			sequenceStore.addSequence(sequenceOne);

		}

		if (s2.size <= 1){
			sequenceOne.add(s2);
			sequenceOne.add(elpowS2);
			result.add(sequenceTwo);
		}
		else{
			while (s2.size >= 1) {

				sequenceTwo.add(s2);
				sequenceTwo.add(sequenceStore.sequenceToTree(elpow(sequenceStore.treeToSequence(s2))));
				s2 = sequenceStore.sequenceToTree(shrink(sequenceStore.treeToSequence(s2)));
			}
			result.add(sequenceTwo);
			sequenceStore.addSequence(sequenceTwo);
		}
		
		return result;
	}

	/**
	 * Compresses input string into Power objects, like run-length encoding.
	 * @param text
	 * @return
	 */
	private List<Element> group(String text) {
		List<Element> list = new ArrayList<>();
		char current = text.charAt(0);
		int count = 1;
		for (int i = 1; i < text.length(); i++) {
			if (current == text.charAt(i)) {
				count++;
				continue;
			}
			list.add(new Power(count, current));
			current = text.charAt(i);
			count = 1;
		}
		list.add(new Power(count, current));
		return list;
	}
	
	/**
	 * Used during concatenation, fixes misaligned groupings i.e (a^3)(a^7) becomes a^10
	 * @param list
	 * @return
	 */
	private List<Element> fixGroups(List<Element> list) {
		List<Element> newList = list;
		for(int i = 0; i < newList.size()-1; i++) {
			if(newList.get(i).getChar() == newList.get(i+1).getChar()) {
				newList.get(i).setPow(newList.get(i).getPow()+newList.get(i+1).getPow());
				newList.remove(i+1);
			}
		}
		return newList;
	}

	private List<Element> elpow(List<Element> list) {
		List<Element> result = new ArrayList<Element>();
		for (Element p : list) {
			result.add(sig(Collections.singletonList(p)));
		}
		return result;
	}

	private Signature getSigU(char c) {
		if (u.inverseBidiMap().containsKey(c)) {
			return u.inverseBidiMap().get(c);
		}
		Float priority = prior.nextFloat();
		while (signatures.containsValue(priority)) {
			priority = prior.nextFloat();
		}

		Signature signature = new Signature(max_sig++);
		signatures.put(signature, priority);
		u.put(signature, c);
		return signature;
	}

	private Signature getSigR(Power power) {
		if (r.inverseBidiMap().containsKey(power)) {
			return r.inverseBidiMap().get(power);
		}
		Float priority = prior.nextFloat();
		while (signatures.containsValue(priority)) {
			priority = prior.nextFloat();
		}
		Signature signature = new Signature(max_sig++);
		signatures.put(signature, priority);
		r.put(signature, power);
		return signature;
	}

	private Signature getSigP(Pair pair) {
		if (p.inverseBidiMap().containsKey(pair)) {
			return p.inverseBidiMap().get(pair);
		}
		Float priority = prior.nextFloat();
		while (signatures.containsValue(priority)) {
			priority = prior.nextFloat();
		}

		Signature signature = new Signature(max_sig++);
		signatures.put(signature, priority);
		p.put(signature, pair);
		return signature;
	}

	private Signature sig(List<Element> list) {
		if (list.size() == 1 & list.get(0).getPow() == 1) {
			return Sig(list);
		} else if (list.size() == 1 && list.get(0).getPow() > 1) {
			return getSigR(new Power(list.get(0).getPow(), list.get(0).getChar()));
		}
		return sig(shrink(list));
	}

	private List<Element> shrink(List<Element> list) {
		List<Element> signatures = elpow(list);
		List<List<Element>> blocks = blocksRandomized(signatures);
		List<Element> result = new ArrayList<>();
		for (List<Element> block : blocks) {
			result.add(Sig(block));
		}

		return result;
	}

	private int getElementBlock(Element z, List<Element> list){
		int position = 1;

		for (int i = 1; i < list.size() - 1; i++) {
			if ((Float.compare(signatures.get(list.get(i - 1)), signatures.get(list.get(i))) > 0)
					&& (Float.compare(signatures.get(list.get(i)), signatures.get(list.get(i + 1))) < 0)) {
				position += 1;
			}
			if (z.equals(list.get(i-1))){
				break;
			}
		}

		return position;

	}

	private List<List<Element>> blocksRandomized(List<Element> list) {
		List<List<Element>> blocks = new ArrayList<>();
		List<Element> currentBlock = new ArrayList<>();
		currentBlock.add(list.get(0));
		for (int i = 1; i < list.size() - 1; i++) {
			if ((Float.compare(signatures.get(list.get(i - 1)), signatures.get(list.get(i))) > 0)
					&& (Float.compare(signatures.get(list.get(i)), signatures.get(list.get(i + 1))) < 0)) {
				list.get(i).setMark();
				blocks.add(currentBlock);
				currentBlock = new ArrayList<>();
			}
			currentBlock.add(list.get(i));
		}
		currentBlock.add(list.get(list.size() - 1));
		blocks.add(currentBlock);

		for (List<Element> block : blocks) {
			for (Element s : block) {
				s.setBlockSize(block.size());
			}
		}
		return blocks;
	}

	private Signature Sig(List<Element> list) {
		for (Element e : list) {
		}
		if (list.size() == 1 && signatures.containsKey(list.get(0))) {
			return (Signature) list.get(0);
		} else if (list.size() == 1 && list.get(0) instanceof Power) {
			return getSigU(list.get(0).getChar());
		} else if (list.size() == 2) {
			return getSigP(new Pair(Sig(list.subList(0, 1)), Sig(list.subList(1, 2))));
		}
		List<Element> newList = new ArrayList<>(list.subList(0, 1));
		newList.add(Sig(list.subList(1, list.size())));
		return Sig(newList);
	}
}
