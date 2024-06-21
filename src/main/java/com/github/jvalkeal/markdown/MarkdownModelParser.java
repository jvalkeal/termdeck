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
package com.github.jvalkeal.markdown;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.ModelParser;
import com.github.jvalkeal.model.Slide;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

/**
 * {@link ModelParser} parsing {@code markdown}.
 *
 * @author Janne Valkealahti
 */
public class MarkdownModelParser implements ModelParser {

	@Override
	public Deck parse(String content) {
		YamlFrontMatterExtension ye = YamlFrontMatterExtension.create();
		DataHolder options = new MutableDataSet();
		Parser parser = Parser.builder(options).extensions(Collections.singleton(ye)).build();
		TermdeckRenderer renderer = TermdeckRenderer.builder(options).build();
		Node document = parser.parse(content);
		List<List<String>> deckContent = renderer.render(document);
		// Deck deck = deckContent.stream()
		// 	.map(c -> Slide.of(c))
		// 	.collect(Collectors.collectingAndThen(Collectors.toList(), l -> new Deck(l)));
		// return deck;

		return null;
	}

}
