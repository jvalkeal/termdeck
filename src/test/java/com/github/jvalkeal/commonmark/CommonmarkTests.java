package com.github.jvalkeal.commonmark;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.MarkdownSettings;
import com.github.jvalkeal.model.Slide;
import net.bytebuddy.asm.Advice.Enter;
import net.bytebuddy.asm.Advice.Exit;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
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

// Enter Document{}
// Enter BulletList{}
// Enter ListItem{}
// Enter Paragraph{}
// Enter Text{literal=list1}
// Exit Paragraph{}
// Exit ListItem{}
// Enter ListItem{}
// Enter Paragraph{}
// Enter Text{literal=list2}

	@Test
	void bulletList1() {
		String markdown = """
			* list1
			* list2
			""";
		Deck deck = parse(markdown);
		assertThat(deck.getSlides()).hasSize(1);
		assertThat(extractSlideLines(deck.getSlides().get(0))).containsExactly("  - list1", "  - list2");

		// assertThat(deck.getSlides()).hasSize(1).satisfiesExactly(
		// 	slide -> {
		// 		assertThat(slide.blocks()).hasSize(1).satisfiesExactly(
		// 			block -> {
		// 				assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(2).satisfiesExactly(
		// 					content -> {
		// 						assertThat(content).contains("  - list1");
		// 					},
		// 					content -> {
		// 						assertThat(content).contains("  - list2");
		// 					}
		// 				);
		// 			}
		// 		);
		// 	}
		// );

	}

	private List<String> extractSlideLines(Slide slide) {
		return slide.blocks().stream()
			.flatMap(block -> block.resolveContent(themeResolver, markdownSettings).stream())
			.collect(Collectors.toList())
			;
	}

	@Test
	void bulletList2() {
		String markdown = """
			* para1

			  para21
			  para22
			""";
		Deck deck = parse(markdown);


	}

// Enter Document{}
// Enter BulletList{}
// Enter ListItem{}
// Enter Paragraph{}
// Enter Text{literal=Like bulleted list}

	@Test
	void numberList() {
		String markdown = """
			1. item1
			2. item2
			""";
		Deck deck = parse(markdown);

		assertThat(deck.getSlides()).hasSize(1).satisfiesExactly(
			slide -> {
				assertThat(slide.blocks()).hasSize(1).satisfiesExactly(
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(2).satisfiesExactly(
							content -> {
								assertThat(content).contains("  - item1");
							},
							content -> {
								assertThat(content).contains("  - item2");
							}
						);
					}
				);
			}
		);

	}

// Enter Document{}
// Enter Paragraph{}
// Enter Text{literal=text}

	@Test
	void simpleParagraph() {
		String markdown = """
			text
			""";
		Deck deck = parse(markdown);

	}

	@Test
	void multiLineInParagraph() {
		String markdown = """
			text1
			text2
			""";
		Deck deck = parse(markdown);

		assertThat(deck.getSlides()).hasSize(1).satisfiesExactly(
			slide -> {
				assertThat(slide.blocks()).hasSize(1).satisfiesExactly(
					block -> {
						assertThat(block.resolveContent(themeResolver, markdownSettings)).hasSize(1).satisfiesExactly(
							content -> {
								assertThat(content).contains("text1 text2");
							}
						);
					}
				);
			}
		);
	}

//
// Enter Document{}
// Enter Heading{}
// Enter Text{literal=h1}


	@Test
	void onlyHeading() {
		String markdown = """
			# h1
			""";
		Deck deck = parse(markdown);

	}

// Visit start TableBlock{} Document{}
// Visit start TableHead{} TableBlock{}
// Visit start TableRow{} TableHead{}
// Visit start TableCell{} TableRow{}
// Visit start Text{literal=header1} TableCell{}
// Visit start TableBody{} TableBlock{}


	@Test
	void basicTable() {
		String markdown = """
			| header1 | header2 |
			| ------- | ------- |
			| row11   | row12   |
			""";
		Deck deck = parse(markdown);
		assertThat(deck.getSlides()).hasSize(1);
		extractSlideLines(deck.getSlides().get(0));
		// assertThat(extractSlideLines(deck.getSlides().get(0))).containsExactly("  - list1");

	}

	@Test
	void frontmatter() {
		String markdown = """
			---
			author: fakeauthor
			---
			text
			""";
		Deck deck = parse(markdown);
		assertThat(deck.getDeckSettings()).satisfies(settings -> {
			assertThat(settings.getAuthor()).isEqualTo("fakeauthor");
		});
		// assertThat(deck.getFrontMatterValues()).hasSize(1);
		// assertThat(deck.getFrontMatterValues()).containsEntry("key1", List.of("value1"));
	}

	@Test
	void code1() {
		String markdown = """
			```
			code
			```
			""";
		Deck deck = parse(markdown);

	}

	@Test
	void code2() {
		String markdown = """
			```
			text
				code
			text
			```
			""";
		Deck deck = parse(markdown);

	}

	private Deck parse(String markdown) {
		List<Extension> extensions = List.of(TablesExtension.create(), YamlFrontMatterExtension.create());
		Parser parser = Parser.builder()
			.extensions(extensions)
			.build();
		Node document = parser.parse(markdown);
		MarkdownVisitor visitor = new MarkdownVisitor();
		document.accept(visitor);
		return visitor.getDeck();
	}
}
