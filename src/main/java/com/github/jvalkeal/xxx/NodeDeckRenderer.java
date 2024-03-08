package com.github.jvalkeal.xxx;

import java.util.Set;

public interface NodeDeckRenderer {
	Set<NodeDeckRendererHandler<?>> getNodeFormattingHandlers();
	Set<Class<?>> getNodeClasses();
}
