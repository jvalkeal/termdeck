package org.springframework.shell.textmate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InitialTests {

	@Test
	void xxx() {
		Registry registry = new Registry(null);
		IRawGrammar rawGrammar = IRawGrammar.of("source.test");
		IGrammar grammar = registry.addGrammar(rawGrammar);
		ITokenizeLineResult result = grammar.tokenizeLine(null, null, null);
		assertThat(result).isNotNull();
	}
}
