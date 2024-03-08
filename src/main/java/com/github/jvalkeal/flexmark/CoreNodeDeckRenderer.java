package com.github.jvalkeal.flexmark;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.ParagraphItemContainer;
import com.vladsch.flexmark.docx.converter.DocxRendererContext;
import com.vladsch.flexmark.docx.converter.NodeDocxRendererHandler;
import com.vladsch.flexmark.docx.converter.internal.CoreNodeDocxRenderer;
import com.vladsch.flexmark.ext.aside.AsideBlock;
import com.vladsch.flexmark.ext.enumerated.reference.EnumeratedReferenceBlock;
import com.vladsch.flexmark.ext.footnotes.FootnoteBlock;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NonRenderingInline;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class CoreNodeDeckRenderer implements PhasedNodeDeckRenderer {

    final public static HashSet<DeckRendererPhase> RENDERING_PHASES = new HashSet<>(Arrays.asList(
            DeckRendererPhase.COLLECT,
            DeckRendererPhase.DOCUMENT_TOP,
            DeckRendererPhase.DOCUMENT_BOTTOM
    ));


	public CoreNodeDeckRenderer(DataHolder options) {

	}

	@Override
	public Set<NodeDeckRendererHandler<?>> getNodeFormattingHandlers() {
		return new HashSet<>(Arrays.asList(
            new NodeDeckRendererHandler<>(Document.class, CoreNodeDeckRenderer.this::render),
			new NodeDeckRendererHandler<>(Node.class, CoreNodeDeckRenderer.this::render),
			new NodeDeckRendererHandler<>(Paragraph.class, CoreNodeDeckRenderer.this::render)
		));
	}

	@Override
	public Set<Class<?>> getNodeClasses() {
		throw new UnsupportedOperationException("Unimplemented method 'getNodeClasses'");
	}

	@Override
	public Set<DeckRendererPhase> getFormattingPhases() {
		return RENDERING_PHASES;
	}

	@Override
	public void renderDocument(DeckRendererContext docx, Document document, DeckRendererPhase phase) {
		switch (phase) {
			case COLLECT:

				break;

			default:
				break;
		}
	}

    private void render(Document node, DeckRendererContext docx) {
		System.out.println("XXX render Document " + node);
        // No rendering itself
        docx.renderChildren(node);
    }

    private void render(Node node, DeckRendererContext docx) {
		System.out.println("XXX render Node " + node);
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

    private void render(Paragraph node, DeckRendererContext docx) {
		System.out.println("XXX render Paragraph " + node);
        // if (node.getParent() instanceof EnumeratedReferenceBlock) {
        //     // we need to unwrap the paragraphs
        //     addBlockAttributeFormatting(node, AttributablePart.NODE, docx, false);
        //     docx.renderChildren(node);
        // } else if (!(node.getParent() instanceof ParagraphItemContainer) || !((ParagraphItemContainer) node.getParent()).isItemParagraph(node)) {
        //     if (node.getParent() instanceof BlockQuote || node.getParent() instanceof AsideBlock) {
        //         // the parent handles our formatting
        //         addBlockAttributeFormatting(node, AttributablePart.NODE, docx, true);
        //         docx.renderChildren(node);
        //     } else {
        //         if (node.getFirstChildAnyNot(NonRenderingInline.class) != null || hasRenderingAttribute(node)) {
        //             docx.setBlockFormatProvider(new BlockFormatProviderBase<>(docx, docx.getDocxRendererOptions().LOOSE_PARAGRAPH_STYLE));
        //             addBlockAttributeFormatting(node, AttributablePart.NODE, docx, true);
        //             docx.renderChildren(node);
        //         }
        //     }
        // } else {
        //     // the parent handles our formatting
        //     // for footnotes there is already an open paragraph, re-use it
        //     addBlockAttributeFormatting(node, AttributablePart.NODE, docx, !(node.getParent() instanceof FootnoteBlock));
        //     docx.renderChildren(node);
        // }
    }


}
