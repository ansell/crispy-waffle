/**
 * 
 */
package com.github.ansell.crispywaffle;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Crispy Waffle DB
 * 
 * @author Peter Ansell p_ansell@yahoo.com
 */
public class CrispyWaffleDB {
	private static final int MINIMUM_SEQUENCE_LENGTH = 3;

	private static final SecureRandom PRNG = new SecureRandom();

	private final Map<String, String> dicewareMap;

	private final List<Character> dicewareCharacters;

	private final int dicewareLength;

	private CrispyWaffleDB(Map<String, String> dicewareMap, int dicewareLength) {
		if (dicewareLength < MINIMUM_SEQUENCE_LENGTH) {
			throw new IllegalArgumentException(
					"Cannot generate passphrases from this list due to it containing shorter sequences than necessary: found: "
							+ dicewareLength + " required: " + MINIMUM_SEQUENCE_LENGTH);
		}
		this.dicewareLength = dicewareLength;
		this.dicewareMap = new ConcurrentHashMap<>(dicewareMap);
		CharsetEncoder asciiChecker = StandardCharsets.US_ASCII.newEncoder();
		Set<Character> nextDicewareCharacters = new HashSet<>();
		for (String nextSequence : this.dicewareMap.keySet()) {
			nextSequence.codePoints().filter(nextCodePoint -> {
				if (!Character.isDigit(nextCodePoint)) {
					throw new IllegalArgumentException("Found a non-digit sequence character in: " + nextSequence);
				}
				return true;
			});
			if (!asciiChecker.canEncode(nextSequence)) {
				throw new IllegalArgumentException(
						"Found a sequence that did not solely use US-ASCII digits: " + nextSequence);
			}
			for (int i = 0; i < nextSequence.length(); i++) {
				nextDicewareCharacters.add(nextSequence.charAt(i));
			}
		}
		this.dicewareCharacters = new ArrayList<>(nextDicewareCharacters);
	}

	public final int getSequenceLength() {
		return dicewareLength;
	}

	public static CrispyWaffleDB fromWords(Iterable<String> allWords) {
		int count = 0;
		Map<String, String> dicewareMap = new ConcurrentHashMap<>();
		Integer dicewareLength = null;
		for (String nextWord : allWords) {
			count++;
			String tempWord = nextWord.trim();
			String[] splitWord = tempWord.split(" ", 2);
			if (splitWord.length < 2) {
				splitWord = tempWord.split("\t", 2);
				if (splitWord.length < 2) {
					throw new IllegalArgumentException("Found an unrecognised line in the word list on line " + count
							+ ": " + nextWord + " splitlength=" + splitWord.length);
				}
			}
			String dicewareSequence = splitWord[0].trim();
			int nextLength = dicewareSequence.length();
			if (nextLength < MINIMUM_SEQUENCE_LENGTH) {
				throw new IllegalArgumentException(
						"Cannot generate passphrases from this list due to it containing shorter sequences than necessary");
			}
			if (dicewareLength == null) {
				dicewareLength = dicewareSequence.length();
			}
			String parsedSequence = Integer.toString(Integer.parseInt(dicewareSequence));
			if (!parsedSequence.equals(dicewareSequence)) {
				throw new IllegalArgumentException(
						"Did not recognise some characters in the diceware sequence: " + dicewareSequence);
			}
			String dicewareWord = splitWord[1].trim();
			if (dicewareWord.contains(" ")) {
				throw new IllegalArgumentException("Diceware word contained a space: " + dicewareWord);
			}
			String putIfAbsent = dicewareMap.putIfAbsent(parsedSequence, dicewareWord);
			if (putIfAbsent != null) {
				throw new IllegalArgumentException("Found duplicate diceware word for different sequences: original="
						+ putIfAbsent + " duplicate=" + dicewareSequence);
			}
		}

		if (dicewareLength == null) {
			throw new IllegalArgumentException("Word list was empty");
		}

		return new CrispyWaffleDB(dicewareMap, dicewareLength);
	}

	public final String throwDice() {
		char[] result = new char[getSequenceLength()];
		for (int i = 0; i < result.length; i++) {
			result[i] = this.dicewareCharacters.get(PRNG.nextInt(dicewareCharacters.size()));
		}
		return dicewareMap.get(new String(result));
	}

}
