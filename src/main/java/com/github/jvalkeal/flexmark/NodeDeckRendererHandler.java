package com.github.jvalkeal.flexmark;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.visitor.AstAction;
import com.vladsch.flexmark.util.visitor.AstHandler;

public class NodeDeckRendererHandler<N extends Node>
		extends AstHandler<N, NodeDeckRendererHandler.CustomNodeDeckRenderer<N>> {
	public NodeDeckRendererHandler(Class<N> aClass, CustomNodeDeckRenderer<N> adapter) {
		super(aClass, adapter);
	}

	public void render(Node node, DeckRendererContext context) {
		// noinspection unchecked
		getAdapter().render((N) node, context);
	}

	public static interface CustomNodeDeckRenderer<N extends Node> extends AstAction<N> {
		void render(N node, DeckRendererContext context);
	}
}
