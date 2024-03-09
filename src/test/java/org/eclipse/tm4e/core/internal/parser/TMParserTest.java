/**
 * Copyright (c) 2023 Vegard IT GmbH and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Sebastian Thomschke (Vegard IT) - initial implementation
 */
package org.eclipse.tm4e.core.internal.parser;

import static org.eclipse.tm4e.core.internal.utils.NullSafetyHelper.castNonNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.tm4e.core.Data;
import org.eclipse.tm4e.core.internal.grammar.raw.RawGrammar;
import org.eclipse.tm4e.core.internal.grammar.raw.RawGrammarReader;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.MethodName.class)
class TMParserTest {

	@SuppressWarnings("null")
	private void validateCaptures(final RawGrammar grammar) {
		assertNotNull(grammar.getPatterns());
		assertEquals(1, grammar.getPatterns().size());
		final var pattern = grammar.getPatterns().iterator().next();
		assertEquals("THE_PATTERN", pattern.getName());
		assertEquals("BEGIN_PATTERN", pattern.getBegin());
		assertEquals("END_PATTERN", pattern.getEnd());
		final var capures = pattern.getCaptures();
		assertEquals("THE_CAPTURE", capures.getCapture("0").getName());
	}

	@Test
	void testParseCapturesJSON() throws Exception {
		// test capture defined as JSON map
		validateCaptures(TMParserJSON.INSTANCE.parse(new StringReader("""
			{"patterns": [{
				"name": "THE_PATTERN",
				"captures": {
					"0": { "name": "THE_CAPTURE" }
				},
				"begin": "BEGIN_PATTERN",
				"end": "END_PATTERN"
			}]}"""), RawGrammarReader.OBJECT_FACTORY));

		// test capture defined as JSON array
		validateCaptures(TMParserJSON.INSTANCE.parse(new StringReader("""
			{"patterns": [{
				"name": "THE_PATTERN",
				"captures": [
					{ "name": "THE_CAPTURE" }
				],
				"begin": "BEGIN_PATTERN",
				"end": "END_PATTERN"
			}]}"""), RawGrammarReader.OBJECT_FACTORY));
	}

	@Test
	void testParseCapturesPList() throws Exception {
		// test capture defined as PList dict
		validateCaptures(TMParserPList.INSTANCE.parse(new StringReader("""
			<plist version="1.0">
			<dict>
				<key>patterns</key>
				<array>
					<dict>
						<key>name</key>
						<string>THE_PATTERN</string>
						<key>captures</key>
						<dict>
							<key>0</key>
							<dict>
								<key>name</key>
								<string>THE_CAPTURE</string>
							</dict>
						</dict>
						<key>begin</key>
						<string>BEGIN_PATTERN</string>
						<key>end</key>
						<string>END_PATTERN</string>
					</dict>
				</array>
			</dict>
			</plist>"""), RawGrammarReader.OBJECT_FACTORY));

		// test capture defined as PList array
		validateCaptures(TMParserPList.INSTANCE.parse(new StringReader("""
			<plist version="1.0">
			<dict>
				<key>patterns</key>
				<array>
					<dict>
						<key>name</key>
						<string>THE_PATTERN</string>
						<key>captures</key>
						<array>
							<dict>
								<key>name</key>
								<string>THE_CAPTURE</string>
							</dict>
						</array>
						<key>begin</key>
						<string>BEGIN_PATTERN</string>
						<key>end</key>
						<string>END_PATTERN</string>
					</dict>
				</array>
			</dict>
			</plist>"""), RawGrammarReader.OBJECT_FACTORY));
	}

