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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.ParagraphItemContainer;
import com.vladsch.flexmark.ast.ThematicBreak;
import com.vladsch.flexmark.ext.aside.AsideBlock;
import com.vladsch.flexmark.ext.attributes.AttributeNode;
import com.vladsch.flexmark.ext.attributes.AttributesNode;
import com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceBlock;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NonRenderingInline;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreNodeTermdeckRenderer implements PhasedNodeTermdeckRenderer {

	private final Logger log = LoggerFactory.getLogger(CoreNodeTermdeckRenderer.class);

	final public static HashSet<TermdeckRendererPhase> RENDERING_PHASES = new HashSet<>(Arrays.asList(
			TermdeckRendererPhase.COLLECT,
			TermdeckRendererPhase.DOCUMENT_TOP,
			TermdeckRendererPhase.DOCUMENT_BOTTOM
	));


	public CoreNodeTermdeckRenderer(DataHolder options) {

	}

	@Override
	public Set<NodeTermdeckRendererHandler<?>> getNodeFormattingHandlers() {
		return new HashSet<>(Arrays.asList(
			new NodeTermdeckRendererHandler<>(Document.class, CoreNodeTermdeckRenderer.this::render),
			new NodeTermdeckRendererHandler<>(Heading.class, CoreNodeTermdeckRenderer.this::render),
			new NodeTermdeckRendererHandler<>(Node.class, CoreNodeTermdeckRenderer.this::render),
			new NodeTermdeckRendererHandler<>(Paragraph.class, CoreNodeTermdeckRenderer.this::render),
			new NodeTermdeckRendererHandler<>(ThematicBreak.class, CoreNodeTermdeckRenderer.this::render)
		));
	}

	@Override
	public Set<Class<?>> getNodeClasses() {
		throw new UnsupportedOperationException("Unimplemented method 'getNodeClasses'");
	}

	@Override
	public Set<TermdeckRendererPhase> getFormattingPhases() {
		return RENDERING_PHASES;
	}

	@Override
	public void renderDocument(TermdeckRendererContext docx, Document document, TermdeckRendererPhase phase) {
		switch (phase) {
			case COLLECT:

				break;

			default:
				break;
		}
	}

	private void render(Document node, TermdeckRendererContext docx) {
		log.debug("Render Document {}", node);
		docx.addSlide();
		docx.renderChildren(node);
	}

	private void render(Heading node, TermdeckRendererContext docx) {
		log.debug("Render Heading {}", node);
		BasedSequence text = node.getText();
		String string = text.toString();
		docx.append(string);
		// docx.setBlockFormatProvider(new HeadingBlockFormatProvider<>(docx, node.getLevel() - 1));
		// addBlockAttributeFormatting(node, AttributablePart.NODE, docx, false);
		docx.renderChildren(node);
	}

	private void render(Node node, TermdeckRendererContext docx) {
		log.debug("Render Node {}", node);
		// BasedSequence chars = node.getChars();
		// MainDocumentPart mdp = docx.getDocxDocument();
		// if (node instanceof Block) {
		//     docx.setBlockFormatProvider(new BlockFormatProviderBase<>(docx, docx.getDocxRendererOptions().LOOSE_PARAGRAPH_STYLE));
		//     docx.createP();
		//     docx.renderChildren(node);
		// } else {
		//     docx.addTextCreateR(chars.unescape());
		// }
	}

	private void render(ThematicBreak node, TermdeckRendererContext docx) {
		log.debug("Render ThematicBreak {}", node);
		docx.addSlide();
	}

	private void render(Paragraph node, TermdeckRendererContext docx) {
		log.debug("Render Paragraph {}", node);
		docx.append(node.getChars().toString());
		if (node.getParent() instanceof EnumeratedReferenceBlock) {
		//     // we need to unwrap the paragraphs
		//     addBlockAttributeFormatting(node, AttributablePart.NODE, docx, false);
			docx.renderChildren(node);
		}
		else if (!(node.getParent() instanceof ParagraphItemContainer) || !((ParagraphItemContainer) node.getParent()).isItemParagraph(node)) {
			if (node.getParent() instanceof BlockQuote || node.getParent() instanceof AsideBlock) {
				// the parent handles our formatting
				// addBlockAttributeFormatting(node, AttributablePart.NODE, docx, true);
				docx.renderChildren(node);
			} else {
				if (node.getFirstChildAnyNot(NonRenderingInline.class) != null || hasRenderingAttribute(node)) {
					// docx.setBlockFormatProvider(new BlockFormatProviderBase<>(docx, docx.getDocxRendererOptions().LOOSE_PARAGRAPH_STYLE));
					// addBlockAttributeFormatting(node, AttributablePart.NODE, docx, true);
					docx.renderChildren(node);
				}
			}
		}
		else {
		//     // the parent handles our formatting
		//     // for footnotes there is already an open paragraph, re-use it
		//     addBlockAttributeFormatting(node, AttributablePart.NODE, docx, !(node.getParent() instanceof FootnoteBlock));
			docx.renderChildren(node);
		}
	}

	private boolean hasRenderingAttribute(Node node) {
		// boolean pageBreak = false;

		for (Node child : node.getChildren()) {
			if (child instanceof AttributesNode) {
				AttributesNode attributesNode = (AttributesNode) child;
				for (Node attribute : attributesNode.getChildren()) {
					if (attribute instanceof AttributeNode) {
						AttributeNode attributeNode = (AttributeNode) attribute;
						if (attributeNode.isClass())
							switch (attributeNode.getValue().toString()) {
								case "pagebreak":
								case "tab":
									return true;

								default:
									break;
							}
					}
				}
			}
		}
		return false;
	}


}
