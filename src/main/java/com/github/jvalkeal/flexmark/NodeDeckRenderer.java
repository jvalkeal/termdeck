package com.github.jvalkeal.flexmark;

import java.util.Set;

public interface NodeDeckRenderer {
	Set<NodeDeckRendererHandler<?>> getNodeFormattingHandlers();
	Set<Class<?>> getNodeClasses();
}
