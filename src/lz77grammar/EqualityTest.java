package lz77grammar;

import java.util.ArrayList;
import java.util.List;

/**
  * EqualityTest performs polynomial time equality testing between two strings that have been compressed
  * using LZ77 using the randomised data structure described the paper.
  
  *   Copyright 2020 Ashutosh Patra

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 * @author Ashutosh Patra
 */

 public class EqualityTest{

    static LZ77 lz77Compressor;
    static SignatureStore signatureStore;
    public static void main(String[] args){
      LZ77 compressor = new LZ77(32500,250);

      List<Reference> testOne = compressor.compress("aabbaaab");
      List<Reference> testTwo = compressor.compress("thisisatest");
      lz77Compressor = new LZ77(32800, 250);
      signatureStore = new SignatureStore();

      boolean result = checkEquals(testOne, testTwo);

      System.out.println("Hello World");

      // System.out.println(result);

  
      
    }

    public static boolean checkEquals(List<Reference> first, List<Reference> second){
      
      Converter converter = new Converter();

      Grammar cnfGrammarOne = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)first));
      Grammar cnfGrammarTwo = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)second));

      List<SequenceNode> a = signatureStore.storeSequence(cnfGrammarOne.evaluate());
		  List<SequenceNode> b = signatureStore.storeSequence(cnfGrammarTwo.evaluate());

      return a.get(a.size() - 1).element.getSig() == b.get(b.size() - 1).element.getSig();
      
    }


 }