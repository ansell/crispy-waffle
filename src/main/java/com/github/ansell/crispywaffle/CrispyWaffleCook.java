/*
 * Copyright (c) 2016, Peter Ansell
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.ansell.crispywaffle;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Summarises CSV files to easily debug and identify likely parse issues before
 * pushing them through a more heavy tool or process.
 * 
 * @author Peter Ansell p_ansell@yahoo.com
 */
public final class CrispyWaffleCook {

	/**
	 * The minimum number of words to include in passphrase.
	 */
	public static final int MINIMUM_WORD_COUNT = 4;

	/**
	 * The default number of words to include in passphrase.
	 */
	public static final int DEFAULT_WORD_COUNT = 6;

	/**
	 * Private constructor for static only class
	 */
	private CrispyWaffleCook() {
	}

	public static void main(String... args) throws Exception {
		final OptionParser parser = new OptionParser();

		final OptionSpec<Void> help = parser.accepts("help").forHelp();
		final OptionSpec<File> wordListOption = parser.accepts("wordlist").withRequiredArg().ofType(File.class)
				.required().describedAs("The words list to be used.");
		final OptionSpec<Integer> wordCountOption = parser.accepts("count").withRequiredArg().ofType(Integer.class)
				.defaultsTo(DEFAULT_WORD_COUNT).describedAs("The number of words to include in the passphrase.");

		OptionSet options = null;

		try {
			options = parser.parse(args);
		} catch (final OptionException e) {
			System.out.println(e.getMessage());
			parser.printHelpOn(System.out);
			throw e;
		}

		if (options.has(help)) {
			parser.printHelpOn(System.out);
			return;
		}

		int wordCount = wordCountOption.value(options);
		if (wordCount < MINIMUM_WORD_COUNT) {
			throw new IllegalArgumentException(
					"This program cannot be used to generate passphrases with less than 4 words in them");
		}

		final Path wordListPath = wordListOption.value(options).toPath();
		if (!Files.exists(wordListPath)) {
			throw new FileNotFoundException("Could not find word list file: " + wordListPath.toString());
		}

		List<String> allWords = Files.readAllLines(wordListPath, StandardCharsets.UTF_8);
		CrispyWaffleIron crispyWaffleDB = CrispyWaffleIron.fromWords(allWords);
		for (int i = 0; i < wordCount; i++) {
			System.out.print(crispyWaffleDB.cookBatch());
			System.out.print(' ');
		}
		System.out.println("");
	}

}
