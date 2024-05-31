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

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.visitor.AstAction;
import com.vladsch.flexmark.util.visitor.AstHandler;

public class NodeTermdeckRendererHandler<N extends Node>
		extends AstHandler<N, NodeTermdeckRendererHandler.CustomNodeDeckRenderer<N>> {

	public NodeTermdeckRendererHandler(Class<N> aClass, CustomNodeDeckRenderer<N> adapter) {
		super(aClass, adapter);
	}

	@SuppressWarnings("unchecked")
	public void render(Node node, TermdeckRendererContext context) {
		getAdapter().render((N) node, context);
	}

	public static interface CustomNodeDeckRenderer<N extends Node> extends AstAction<N> {
		void render(N node, TermdeckRendererContext context);
	}
}
