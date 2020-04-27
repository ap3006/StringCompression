package lz77grammar;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    static void equalityTestOne(){

      LZ77 compressor = new LZ77(32500,250);

      List<Reference> testOne = compressor.compress("aabbaaab");
      List<Reference> testTwo = compressor.compress("aabbaaab");

      Converter converter = new Converter();

      Grammar cnfGrammarOne = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testOne));
      Grammar cnfGrammarTwo = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testTwo));

      assertTrue(cnfGrammarOne.checkEquals(cnfGrammarTwo));
    }


    @Test
    static void equalityTestTwo(){

      LZ77 compressor = new LZ77(32500,250);

      List<Reference> testOne = compressor.compress("thisisatest");
      List<Reference> testTwo = compressor.compress("thisisatest");

      Converter converter = new Converter();

      Grammar cnfGrammarOne = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testOne));
      Grammar cnfGrammarTwo = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testTwo));

      assertTrue(cnfGrammarOne.checkEquals(cnfGrammarTwo));
    }

    @Test
    static void nonEqualityTestOne(){

      LZ77 compressor = new LZ77(32500,250);

      List<Reference> testOne = compressor.compress("thisisatest");
      List<Reference> testTwo = compressor.compress("aabbaaab");

      Converter converter = new Converter();

      Grammar cnfGrammarOne = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testOne));
      Grammar cnfGrammarTwo = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testTwo));

      assertFalse(cnfGrammarOne.checkEquals(cnfGrammarTwo));
    }

    @Test
    static void nonEqualityTestTwo(){

      LZ77 compressor = new LZ77(32500,250);

      List<Reference> testOne = compressor.compress("thisisatest");
      List<Reference> testTwo = compressor.compress("thisisates");

      

      Converter converter = new Converter();

      Grammar cnfGrammarOne = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testOne));
      Grammar cnfGrammarTwo = converter.constructGrammar(lz77Compressor.getTuples((ArrayList<Reference>)testTwo));

      assertFalse(cnfGrammarOne.checkEquals(cnfGrammarTwo));
    }


 }