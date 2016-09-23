/**
 * 
 */
package com.github.ansell.crispywaffle;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import joptsimple.OptionException;

/**
 * 
 * @author Peter Ansell p_ansell@yahoo.com
 */
public class CrispyWaffleCookTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public TemporaryFolder tempDir = new TemporaryFolder();

	private Path testFile;

	@Before
	public void setUp() throws Exception {
		Path testDir = tempDir.newFolder().toPath();
		testFile = testDir.resolve("testwordlist.txt");
		Files.copy(this.getClass().getResourceAsStream("/com/github/ansell/crispywaffle/eff_large_wordlist.txt"),
				testFile);
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainNone() throws Exception {
		thrown.expect(OptionException.class);
		CrispyWaffleCook.main();
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainEmpty() throws Exception {
		thrown.expect(OptionException.class);
		CrispyWaffleCook.main("");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainUnrecognised() throws Exception {
		thrown.expect(OptionException.class);
		CrispyWaffleCook.main("--bogus");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainNoWordlistFile() throws Exception {
		thrown.expect(OptionException.class);
		CrispyWaffleCook.main("--wordlist");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainNonExistentWordlistFile() throws Exception {
		thrown.expect(IOException.class);
		CrispyWaffleCook.main("--wordlist", "/path/to/non/existent/file.txt");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainMissingCount() throws Exception {
		thrown.expect(OptionException.class);
		CrispyWaffleCook.main("--wordlist", testFile.toAbsolutePath().toString(), "--count");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainLowCount1() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		CrispyWaffleCook.main("--wordlist", testFile.toAbsolutePath().toString(), "--count", "1");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainLowCount2() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		CrispyWaffleCook.main("--wordlist", testFile.toAbsolutePath().toString(), "--count", "2");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainLowCount3() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		CrispyWaffleCook.main("--wordlist", testFile.toAbsolutePath().toString(), "--count", "3");
	}

	/**
	 * Test method for
	 * {@link com.github.ansell.crispywaffle.CrispyWaffleCook#main(java.lang.String[])}.
	 */
	@Test
	public final void testMainMinimumCount4() throws Exception {
		CrispyWaffleCook.main("--wordlist", testFile.toAbsolutePath().toString(), "--count", "4");
	}

}
