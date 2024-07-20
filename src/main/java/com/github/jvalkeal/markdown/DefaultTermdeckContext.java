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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.jvalkeal.model.Block;
import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.Slide;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.ScopedDataSet;

class DefaultTermdeckContext /*extends TermdeckContextImpl<Node>*/ implements TermdeckRendererContext {

	final private Map<Class<?>, NodeTermdeckRendererHandler<?>> renderers;
	final private Set<TermdeckRendererPhase> renderingPhases;
	private TermdeckRendererPhase phase;
	Node renderingNode;
	final private List<PhasedNodeTermdeckRenderer> phasedFormatters;
	final List<NodeTermdeckRendererFactory> nodeFormatterFactories;
	DataHolder options;

	DefaultTermdeckContext(DataHolder options, Document document, List<NodeTermdeckRendererFactory> nodeFormatterFactories) {
		// super(new ScopedDataSet(document, options));
		this.options = new ScopedDataSet(document, options);
		this.nodeFormatterFactories = nodeFormatterFactories;
		this.renderingPhases = new HashSet<>(TermdeckRendererPhase.values().length);
		this.renderers = new HashMap<>(32);
		this.phasedFormatters = new ArrayList<>(nodeFormatterFactories.size());

		for (int i = nodeFormatterFactories.size() - 1; i >= 0; i--) {
			NodeTermdeckRendererFactory nodeDocxRendererFactory = nodeFormatterFactories.get(i);
			NodeTermdeckRenderer nodeDeckRenderer = nodeDocxRendererFactory.create(this.getOptions());
			Set<NodeTermdeckRendererHandler<?>> formattingHandlers = nodeDeckRenderer.getNodeFormattingHandlers();
			if (formattingHandlers == null) continue;

			for (NodeTermdeckRendererHandler<?> nodeType : formattingHandlers) {
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

			if (nodeDeckRenderer instanceof PhasedNodeTermdeckRenderer) {
				Set<TermdeckRendererPhase> phases = ((PhasedNodeTermdeckRenderer) nodeDeckRenderer).getFormattingPhases();
				if (phases != null) {
					if (phases.isEmpty()) {
						throw new IllegalStateException("PhasedNodeTermdeckRenderer with empty Phases");
					}
					this.renderingPhases.addAll(phases);
					this.phasedFormatters.add((PhasedNodeTermdeckRenderer) nodeDeckRenderer);
				}
				else {
					throw new IllegalStateException("PhasedNodeTermdeckRenderer with null Phases");
				}
			}

		}
	}

	@Override
	public DataHolder getOptions() {
		return options;
	}

    @Override
    public void contextFramed(Runnable runnable) {

        // myBlockFormatProviders.put(getContextFrame(), myBlockFormatProvider);
        // myRunFormatProviders.put(getContextFrame(), myRunFormatProvider);
        // BlockFormatProvider<T> oldRenderingBlockFormatProvider = myBlockFormatProvider;
        // RunFormatProvider<T> oldRenderingRunFormatProvider = myRunFormatProvider;
        // ContentContainer oldRenderingContentContainer = myContentContainer;
        // ParaContainer oldRenderingParaContainer = myParaContainer;
        // RunContainer oldRenderingRunContainer = myRunContainer;
        // T oldNode = getContextFrame();

        // runnable.run();

        // if (oldNode != getContextFrame()) {
        //     RunFormatProvider<T> runFormatProvider = myRunFormatProviders.remove(oldNode);

        //     if (runFormatProvider != oldRenderingRunFormatProvider) {
        //         runFormatProvider.close();
        //     }
        // }
        // myRunFormatProvider = oldRenderingRunFormatProvider;

        // if (oldNode != getContextFrame()) {
        //     BlockFormatProvider<T> blockFormatProvider = myBlockFormatProviders.remove(oldNode);
        //     if (blockFormatProvider != oldRenderingBlockFormatProvider) {
        //         blockFormatProvider.close();
        //     }
        // }

        // myBlockFormatProvider = oldRenderingBlockFormatProvider;
        // myRunContainer = oldRenderingRunContainer;
        // myParaContainer = oldRenderingParaContainer;
        // myContentContainer = oldRenderingContentContainer;

	}

	public void render(Node node) {
		if (node instanceof Document) {
			for (TermdeckRendererPhase phase : TermdeckRendererPhase.values()) {
				if (phase != TermdeckRendererPhase.DOCUMENT && !renderingPhases.contains(phase)) {
					continue;
				}
				this.phase = phase;
				if (this.phase == TermdeckRendererPhase.DOCUMENT) {
					NodeTermdeckRendererHandler<?> nodeRenderer = renderers.get(node.getClass());
					if (nodeRenderer != null) {
						renderingNode = node;
						nodeRenderer.render(node, this);
						renderingNode = null;
					}
				}
				else {
					for (PhasedNodeTermdeckRenderer phasedFormatter : phasedFormatters) {
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
			NodeTermdeckRendererHandler<?> nodeRenderer = renderers.get(node.getClass());

			if (nodeRenderer == null) {
				nodeRenderer = renderers.get(Node.class);
			}

			if (nodeRenderer != null) {
				NodeTermdeckRendererHandler<?> finalNodeRenderer = nodeRenderer;
				Node oldNode = DefaultTermdeckContext.this.renderingNode;
				renderingNode = node;

				finalNodeRenderer.render(renderingNode, DefaultTermdeckContext.this);
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

				contextFramed(() -> {

				});

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

	List<List<String>> pages = new ArrayList<>();
	private List<String> currentBlock;

	// private Deck deck = new Deck();
	private List<Slide> xSlides = new ArrayList<>();
	private List<Block> xBlocks; // = new ArrayList<>();

	public Deck getDeck() {
		return new Deck(xSlides);
	}


	@Override
	public void startSlide() {
		List<String> slide = new ArrayList<>();
		pages.add(slide);
		currentBlock = slide;

		xBlocks = new ArrayList<>();
	}

	@Override
	public void endSlide() {
		if (xBlocks != null) {
			Slide slide = Slide.of(xBlocks);
			xSlides.add(slide);
		}
		xBlocks = null;
	}

	@Override
	public void append(String text) {
		// currentSlide.add(text);
		if (buf != null) {
			buf.append(text);
		}
	}

	private StringBuilder buf;

	@Override
	public void startBlock() {
		if (xBlocks == null) {
			xBlocks = new ArrayList<>();
		}
		buf = new StringBuilder();
	}

	@Override
	public void endBlock() {
		currentBlock.add(buf.toString());
		// Block block = Block.of(Arrays.asList(buf.toString()));
		// xBlocks.add(block);
	}

	@Override
	public void renderChildren(Node parent) {
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