	@Test
	void testParseCapturesYAML() throws Exception {
		// test capture defined as YAML map
		validateCaptures(TMParserYAML.INSTANCE.parse(new StringReader("""
			---
			patterns:
			- name: THE_PATTERN
			  captures:
			    0:
			      name: THE_CAPTURE
			  begin: "BEGIN_PATTERN"
			  end: "END_PATTERN"
			"""), RawGrammarReader.OBJECT_FACTORY));

		// test capture defined as YAML list
		validateCaptures(TMParserYAML.INSTANCE.parse(new StringReader("""
			---
			patterns:
			- name: THE_PATTERN
			  captures:
			    - name: THE_CAPTURE
			  begin: "BEGIN_PATTERN"
			  end: "END_PATTERN"
			"""), RawGrammarReader.OBJECT_FACTORY));
	}

	@Test
	void testParseJSON() throws Exception {
		try (var is = Data.class.getResourceAsStream("csharp.json")) {
			final var grammar = TMParserJSON.INSTANCE.parse(new InputStreamReader(is), RawGrammarReader.OBJECT_FACTORY);
			assertNotNull(grammar.getRepository());
			assertFalse(grammar.getFileTypes().isEmpty());
			assertEquals(List.of("cs"), grammar.getFileTypes());
			assertEquals("C#", grammar.getName());
			assertEquals("source.cs", grammar.getScopeName());
			assertEquals(List.of("cs"), grammar.getFileTypes());
			assertEquals(Set.of("fileTypes", "foldingStartMarker", "foldingStopMarker", "name", "patterns", "repository", "scopeName"),
					grammar.keySet());
		}
	}

	@Test
	void testParsePlist() throws Exception {
		try (var is = Data.class.getResourceAsStream("JavaScript.tmLanguage")) {
			final var grammar = TMParserPList.INSTANCE.parse(new InputStreamReader(is), RawGrammarReader.OBJECT_FACTORY);
			assertNotNull(grammar);
			assertNotNull(grammar.getRepository());
			assertFalse(grammar.getFileTypes().isEmpty());
			assertEquals(List.of("js", "jsx"), grammar.getFileTypes());
			assertEquals("JavaScript (with React support)", grammar.getName());
			assertEquals("source.js", grammar.getScopeName());
			assertEquals(Set.of("fileTypes", "name", "patterns", "repository", "scopeName", "uuid"), grammar.keySet());
		}
	}

	@Test
	void testParseYAML() throws Exception {
		try (var is = Data.class.getResourceAsStream("JavaScript.tmLanguage.yaml")) {
			final var grammar = TMParserYAML.INSTANCE.parse(new InputStreamReader(is), RawGrammarReader.OBJECT_FACTORY);
			assertNotNull(grammar.getRepository());
			assertFalse(grammar.getFileTypes().isEmpty());
			assertEquals(List.of("js", "jsx"), grammar.getFileTypes());
			assertEquals("JavaScript (with React support)", grammar.getName());
			assertEquals("source.js", grammar.getScopeName());
			assertEquals(Set.of("fileTypes", "name", "patterns", "repository", "scopeName", "uuid"), grammar.keySet());
		}
	}

	@Test
	@NonNullByDefault({})
	void testLanguagePackGrammars() throws IOException {
		final var count = new AtomicInteger();
		Files.walkFileTree(Paths.get("org.eclipse.tm4e.language_pack"), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().endsWith("tmLanguage.json")) {
					try (var input = Files.newBufferedReader(file)) {
						System.out.println("Parsing [" + file + "]...");
						final var grammar = TMParserJSON.INSTANCE.parse(input, RawGrammarReader.OBJECT_FACTORY);
						count.incrementAndGet();
						assertFalse(grammar.getScopeName().isBlank());
						assertFalse(castNonNull(grammar.getPatterns()).isEmpty());
						assertNotNull(grammar.getFileTypes());
						assertNotNull(grammar.getRepository());
					} catch (final Exception ex) {
						throw new RuntimeException(ex);
					}
				}
				return FileVisitResult.CONTINUE;
			}
		});
		System.out.println("Successfully parsed " + count.intValue() + " grammars.");
		assertTrue(count.intValue() > 10, "Only " + count.intValue() + " grammars found, expected more than 10!");
	}
}
