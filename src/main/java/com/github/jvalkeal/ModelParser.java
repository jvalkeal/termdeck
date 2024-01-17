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

import java.util.Collections;
import java.util.function.BiConsumer;

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

public class ModelParser {


	NodeVisitor visitor = new NodeVisitor(
		// new VisitHandler<>(Node.class, this::visit)
	) {
		@Override
		protected void processNode(Node node, boolean withChildren, java.util.function.BiConsumer<Node,com.vladsch.flexmark.util.ast.Visitor<Node>> processor) {
			System.out.println("Visiting node:" + node);
			super.processNode(node, withChildren, processor);
		};
	};

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

	private static class ModelNodeVisitor extends NodeVisitor {

		private Deck deck;
		private Slide slide;
		private StringBuilder content;

		@Override
		protected void processNode(@NotNull Node node, boolean withChildren,
				@NotNull BiConsumer<Node, Visitor<Node>> processor) {

			// start node
			if (node instanceof Document) {
				this.deck = new Deck();
				this.content = new StringBuilder();
				this.slide = new Slide();
			}
			else if (node instanceof ThematicBreak) {
			}

			super.processNode(node, withChildren, processor);

			// end node
			if (node instanceof ThematicBreak) {
				if (slide != null) {
					slide.setContent(content.toString());
					deck.addSlide(slide);
					this.slide = null;
					this.slide = new Slide();
				}
				this.content = new StringBuilder();
			}
			else if (node instanceof Text n) {
				BasedSequence chars = n.getChars();
				content.append(chars);
			}
			else if (node instanceof Document) {
				if (slide != null) {
					slide.setContent(content.toString());
					deck.addSlide(slide);
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
