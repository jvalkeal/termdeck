package com.github.jvalkeal.xxx;

import com.vladsch.flexmark.util.ast.Node;

public interface DeckRendererContext extends DeckContext<Node> {


	void renderChildren(Node parent);
}
