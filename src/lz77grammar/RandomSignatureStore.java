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

@SuppressWarnings("unused") class RandomSignatureStore implements Serializable {

    private static final long serialVersionUID = -872470916710859680L;

    private Random prior;

    private BidiMap<Signature, Character> u; // Universe
	private BidiMap<Signature, Pair> p; // Pairs
    private BidiMap<Signature, Power> r; // Power
    
    private Map<Signature, Float> signatures;

    private SequenceStore sequenceStore;

    private int max_sig;

    public RandomSignatureStore(){
        prior = new Random();
        signatures = new HashMap<>();

        u = new DualHashBidiMap<>();
		p = new DualHashBidiMap<>();
        r = new DualHashBidiMap<>();
        
        sequenceStore = new SequenceStore();

        max_sig = 0;
    }
    
    



    

}