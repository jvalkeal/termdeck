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
package com.github.jvalkeal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelParserTests {

	@Test
	void basicSinglePage() {
		String data = """
				hello
				""";
		Deck deck = parse(data);
		assertThat(deck).satisfies(d -> {
			assertThat(d.getSlides()).hasSize(1);
			assertThat(d.getSlides().get(0).getContent()).isEqualTo("hello");
		});
	}

	@Test
	void basicMultiPage() {
		String data = """
				hello1

				---
				hello2
				""";
		Deck deck = parse(data);
		assertThat(deck).satisfies(d -> {
			assertThat(d.getSlides()).hasSize(2);
			assertThat(d.getSlides().get(0).getContent()).isEqualTo("hello1");
			assertThat(d.getSlides().get(1).getContent()).isEqualTo("hello2");
		});
	}

	private Deck parse(String data) {
		ModelParser modelParser = new ModelParser();
		return modelParser.parse(data);
	}
}
