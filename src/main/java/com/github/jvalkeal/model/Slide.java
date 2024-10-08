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
package com.github.jvalkeal.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Slide represents a partial content in a {@link Deck}.
 *
 * @author Janne Valkealahti
 */
public class Slide {

	private final List<Chunk> blocks;

	public Slide(@NonNull List<Chunk> blocks) {
		Assert.notNull(blocks, "blocks cannot be null");
		this.blocks = new ArrayList<>(blocks);
	}

	public List<Chunk> blocks() {
		return blocks;
	}

	public static Slide of(List<Chunk> blocks) {
		return new Slide(blocks);
	}

}
