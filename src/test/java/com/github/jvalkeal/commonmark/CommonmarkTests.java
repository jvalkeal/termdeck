package com.github.jvalkeal.commonmark;

import com.github.jvalkeal.model.Deck;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonmarkTests {

	@Test
	void singlePage() {
		String markdown = """
			# slide1
			hello1
			""";
		Deck deck = parse(markdown);

		assertThat(deck.getSlides()).hasSize(1).satisfiesExactly(slide -> {
			assertThat(slide.blocks()).hasSize(2).satisfiesExactly(
				block -> {
					assertThat(block.content()).hasSize(1).satisfiesExactly(content -> {
						assertThat(content).contains("slide1");
					});
				},
				block -> {
					assertThat(block.content()).hasSize(1).satisfiesExactly(content -> {
						assertThat(content).contains("hello1");
					});
				});
		});

	}

	@Test
	void basicMultiPage() {
		String markdown = """
			# slide1
			hello1

			---
			# slide2
			hello2
			""";
		Deck deck = parse(markdown);

		assertThat(deck.getSlides()).hasSize(2).satisfiesExactly(
			slide -> {
				assertThat(slide.blocks()).hasSize(2).satisfiesExactly(
					block -> {
						assertThat(block.content()).hasSize(1).satisfiesExactly(content -> {
							assertThat(content).contains("slide1");
						});
					},
					block -> {
						assertThat(block.content()).hasSize(1).satisfiesExactly(content -> {
							assertThat(content).contains("hello1");
						});
					});
			},
			slide -> {
				assertThat(slide.blocks()).hasSize(2).satisfiesExactly(
					block -> {
						assertThat(block.content()).hasSize(1).satisfiesExactly(content -> {
							assertThat(content).contains("slide2");
						});
					},
					block -> {
						assertThat(block.content()).hasSize(1).satisfiesExactly(content -> {
							assertThat(content).contains("hello2");
						});
					});
			}
		);

	}

	private Deck parse(String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		MarkdownVisitor visitor = new MarkdownVisitor();
		document.accept(visitor);
		return visitor.getDeck();
	}
}
