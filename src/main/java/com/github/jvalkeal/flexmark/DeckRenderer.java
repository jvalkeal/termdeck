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
package com.github.jvalkeal.flexmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.builder.BuilderBase;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.ScopedDataSet;
import com.vladsch.flexmark.util.misc.Extension;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * @author Janne Valkealahti
 */
public class DeckRenderer {

    final private DataHolder options;
	final List<NodeDeckRendererFactory> nodeFormatterFactories;

	DeckRenderer(Builder builder) {
		options = builder.toImmutable();
		this.nodeFormatterFactories = new ArrayList<>(builder.nodeDeckRendererFactories.size() + 1);
		this.nodeFormatterFactories.addAll(builder.nodeDeckRendererFactories);

		this.nodeFormatterFactories.add(CoreNodeDeckRenderer::new);
	}

	public static Builder builder(DataHolder options) {
		return new Builder(options);
	}

	public void render(Node node) {
		MainDeckRenderer renderer = new MainDeckRenderer(options, node.getDocument());
		renderer.render(node);
	}

	public static class Builder extends BuilderBase<Builder> {

		final List<NodeDeckRendererFactory> nodeDeckRendererFactories = new ArrayList<>();

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
		public @NotNull DeckRenderer build() {
			return new DeckRenderer(this);
		}

	}

    private class MainDeckRenderer extends DeckContextImpl<Node> implements DeckRendererContext {

		final private Map<Class<?>, NodeDeckRendererHandler<?>> renderers;
		final private Set<DeckRendererPhase> renderingPhases;
		private DeckRendererPhase phase;
		Node renderingNode;
		final private List<PhasedNodeDeckRenderer> phasedFormatters;

		MainDeckRenderer(DataHolder options, Document document) {
			super(new ScopedDataSet(document, options));
			this.renderingPhases = new HashSet<>(DeckRendererPhase.values().length);
			this.renderers = new HashMap<>(32);
			this.phasedFormatters = new ArrayList<>(nodeFormatterFactories.size());

			for (int i = nodeFormatterFactories.size() - 1; i >= 0; i--) {
                NodeDeckRendererFactory nodeDocxRendererFactory = nodeFormatterFactories.get(i);
                NodeDeckRenderer nodeDeckRenderer = nodeDocxRendererFactory.create(this.getOptions());
                Set<NodeDeckRendererHandler<?>> formattingHandlers = nodeDeckRenderer.getNodeFormattingHandlers();
                if (formattingHandlers == null) continue;

                for (NodeDeckRendererHandler<?> nodeType : formattingHandlers) {
                    // Overwrite existing renderer
                    renderers.put(nodeType.getNodeType(), nodeType);
                }

                // // get nodes of interest
                // Set<Class<?>> nodeClasses = nodeDeckRenderer.getNodeClasses();
                // if (nodeClasses != null) {
                //     collectNodeTypes.addAll(nodeClasses);
                // }

                // // get nodes of interest
                // Set<Class<?>> wrapChildrenClasses = nodeDeckRenderer.getBookmarkWrapsChildrenClasses();
                // if (wrapChildrenClasses != null) {
                //     bookmarkWrapsChildren.addAll(wrapChildrenClasses);
                // }

                if (nodeDeckRenderer instanceof PhasedNodeDeckRenderer) {
                    Set<DeckRendererPhase> phases = ((PhasedNodeDeckRenderer) nodeDeckRenderer).getFormattingPhases();
                    if (phases != null) {
                        if (phases.isEmpty()) throw new IllegalStateException("PhasedNodeDocxRenderer with empty Phases");
                        this.renderingPhases.addAll(phases);
                        this.phasedFormatters.add((PhasedNodeDeckRenderer) nodeDeckRenderer);
                    } else {
                        throw new IllegalStateException("PhasedNodeDocxRenderer with null Phases");
                    }
                }

			}
		}

        // @Override
        public @NotNull DataHolder getOptions() {
            return options;
        }

