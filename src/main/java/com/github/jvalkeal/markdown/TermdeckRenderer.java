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

import java.util.ArrayList;
import java.util.List;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.builder.BuilderBase;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.misc.Extension;
import org.jetbrains.annotations.NotNull;

public class TermdeckRenderer {

	final private DataHolder options;
	final private List<NodeTermdeckRendererFactory> nodeFormatterFactories;

	TermdeckRenderer(Builder builder) {
		options = builder.toImmutable();
		this.nodeFormatterFactories = new ArrayList<>(builder.nodeDeckRendererFactories.size() + 1);
		this.nodeFormatterFactories.addAll(builder.nodeDeckRendererFactories);

		this.nodeFormatterFactories.add(CoreNodeTermdeckRenderer::new);
	}

	public static Builder builder(DataHolder options) {
		return new Builder(options);
	}

	public List<List<String>> render(Node node) {
		MainTermdeckContext renderer = new MainTermdeckContext(options, node.getDocument(), nodeFormatterFactories);
		renderer.render(node);
		return renderer.deck;
	}

	public static class Builder extends BuilderBase<Builder> {

		final List<NodeTermdeckRendererFactory> nodeDeckRendererFactories = new ArrayList<>();

		public Builder(DataHolder options) {
			super(options);
			loadExtensions();
		}

		@Override
		protected void removeApiPoint(@NotNull Object apiPoint) {
		}

		@Override
		protected void preloadExtension(@NotNull Extension extension) {
		}

		@Override
		protected boolean loadExtension(@NotNull Extension extension) {
			throw new UnsupportedOperationException("Unimplemented method 'loadExtension'");
		}

		@Override
		public @NotNull TermdeckRenderer build() {
			return new TermdeckRenderer(this);
		}

	}

}
