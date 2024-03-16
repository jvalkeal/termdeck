package org.springframework.shell.textmate;

import java.util.Arrays;

import org.eclipse.tm4e.core.internal.theme.ParsedThemeRule;

public class Theme {

	public static Theme createFromRawTheme(IRawTheme source) {
		return createFromParsedTheme(parseTheme(source));
	}

	public static Theme createFromParsedTheme(ParsedThemeRule[] source) {
		return resolveParsedThemeRules(source);
	}

	private static ParsedThemeRule[] parseTheme(IRawTheme source) {
		if (source == null) {
			return new ParsedThemeRule[0];
		}
		return null;
	}

	private static Theme resolveParsedThemeRules(ParsedThemeRule[] parsedThemeRules) {
		return new Theme();
	}
}
