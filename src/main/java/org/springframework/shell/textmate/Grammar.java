package org.springframework.shell.textmate;

import java.time.Duration;

public class Grammar implements IGrammar {

	private String scopeName;

	public Grammar(String scopeName) {
		this.scopeName = scopeName;
	}

	@Override
	public ITokenizeLineResult tokenizeLine(String lineText, StateStack prevState, Duration timeLimit) {

		// const r = this._tokenize(lineText, prevState, false, timeLimit);
		// return {
		// 	tokens: r.lineTokens.getResult(r.ruleStack, r.lineLength),
		// 	ruleStack: r.ruleStack,
		// 	stoppedEarly: r.stoppedEarly,
		// };

		return null;
	}

	public static Grammar createGrammar(String scopeName) {
		return new Grammar(scopeName);
	}
}
