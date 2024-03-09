package com.github.jvalkeal.flexmark;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.jvalkeal.flexmark.DeckRenderer;
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
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
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
		// Parser parser = Parser.builder(options).build();
		DeckRenderer renderer = DeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);
		List<List<String>> deckContent = renderer.render(document);
		assertThat(deckContent).isNotNull();

	}

	@Test
	void basicSinglePage() {
				String markdown = """
					# slide1
					hello1

					---
					# slide2
					hello2
					""";
		DataHolder options = new MutableDataSet();
		Parser parser = Parser.builder(options).build();
		DeckRenderer renderer = DeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);
		List<List<String>> deckContent = renderer.render(document);
		assertThat(deckContent).isNotNull();
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
		DeckRenderer renderer = DeckRenderer.builder(options).build();
		Node document = parser.parse(markdown);
		List<List<String>> deckContent = renderer.render(document);
		assertThat(deckContent).isNotNull();
	}

	@Test
	void docxTest() {
		String markdown = """
			# slide
			hello
			""";

		Parser PARSER = Parser.builder(OPTIONS).build();
		DocxRenderer RENDERER = DocxRenderer.builder(OPTIONS).build();

		Node document = PARSER.parse(markdown);

		// to get XML
		String xml = RENDERER.render(document);

		// or to control the package
		// WordprocessingMLPackage template = DocxRenderer.getDefaultTemplate();
		// RENDERER.render(document, template);

	}

	final private static DataHolder OPTIONS = new MutableDataSet()
			.set(Parser.EXTENSIONS, Arrays.asList(
					DefinitionExtension.create(),
					EmojiExtension.create(),
					FootnoteExtension.create(),
					StrikethroughSubscriptExtension.create(),
					InsExtension.create(),
					SuperscriptExtension.create(),
					TablesExtension.create(),
					TocExtension.create(),
					SimTocExtension.create(),
					WikiLinkExtension.create()
			))
			.set(DocxRenderer.SUPPRESS_HTML, true)
			// the following two are needed to allow doc relative and site relative address resolution
			.set(DocxRenderer.DOC_RELATIVE_URL, "file:///Users/vlad/src/pdf") // this will be used for URLs like 'images/...' or './' or '../'
			.set(DocxRenderer.DOC_ROOT_URL, "file:///Users/vlad/src/pdf") // this will be used for URLs like: '/...'
			;

}
