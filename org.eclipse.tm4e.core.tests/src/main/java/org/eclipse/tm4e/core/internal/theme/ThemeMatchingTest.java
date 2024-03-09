/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 * - Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.internal.theme;

import java.util.Map;

import org.eclipse.tm4e.core.internal.grammar.ScopeStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @see <a href=
 *      "https://github.com/microsoft/vscode-textmate/blob/09effd8b7429b71010e0fa34ea2e16e622692946/src/tests/themes.test.ts#L126">
 *      github.com/microsoft/vscode-textmate/blob/main/src/tests/themes.test.ts</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThemeMatchingTest extends AbstractThemeTest {

	@Test
	@Order(1)
	@DisplayName("Theme matching gives higher priority to deeper matches")
	void testGivesHigherPriorityToDeeperMatches() throws Exception {
		final Theme theme = createTheme("""
			{"settings": [
				{ "settings": { "foreground": "#100000", "background": "#200000" } },
				{ "scope": "punctuation.definition.string.begin.html", "settings": { "foreground": "#300000" } },
				{ "scope": "meta.tag punctuation.definition.string", "settings": { "foreground": "#400000" } }
			]}""");

		final var actual = theme.match(ScopeStack.from("punctuation.definition.string.begin.html"));
		assertEquals(theme.getColorMap().get(actual.foregroundId), "#300000");
	}

	@Test
	@Order(2)
	@DisplayName("Theme matching gives higher priority to parent matches 1")
	void testGivesHigherPriorityToParentMatches1() throws Exception {
		final Theme theme = createTheme("""
			{"settings": [
				{ "settings": { "foreground": "#100000", "background": "#200000" } },
				{ "scope": "c a", "settings": { "foreground": "#300000" } },
				{ "scope": "d a.b", "settings": { "foreground": "#400000" } },
				{ "scope": "a", "settings": { "foreground": "#500000" } }
			]}""");

		final var map = theme.getColorMap();

		assertEquals(map.get(theme.match(ScopeStack.from("d", "a.b")).foregroundId), "#400000");
	}

	@Test
	@Order(3)
	@DisplayName("Theme matching gives higher priority to parent matches 2")
	void testGivesHigherPriorityToParentMatches2() throws Exception {
		final Theme theme = createTheme("""
			{"settings": [
				{ "settings": { "foreground": "#100000", "background": "#200000" } },
				{ "scope": "meta.tag entity", "settings": { "foreground": "#300000" } },
				{ "scope": "meta.selector.css entity.name.tag", "settings": { "foreground": "#400000" } },
				{ "scope": "entity", "settings": { "foreground": "#500000" } }
			]}""");

		final var result = theme.match(
				ScopeStack.from(
						"text.html.cshtml",
						"meta.tag.structure.any.html",
						"entity.name.tag.structure.any.html"));

		final var colorMap = theme.getColorMap();
		assertEquals(colorMap.get(result.foregroundId), "#300000");
	}

	private Map<String, String> match(final Theme theme, final String... path) {
		final var map = theme.getColorMap();
		final var result = theme.match(ScopeStack.from(path));
		if (result == null) {
			return null;
		}
		final var obj = map("fontStyle", FontStyle.fontStyleToString(result.fontStyle));
		if (result.foregroundId != 0) {
			obj.put("foreground", map.get(result.foregroundId));
		}
		if (result.backgroundId != 0) {
			obj.put("background", map.get(result.backgroundId));
		}
		return obj;
	}

	@Test
	@Order(4)
	@DisplayName("Theme matching can match")
	void testCanMatch() throws Exception {
		final Theme theme = createTheme("""
			{"settings": [
				{ "settings": { "foreground": "#F8F8F2", "background": "#272822" } },
				{ "scope": "source, something", "settings": { "background": "#100000" } },
				{ "scope": ["bar", "baz"], "settings": { "background": "#200000" } },
				{ "scope": "source.css selector bar", "settings": { "fontStyle": "bold" } },
				{ "scope": "constant", "settings": { "fontStyle": "italic", "foreground": "#300000" } },
				{ "scope": "constant.numeric", "settings": { "foreground": "#400000" } },
				{ "scope": "constant.numeric.hex", "settings": { "fontStyle": "bold" } },
				{ "scope": "constant.numeric.oct", "settings": { "fontStyle": "bold italic underline" } },
				{ "scope": "constant.numeric.dec", "settings": { "fontStyle": "", "foreground": "#500000" } },
				{ "scope": "storage.object.bar", "settings": { "fontStyle": "", "foreground": "#600000" } }
			]}""");

		// simpleMatch1..25
		assertEquals(match(theme, "source"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "source"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "source.ts"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "source.tss"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "something"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "something.ts"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "something.tss"), map("background", "#100000", "fontStyle", "not set"));
		assertEquals(match(theme, "baz"), map("background", "#200000", "fontStyle", "not set"));
		assertEquals(match(theme, "baz.ts"), map("background", "#200000", "fontStyle", "not set"));
		assertEquals(match(theme, "baz.tss"), map("background", "#200000", "fontStyle", "not set"));
		assertEquals(match(theme, "constant"), map("foreground", "#300000", "fontStyle", "italic"));
		assertEquals(match(theme, "constant.string"), map("foreground", "#300000", "fontStyle", "italic"));
		assertEquals(match(theme, "constant.hex"), map("foreground", "#300000", "fontStyle", "italic"));
		assertEquals(match(theme, "constant.numeric"), map("foreground", "#400000", "fontStyle", "italic"));
		assertEquals(match(theme, "constant.numeric.baz"), map("foreground", "#400000", "fontStyle", "italic"));
		assertEquals(match(theme, "constant.numeric.hex"), map("foreground", "#400000", "fontStyle", "bold"));
		assertEquals(match(theme, "constant.numeric.hex.baz"), map("foreground", "#400000", "fontStyle", "bold"));
		assertEquals(match(theme, "constant.numeric.oct"), map("foreground", "#400000", "fontStyle", "italic bold underline"));
		assertEquals(match(theme, "constant.numeric.oct.baz"), map("foreground", "#400000", "fontStyle", "italic bold underline"));
		assertEquals(match(theme, "constant.numeric.dec"), map("foreground", "#500000", "fontStyle", "none"));
		assertEquals(match(theme, "constant.numeric.dec.baz"), map("foreground", "#500000", "fontStyle", "none"));
		assertEquals(match(theme, "storage.object.bar"), map("foreground", "#600000", "fontStyle", "none"));
		assertEquals(match(theme, "storage.object.bar.baz"), map("foreground", "#600000", "fontStyle", "none"));
		assertEquals(match(theme, "storage.object.bart"), map("fontStyle", "not set"));
		assertEquals(match(theme, "storage.object"), map("fontStyle", "not set"));
		assertEquals(match(theme, "storage"), map("fontStyle", "not set"));

		// defaultMatch1..3
		assertEquals(match(theme, ""), map("fontStyle", "not set"));
		assertEquals(match(theme, "bazz"), map("fontStyle", "not set"));
		assertEquals(match(theme, "asdfg"), map("fontStyle", "not set"));

		// multiMatch1..2
		assertEquals(match(theme, "bar"), map("background", "#200000", "fontStyle", "not set"));
		assertEquals(match(theme, "source.css", "selector", "bar"), map("background", "#200000", "fontStyle", "bold"));
	}

	@Test
	@Order(5)
	@DisplayName("Theme matching Microsoft/vscode#23460")
	void testMicrosoft_vscode_23460() throws Exception {
		final Theme theme = createTheme("""
			{"settings": [
				{
					"settings": {
						"foreground": "#aec2e0",
						"background": "#14191f"
					}
				}, {
					"name": "JSON String",
					"scope": "meta.structure.dictionary.json string.quoted.double.json",
					"settings": {
						"foreground": "#FF410D"
					}
				}, {
					"scope": "meta.structure.dictionary.json string.quoted.double.json",
					"settings": {
						"foreground": "#ffffff"
					}
				}, {
					"scope": "meta.structure.dictionary.value.json string.quoted.double.json",
					"settings": {
						"foreground": "#FF410D"
					}
				}
			]}""");

		final var path = ScopeStack.from(
				"source.json",
				"meta.structure.dictionary.json",
				"meta.structure.dictionary.value.json",
				"string.quoted.double.json");
		final var result = theme.match(path);
		assertEquals(theme.getColorMap().get(result.foregroundId), "#FF410D");
	}
}
