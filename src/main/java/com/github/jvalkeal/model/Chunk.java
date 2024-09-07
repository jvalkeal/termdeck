/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jvalkeal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jline.utils.AttributedStyle;

import org.springframework.lang.NonNull;
import org.springframework.shell.style.ThemeResolver;
import org.springframework.shell.style.ThemeResolver.ResolvedValues;
import org.springframework.shell.treesitter.TreeSitterLanguages;
import org.springframework.util.Assert;

/**
 * Block is a representation part of a slide.
 *
 * @author Janne Valkealahti
 */
public abstract class Chunk {

	// private final List<String> content;
	// private ThemeResolver themeResolver;

	// public Block(@NonNull List<String> content) {
	// 	Assert.notNull(content, "content cannot be null");
	// 	this.content = new ArrayList<>(content);
	// }

	public record ResolveContentContext(ThemeResolver themeResolver, MarkdownSettings markdownSettings, TreeSitterLanguages treeSitterLanguages) {
	}

	public abstract List<String> resolveContent(ResolveContentContext context);
	// public abstract List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings);

	// public List<String> content() {
	// 	return content;
	// }

	// public ThemeResolver getThemeResolver() {
	// 	return themeResolver;
	// }


	// private Optional<ResolvedValues> getThemeResolvedValues(String tag) {
	// 	ThemeResolver themeResolver = getThemeResolver();
	// 	if (themeResolver != null) {
	// 		String styleTag = themeResolver.resolveStyleTag(tag, getThemeName());
	// 		AttributedStyle attributedStyle = themeResolver.resolveStyle(styleTag);
	// 		return Optional.of(themeResolver.resolveValues(attributedStyle));
	// 	}
	// 	return Optional.empty();
	// }


	// public static Block of(List<String> content) {
	// 	return new Block(content);
	// }

}
