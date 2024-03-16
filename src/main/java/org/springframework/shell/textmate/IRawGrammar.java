package org.springframework.shell.textmate;

public interface IRawGrammar {

	String scopeName();


	static IRawGrammar of(String scopeName) {
		return new DefaultIRawGrammar(scopeName);
	}

	static class DefaultIRawGrammar implements IRawGrammar {
		String scopeName;
		DefaultIRawGrammar(String scopeName) {
			this.scopeName = scopeName;
		}

		@Override
		public String scopeName() {
			return this.scopeName;
		}
	}
}
