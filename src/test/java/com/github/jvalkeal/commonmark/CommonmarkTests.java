package com.github.jvalkeal.commonmark;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.MarkdownSettings;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.shell.style.Theme;
import org.springframework.shell.style.ThemeRegistry;
import org.springframework.shell.style.ThemeResolver;
import org.springframework.shell.style.ThemeSettings;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonmarkTests {

	private ThemeResolver themeResolver;
	private MarkdownSettings markdownSettings;

	@BeforeEach
	public void setup() {
		ThemeRegistry themeRegistry = new ThemeRegistry();
		themeRegistry.register(new Theme() {
				@Override
				public String getName() {
						return "default";
				}
				@Override
				public ThemeSettings getSettings() {
						return ThemeSettings.defaults();
				}
		});
		themeResolver = new ThemeResolver(themeRegistry, "default");
		markdownSettings = MarkdownSettings.defaults();
	}


	@Test
	void basicSinglePage() {
		String markdown = """
			# slide1
			hello1
			""";
		Deck deck = parse(markdown);

		assertThat(deck.getSlides()).hasSize(1).satisfiesExactly(
			slide -> {
				assertThat(slide.blocks()).hasSize(2).satisfiesExactly(
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(2).satisfiesExactly(
							content -> {
								assertThat(content).contains("slide1");
							},
							content -> {
								assertThat(content).isEmpty();
							}
						);
					},
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(1).satisfiesExactly(
							content -> {
								assertThat(content).contains("hello1");
							}
						);
					}
				);
			}
		);
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
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(2).satisfiesExactly(
							content -> {
								assertThat(content).contains("slide1");
							},
							content -> {
								assertThat(content).isEmpty();
							}
						);
					},
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(1).satisfiesExactly(
							content -> {
								assertThat(content).contains("hello1");
							}
						);
					});
			},
			slide -> {
				assertThat(slide.blocks()).hasSize(2).satisfiesExactly(
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(2).satisfiesExactly(
							content -> {
								assertThat(content).contains("slide2");
							},
							content -> {
								assertThat(content).isEmpty();
							}
						);
					},
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(1).satisfiesExactly(
							content -> {
								assertThat(content).contains("hello2");
							}
						);
					}
				);
			}
		);

	}

	// @Test
	void test1() {
		String markdown = """
			text1 `grave` text2
			""";
		Deck deck = parse(markdown);

	}

	// @Test
	void test2() {
		String markdown = """
			```
			code
			```
			""";
		Deck deck = parse(markdown);

	}

	private Deck parse(String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		MarkdownVisitor visitor = new MarkdownVisitor();
		document.accept(visitor);
		return visitor.getDeck();
	}
}
