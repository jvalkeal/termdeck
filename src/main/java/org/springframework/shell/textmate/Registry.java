package org.springframework.shell.textmate;

public class Registry {

	private RegistryOptions options;
	private SyncRegistry syncRegistry;

	public Registry(RegistryOptions options) {
		this.options = options;
		this.syncRegistry = new SyncRegistry(null);
	}

	public IGrammar addGrammar(IRawGrammar rawGrammar) {
		this.syncRegistry.addGrammar(rawGrammar, null);
		// this._syncRegistry.addGrammar(rawGrammar, injections);
		// return (await this._grammarForScopeName(rawGrammar.scopeName, initialLanguage, embeddedLanguages))!;
		// return this.syncRegistry.grammarForScopeName(rawGrammar.scopeName());
		return grammarForScopeName(rawGrammar.scopeName());
	}

	public IGrammar grammarForScopeName(String scopeName) {
		return this.syncRegistry.grammarForScopeName(scopeName);
	}

}
