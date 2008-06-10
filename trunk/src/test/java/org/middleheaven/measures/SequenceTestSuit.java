package org.middleheaven.measures;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;
import org.middleheaven.util.CharacterIncrementor;
import org.middleheaven.util.NumberIncrementor;
import org.middleheaven.util.Range;
import org.middleheaven.util.sequence.IterableBasedSequence;
import org.middleheaven.util.sequence.Sequence;
import org.middleheaven.util.sequence.SequenceToken;


public class SequenceTestSuit {

	
	@Test
	public void testSequences(){
		
		Integer[] col = {new Integer(1), new Integer(2)};
		
		Sequence<Integer> a = IterableBasedSequence.sequenceFor(col);
		testSequence(a, Range.over(1,2).toList());
		
		Sequence<Integer> b = IterableBasedSequence.sequenceFor(Range.over(1,10).toList());
		
		testSequence(b, Range.over(1,10).toList());
		
		Sequence<Character> is2 = IterableBasedSequence.sequenceFor(Range.over('a','z', new CharacterIncrementor(1)));
		
		testSequence(is2, Range.over('a','z', new CharacterIncrementor(1)).toList());
		
	}
	
	
	private static <T> void testSequence(Sequence<T> seq, Collection<T> collection){
		SequenceToken<T> token;
		while ( (token=seq.next())!=null){
			assertTrue(collection.contains(token.getValue()));
		}
	}
}