		public void render(Node node) {
			if (node instanceof Document) {
				for (DeckRendererPhase phase : DeckRendererPhase.values()) {
					if (phase != DeckRendererPhase.DOCUMENT && !renderingPhases.contains(phase)) {
						continue;
					}
					this.phase = phase;
					if (this.phase == DeckRendererPhase.DOCUMENT) {
					    NodeDeckRendererHandler<?> nodeRenderer = renderers.get(node.getClass());
					    if (nodeRenderer != null) {
					        renderingNode = node;
					        nodeRenderer.render(node, this);
					        renderingNode = null;
					    }
					} else {
					    for (PhasedNodeDeckRenderer phasedFormatter : phasedFormatters) {
					        if (phasedFormatter.getFormattingPhases().contains(phase)) {
					            renderingNode = node;
					            phasedFormatter.renderDocument(this, (Document) node, phase);
					            renderingNode = null;
					        }
					    }
					}

				}
			}
			else {
                NodeDeckRendererHandler<?> nodeRenderer = renderers.get(node.getClass());

                if (nodeRenderer == null) {
                    nodeRenderer = renderers.get(Node.class);
                }

                if (nodeRenderer != null) {
                    NodeDeckRendererHandler<?> finalNodeRenderer = nodeRenderer;
                    Node oldNode = MainDeckRenderer.this.renderingNode;
                    renderingNode = node;

					finalNodeRenderer.render(renderingNode, MainDeckRenderer.this);
					renderingNode = oldNode;
                    // contextFramed(() -> {
                    //     String id = getNodeId(node);
                    //     if (id != null && !id.isEmpty()) {
                    //         if (!bookmarkWrapsChildren.contains(node.getClass())) {
                    //             boolean isBlockBookmark = node instanceof Block;
                    //             if (isBlockBookmark) {
                    //                 // put bookmark before the block element
                    //                 CTBookmark bookmarkStart = createBookmarkStart(id, true);
                    //                 createBookmarkEnd(bookmarkStart, true);
                    //                 finalNodeRenderer.render(renderingNode, MainDocxRenderer.this);
                    //             } else {
                    //                 // wrap bookmark around the inline element
                    //                 CTBookmark bookmarkStart = createBookmarkStart(id, false);
                    //                 finalNodeRenderer.render(renderingNode, MainDocxRenderer.this);
                    //                 createBookmarkEnd(bookmarkStart, false);
                    //             }
                    //         } else {
                    //             finalNodeRenderer.render(renderingNode, MainDocxRenderer.this);
                    //         }
                    //     } else {
                    //         finalNodeRenderer.render(renderingNode, MainDocxRenderer.this);
                    //     }
                    //     renderingNode = oldNode;
                    // });
                } else {
                    // default behavior is controlled by generic Node.class that is implemented in CoreNodeDocxRenderer
                    throw new IllegalStateException("Core Node DocxRenderer should implement generic Node renderer");
                }

			}
		}

        void renderChildrenUnwrapped(Node parent) {
            Node node = parent.getFirstChild();
            while (node != null) {
                Node next = node.getNext();
                render(node);
                node = next;
            }
        }

        @Override
        public void append(String text) {

        }

        @Override
        public void renderChildren(@NotNull Node parent) {
            // String id = getNodeId(parent);
            // if (id != null && !id.isEmpty()) {
            //     if (bookmarkWrapsChildren.contains(parent.getClass())) {
            //         CTBookmark bookmarkStart = createBookmarkStart(id, false);
            //         renderChildrenUnwrapped(parent);
            //         createBookmarkEnd(bookmarkStart, false);
            //         //contextFramed(new Runnable() {
            //         //    @Override
            //         //    public void run() {
            //         //    }
            //         //});
            //     } else {
            //         renderChildrenUnwrapped(parent);
            //     }
            // } else {
            //     renderChildrenUnwrapped(parent);
            // }
			renderChildrenUnwrapped(parent);
        }

	}

}
