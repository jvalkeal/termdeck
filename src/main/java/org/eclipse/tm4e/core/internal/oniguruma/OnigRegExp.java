/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/atom/node-oniguruma
 * Initial copyright Copyright (c) 2013 GitHub Inc.
 * Initial license: MIT
 *
 * Contributors:
 * - GitHub Inc.: Initial code, written in JavaScript, licensed under MIT license
 * - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 * - Fabio Zadrozny <fabiofz@gmail.com> - Convert uniqueId to Object (for identity compare)
 * - Fabio Zadrozny <fabiofz@gmail.com> - Fix recursion error on creation of OnigRegExp with unicode chars
 */
package org.eclipse.tm4e.core.internal.oniguruma;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.StandardCharsets;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tm4e.core.TMException;
import org.eclipse.tm4e.core.internal.utils.StringUtils;
import org.jcodings.specific.UTF8Encoding;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;
import org.joni.Syntax;
import org.joni.WarnCallback;
import org.joni.exception.SyntaxException;

/**
 * @see <a href="https://github.com/atom/node-oniguruma/blob/master/src/onig-reg-exp.cc">
 *      github.com/atom/node-oniguruma/blob/master/src/onig-reg-exp.cc</a>
 */
public final class OnigRegExp {
	private static final Logger LOGGER = System.getLogger(OnigRegExp.class.getName());

	/**
	 * {@link WarnCallback} which is used if log level is at least Level.WARNING.
	 */
	private static final WarnCallback LOGGER_WARN_CALLBACK = message -> LOGGER.log(Level.WARNING, message);

	@Nullable
	private OnigString lastSearchString;

	private int lastSearchPosition = -1;

	@Nullable
	private OnigResult lastSearchResult;

	private final String pattern;
	private final Regex regex;

	private final boolean hasGAnchor;

	/**
	 * @throws TMException if parsing fails
	 */
	public OnigRegExp(final String pattern) {
		this(pattern, false);
	}

	/**
	 * @throws TMException if parsing fails
	 */
	public OnigRegExp(final String pattern, final boolean ignoreCase) {
		this.pattern = rewritePatternIfRequired(pattern);
		hasGAnchor = this.pattern.contains("\\G");
		final byte[] patternBytes = this.pattern.getBytes(StandardCharsets.UTF_8);
		try {
			int options = Option.CAPTURE_GROUP;
			if (ignoreCase)
				options |= Option.IGNORECASE;
			regex = new Regex(patternBytes, 0, patternBytes.length, options, UTF8Encoding.INSTANCE, Syntax.DEFAULT,
					LOGGER.isLoggable(Level.WARNING) ? LOGGER_WARN_CALLBACK : WarnCallback.NONE);
		} catch (final SyntaxException ex) {
			throw new TMException("Parsing regex pattern \"" + this.pattern + "\" failed with " + ex, ex);
		}
	}

	/**
	 * Rewrites the given pattern to workaround limitations of the joni library which for example does not support
	 * negative variable-length lookbehinds
	 *
	 * @see <a href="https://github.com/eclipse/tm4e/issues/677">github.com/eclipse/tm4e/issue/677</a>
	 */
	private String rewritePatternIfRequired(final String pattern) {

		// e.g. used in csharp.tmLanguage.json
		final var lookbehind1 = "(?<!\\.\\s*)";
		if (pattern.startsWith(lookbehind1)) {
			return "(?<!\\.)\\s*" + pattern.substring(lookbehind1.length());
		}

		// e.g. used in markdown.math.inline.tmLanguage.json
		final var lookbehind2 = "(?<=^\\s*)";
		if (pattern.startsWith(lookbehind2)) {
			return "(?<=^)\\s*" + pattern.substring(lookbehind2.length());
		}
		return pattern;
	}

	/**
	 * @return null if not found
	 */
	public @Nullable OnigResult search(final OnigString str, final int startPosition) {
		if (hasGAnchor) {
			// Should not use caching, because the regular expression
			// targets the current search position (\G)
			return search(str.bytesUTF8, startPosition, str.bytesCount);
		}

		final var lastSearchResult0 = this.lastSearchResult;
		if (lastSearchString == str
				&& lastSearchPosition <= startPosition
				&& (lastSearchResult0 == null || lastSearchResult0.locationAt(0) >= startPosition)) {
			return lastSearchResult0;
		}

		lastSearchString = str;
		lastSearchPosition = startPosition;
		lastSearchResult = search(str.bytesUTF8, startPosition, str.bytesCount);
		return lastSearchResult;
	}

	@Nullable
	private OnigResult search(final byte[] data, final int startPosition, final int end) {
		final Matcher matcher = regex.matcher(data);
		final int status = matcher.search(startPosition, end, Option.DEFAULT);
		if (status != Matcher.FAILED) {
			final Region region = matcher.getEagerRegion();
			return new OnigResult(region, -1);
		}
		return null;
	}

	public String pattern() {
		return pattern;
	}

	@Override
	public String toString() {
		return StringUtils.toString(this, sb -> {
			sb.append("pattern=").append(pattern);
		});
	}
}
