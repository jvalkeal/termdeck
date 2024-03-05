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
package com.github.jvalkeal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.HeadingSection;
import com.github.jvalkeal.model.ParagraphSection;
import com.github.jvalkeal.model.Slide;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.ast.ThematicBreak;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.Visitor;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlexmarkParser {

	private Logger log = LoggerFactory.getLogger(FlexmarkParser.class);

	public Deck parse(String content) {
		YamlFrontMatterExtension ye = YamlFrontMatterExtension.create();
		Parser parser = Parser.builder()
			.extensions(Collections.singleton(ye))
			.build();

		ModelNodeVisitor visitor = new ModelNodeVisitor();
		Document document = parser.parse(content);
		visitor.visit(document);
		return visitor.getDeck();
	}

	private class ModelNodeVisitor extends NodeVisitor {

		private final Deck deck = new Deck();
		private Slide currentSlide;
		// private StringBuilder content;
		private List<String> content;
		private StringBuilder headingContent;
		private StringBuilder paragraphContent;

		@Override
		protected void processNode(@NotNull Node node, boolean withChildren,
				@NotNull BiConsumer<Node, Visitor<Node>> processor) {

			log.debug("Start visit node {} {}", node.hashCode(), node);
			// System.out.println("node: " + node);
			// start node
			if (node instanceof Document) {
				// this.deck = new Deck();
				// this.content = new StringBuilder();
				this.content = new ArrayList<>();
				this.currentSlide = new Slide();
			}
			else if (node instanceof Text) {
			}
			else if (node instanceof Heading) {
				this.headingContent = new StringBuilder();
			}
			else if (node instanceof Paragraph) {
				this.paragraphContent = new StringBuilder();
			}
			else if (node instanceof ThematicBreak) {
			}

			super.processNode(node, withChildren, processor);

			log.debug("End visit node {} {}", node.hashCode(), node);
			// end node
			if (node instanceof ThematicBreak) {

				if (currentSlide != null) {
					// currentSlide.setContent(content.toString());
					currentSlide.setContent(content.toArray(new String[0]));
					deck.addSlide(currentSlide);
					this.currentSlide = null;
					this.currentSlide = new Slide();
				}
				// this.content = new StringBuilder();
				this.content = new ArrayList<>();
			}
			else if (node instanceof Paragraph) {
				currentSlide.add(ParagraphSection.of(this.paragraphContent.toString()));
				// ParagraphSection xxx = ParagraphSection.of(this.paragraphContent.toString());
				this.content.add(this.paragraphContent.toString());
				this.paragraphContent = null;
			}
			else if (node instanceof Heading) {
				currentSlide.add(HeadingSection.of(this.headingContent.toString()));
				// HeadingSection xxx = HeadingSection.of(this.headingContent.toString());
				this.content.add(this.headingContent.toString());
				this.headingContent = null;
			}
			else if (node instanceof Text n) {
				BasedSequence chars = n.getChars();
				if (this.paragraphContent != null) {
					this.paragraphContent.append(chars);
				}
				if (this.headingContent != null) {
					this.headingContent.append("■ ");
					this.headingContent.append(chars);
				}
			}
			else if (node instanceof Document) {
				if (currentSlide != null) {
					// currentSlide.setContent(content.toString());
					currentSlide.setContent(content.toArray(new String[0]));
					deck.addSlide(currentSlide);
				}
			}
		}

		public Deck getDeck() {
			return deck;
		}
	}

	// Visiting node:Document{}
	// Visiting node:Heading{}
	// Visiting node:Text{text=Slide1}
	// Visiting node:Paragraph{}
	// Visiting node:Text{text=hello from slide 1}
	// Visiting node:ThematicBreak{}
	// Visiting node:Heading{}
	// Visiting node:Text{text=Slide2}
	// Visiting node:Paragraph{}
	// Visiting node:Text{text=hello1 from slide 2}

}
