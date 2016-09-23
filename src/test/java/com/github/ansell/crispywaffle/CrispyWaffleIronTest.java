package com.github.ansell.crispywaffle;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CrispyWaffleIronTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void testGetSequenceLengthMinimum() throws Exception {
		CrispyWaffleIron testIron = CrispyWaffleIron.fromWords(Arrays.asList("111 AAA"));
		assertEquals(3, testIron.getSequenceLength());
	}

	@Test
	public final void testGetSequenceLengthFour() throws Exception {
		CrispyWaffleIron testIron = CrispyWaffleIron.fromWords(Arrays.asList("1111 AAA"));
		assertEquals(4, testIron.getSequenceLength());
	}

	@Test
	public final void testFromWordsEmpty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Word list was empty");
		CrispyWaffleIron.fromWords(Arrays.asList());
	}

	@Test
	public final void testFromWordsSingleSpace() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Found an unrecognised line in the word list on line 1: ");
		CrispyWaffleIron.fromWords(Arrays.asList(" "));
	}

	@Test
	public final void testFromWordsNoWord() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Found an unrecognised line in the word list on line 1: 111 ");
		CrispyWaffleIron.fromWords(Arrays.asList("111 "));
	}

	@Test
	public final void testFromWordsNoWordShortKey1() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Found an unrecognised line in the word list on line 1: 1 ");
		CrispyWaffleIron.fromWords(Arrays.asList("1 "));
	}

	@Test
	public final void testFromWordsNoWordShortKey2() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Found an unrecognised line in the word list on line 1: 11 ");
		CrispyWaffleIron.fromWords(Arrays.asList("11 "));
	}

	@Test
	public final void testFromWordsShortKey1() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Cannot generate passphrases from this list due to it containing shorter sequences than necessary");
		CrispyWaffleIron.fromWords(Arrays.asList("1 AAA"));
	}

	@Test
	public final void testFromWordsShortKey2() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Cannot generate passphrases from this list due to it containing shorter sequences than necessary");
		CrispyWaffleIron.fromWords(Arrays.asList("11 AAA"));
	}

	@Test
	public final void testFromWordsShortValue1() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Cannot generate passphrases from this list due to it containing shorter passphrase words than necessary");
		CrispyWaffleIron.fromWords(Arrays.asList("111 A"));
	}

	@Test
	public final void testFromWordsShortValue2() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Cannot generate passphrases from this list due to it containing shorter passphrase words than necessary");
		CrispyWaffleIron.fromWords(Arrays.asList("111 AA"));
	}

	@Test
	public final void testFromWordsShortValueAfterValid() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Cannot generate passphrases from this list due to it containing shorter passphrase words than necessary");
		CrispyWaffleIron.fromWords(Arrays.asList("111 AAA", "112 AB"));
	}

	@Test
	public final void testFromWordsValueWithSpace() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Diceware word contained a space: AA A");
		CrispyWaffleIron.fromWords(Arrays.asList("111 AA A", "112 AAB"));
	}

	@Test
	public final void testFromWordsNegativeKey() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(
				"Found a sequence that did not solely use digits: -111");
		CrispyWaffleIron.fromWords(Arrays.asList("-111 AAA", "112 AAB"));
	}

	@Test
	public final void testFromWordsTwoValidValues() throws Exception {
		CrispyWaffleIron testIron = CrispyWaffleIron.fromWords(Arrays.asList("111 AAA", "112 AAB"));
		assertEquals(3, testIron.getSequenceLength());
	}

	@Test
	public final void testFromWordsDuplicateKeys() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Found duplicate diceware word for different sequences: original=AAA duplicate=111");
		CrispyWaffleIron.fromWords(Arrays.asList("111 AAA", "111 AAB"));
	}

	@Test
	public final void testFromWordsDuplicateValues() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Found duplicate diceware word: duplicate=AAA");
		CrispyWaffleIron.fromWords(Arrays.asList("111 AAA", "112 AAA"));
	}

	@Test
	public final void testCookBatchMinimumKeyLengths() throws Exception {
		Set<String> validValues = new HashSet<>(Arrays.asList("AAA", "AAB", "ABA", "ABB", "BAA", "BAB", "BBA", "BBB"));
		CrispyWaffleIron testIron = CrispyWaffleIron.fromWords(
				Arrays.asList("111 AAA", "112 AAB", "121 ABA", "122 ABB", "211 BAA", "212 BAB", "221 BBA", "222 BBB"));
		assertEquals(3, testIron.getSequenceLength());
		Set<String> results = new HashSet<>();
		for (int i = 0; i < 1000000; i++) {
			String nextBatch = testIron.cookBatch();
			assertNotNull(nextBatch);
			assertTrue(validValues.contains(nextBatch));
			results.add(nextBatch);
		}
		// If we are returning constant values for that many iterations it is a
		// bug, but may not be returning all of them even over that range
		assertTrue(results.size() > 1);
	}

}
