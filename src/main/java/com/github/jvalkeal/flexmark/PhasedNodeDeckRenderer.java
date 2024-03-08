package com.github.jvalkeal.flexmark;

import java.util.Set;

import com.vladsch.flexmark.util.ast.Document;

public interface PhasedNodeDeckRenderer extends NodeDeckRenderer {
	Set<DeckRendererPhase> getFormattingPhases();
	void renderDocument(DeckRendererContext docx, Document document, DeckRendererPhase phase);
}
