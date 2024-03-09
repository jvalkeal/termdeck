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
package org.eclipse.tm4e.core.internal.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Based on <a href="https://github.com/microsoft/vscode-textmate/blob/09effd8b7429b71010e0fa34ea2e16e622692946/src/theme.ts#L430">
 * github.com/microsoft/vscode-textmate/blob/main/src/theme.ts#ThemeTrieElementRule</a>.
 * <p>
 * See also <a href=
 * "https://github.com/microsoft/vscode/blob/ba2cf46e20df3edf77bdd905acde3e175d985f70/src/vs/editor/common/languages/supports/tokenization.ts#L276">
 * github.com/microsoft/vscode/blob/main/src/vs/editor/common/languages/supports/tokenization.ts#ThemeTrieElementRule</a>
 */
public class ThemeTrieElementRule {

	public int scopeDepth;
	public final @Nullable List<String> parentScopes;
	public int fontStyle;
	public int foreground;
	public int background;

	public ThemeTrieElementRule(final int scopeDepth, final @Nullable List<String> parentScopes, final int fontStyle, final int foreground,
			final int background) {
		this.scopeDepth = scopeDepth;
		this.parentScopes = parentScopes;
		this.fontStyle = fontStyle;
		this.foreground = foreground;
		this.background = background;
	}

	@Override
	public ThemeTrieElementRule clone() {
		return new ThemeTrieElementRule(this.scopeDepth, this.parentScopes, this.fontStyle, this.foreground, this.background);
	}

	public static List<ThemeTrieElementRule> cloneArr(final List<ThemeTrieElementRule> arr) {
		final var r = new ArrayList<ThemeTrieElementRule>(arr.size());
		for (final var e : arr) {
			r.add(e.clone());
		}
		return r;
	}

	public void acceptOverwrite(final int scopeDepth, final int fontStyle, final int foreground, final int background) {
		if (this.scopeDepth > scopeDepth) {
			// TODO!!!
			// console.log('how did this happen?');
		} else {
			this.scopeDepth = scopeDepth;
		}
		// console.log('TODO -> my depth: ' + this.scopeDepth + ', overwriting depth: ' + scopeDepth);
		if (fontStyle != FontStyle.NotSet) {
			this.fontStyle = fontStyle;
		}
		if (foreground != 0) {
			this.foreground = foreground;
		}
		if (background != 0) {
			this.background = background;
		}
	}

	@Override
	public int hashCode() {
		int result = 31 + background;
		result = 31 * result + fontStyle;
		result = 31 * result + foreground;
		result = 31 * result + Objects.hashCode(parentScopes);
		return 31 * result + scopeDepth;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof final ThemeTrieElementRule other)
			return scopeDepth == other.scopeDepth
					&& background == other.background
					&& fontStyle == other.fontStyle
					&& foreground == other.foreground
					&& Objects.equals(parentScopes, other.parentScopes);
		return false;
	}

	@Override
	public String toString() {
		return "ThemeTrieElementRule [fontStyle=" + fontStyle + ", foreground=" + foreground + ", background=" + background
				+ ", scopeDepth=" + scopeDepth + ", parentScopes=" + parentScopes + "]";
	}
}
