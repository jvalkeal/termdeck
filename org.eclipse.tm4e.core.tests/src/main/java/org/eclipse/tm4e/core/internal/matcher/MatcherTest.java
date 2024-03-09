/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.internal.matcher;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * VSCode TextMate matcher tests
 *
 * @see <a href="https://github.com/microsoft/vscode-textmate/blob/09effd8b7429b71010e0fa34ea2e16e622692946/src/tests/matcher.test.ts">
 *      github.com/microsoft/vscode-textmate/blob/main/src/tests/matcher.test.ts</a>
 */
public class MatcherTest {

	static final class MatcherTestImpl implements Executable {

		String expression;
		List<String> input;
		boolean result;

		@Override
		public void execute() {
			final var matcher = Matcher.createMatchers(expression);
			final boolean result = matcher.stream().anyMatch(m -> m.matcher.matches(input));
			assertEquals(result, this.result);
		}
	}

	@TestFactory
	@DisplayName("Matcher tests")
	List<DynamicTest> matcherTests() throws Exception {
		final var jsonTests = """
			[
				{ "expression": "foo", "input": ["foo"], "result": true },
				{ "expression": "foo", "input": ["bar"], "result": false },
				{ "expression": "- foo", "input": ["foo"], "result": false },
				{ "expression": "- foo", "input": ["bar"], "result": true },
				{ "expression": "- - foo", "input": ["bar"], "result": false },
				{ "expression": "bar foo", "input": ["foo"], "result": false },
				{ "expression": "bar foo", "input": ["bar"], "result": false },
				{ "expression": "bar foo", "input": ["bar", "foo"], "result": true },
				{ "expression": "bar - foo", "input": ["bar"], "result": true },
				{ "expression": "bar - foo", "input": ["foo", "bar"], "result": false },
				{ "expression": "bar - foo", "input": ["foo"], "result": false },
				{ "expression": "bar, foo", "input": ["foo"], "result": true },
				{ "expression": "bar, foo", "input": ["bar"], "result": true },
				{ "expression": "bar, foo", "input": ["bar", "foo"], "result": true },
				{ "expression": "bar, -foo", "input": ["bar", "foo"], "result": true },
				{ "expression": "bar, -foo", "input": ["yo"], "result": true },
				{ "expression": "bar, -foo", "input": ["foo"], "result": false },
				{ "expression": "(foo)", "input": ["foo"], "result": true },
				{ "expression": "(foo - bar)", "input": ["foo"], "result": true },
				{ "expression": "(foo - bar)", "input": ["foo", "bar"], "result": false },
				{ "expression": "foo bar - (yo man)", "input": ["foo", "bar"], "result": true },
				{ "expression": "foo bar - (yo man)", "input": ["foo", "bar", "yo"], "result": true },
				{ "expression": "foo bar - (yo man)", "input": ["foo", "bar", "yo", "man"], "result": false },
				{ "expression": "foo bar - (yo | man)", "input": ["foo", "bar", "yo", "man"], "result": false },
				{ "expression": "foo bar - (yo | man)", "input": ["foo", "bar", "yo"], "result": false }
			]
			""";
		final var listType = new TypeToken<List<MatcherTestImpl>>() {
		}.getType();
		final List<MatcherTestImpl> tests = new GsonBuilder().create().fromJson(jsonTests, listType);
		final var dynamicTests = new ArrayList<DynamicTest>();
		for (int i = 0; i < tests.size(); i++) {
			dynamicTests.add(DynamicTest.dynamicTest("Test #" + (i + 1), tests.get(i)));
		}
		return dynamicTests;
	}
}
