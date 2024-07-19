package com.github.jvalkeal.markdown;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.jvalkeal.model.Deck;
import com.vladsch.flexmark.docx.converter.DocxRenderer;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.superscript.SuperscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.ext.wikilink.WikiLinkExtension;
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor;
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RendererTests {

	@Test
	void frontmatter() {
		String markdown = """
			---
			key1: value1
			---
			# slide1
			hello1
			""";

		YamlFrontMatterExtension ye = YamlFrontMatterExtension.create();
		DataHolder options = new MutableDataSet();
		Parser parser = Parser.builder(options).extensions(Collections.singleton(ye)).build();
		TermdeckRenderer renderer = TermdeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);

		AbstractYamlFrontMatterVisitor v = new AbstractYamlFrontMatterVisitor();
		v.visit(document);
		assertThat(v.getData()).hasSize(1);
		assertThat(v.getData()).containsEntry("key1", Arrays.asList("value1"));

		Deck deck = renderer.render(document);
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
		DataHolder options = new MutableDataSet();
		Parser parser = Parser.builder(options).build();
		TermdeckRenderer renderer = TermdeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);

		Deck deck = renderer.render(document);

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

	@Test
	void basicMultiSections() {
		String markdown = """
			# slide1
			hello11

			hello12

			---
			# slide2
			hello21

			hello22
			""";
		DataHolder options = new MutableDataSet();
		Parser parser = Parser.builder(options).build();
		TermdeckRenderer renderer = TermdeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);
		// List<List<String>> deckContent = renderer.render(document);
		// assertThat(deckContent).hasSize(2);

		Deck deck = renderer.render(document);
		assertThat(deck.getSlides()).hasSize(2);
	}

	@Test
	void codeJson() {
				String markdown = """
					```json
					{
					  "firstName": "John",
					  "lastName": "Smith"
					}
					```
					""";
		DataHolder options = new MutableDataSet();
		Parser parser = Parser.builder(options).build();
		TermdeckRenderer renderer = TermdeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);
		Deck deck = renderer.render(document);
		assertThat(deck.getSlides()).hasSize(1);
	}

	// @Test
	// void docxTest() {
	// 	String markdown = """
	// 		# slide
	// 		hello
	// 		""";

	// 	Parser PARSER = Parser.builder(OPTIONS).build();
	// 	DocxRenderer RENDERER = DocxRenderer.builder(OPTIONS).build();

	// 	Node document = PARSER.parse(markdown);

	// 	// to get XML
	// 	String xml = RENDERER.render(document);

	// 	// or to control the package
	// 	// WordprocessingMLPackage template = DocxRenderer.getDefaultTemplate();
	// 	// RENDERER.render(document, template);

	// }

	// final private static DataHolder OPTIONS = new MutableDataSet()
	// 		.set(Parser.EXTENSIONS, Arrays.asList(
	// 				DefinitionExtension.create(),
	// 				EmojiExtension.create(),
	// 				FootnoteExtension.create(),
	// 				StrikethroughSubscriptExtension.create(),
	// 				InsExtension.create(),
	// 				SuperscriptExtension.create(),
	// 				TablesExtension.create(),
	// 				TocExtension.create(),
	// 				SimTocExtension.create(),
	// 				WikiLinkExtension.create()
	// 		))
	// 		.set(DocxRenderer.SUPPRESS_HTML, true)
	// 		// the following two are needed to allow doc relative and site relative address resolution
	// 		.set(DocxRenderer.DOC_RELATIVE_URL, "file:///Users/vlad/src/pdf") // this will be used for URLs like 'images/...' or './' or '../'
	// 		.set(DocxRenderer.DOC_ROOT_URL, "file:///Users/vlad/src/pdf") // this will be used for URLs like: '/...'
	// 		;

}
