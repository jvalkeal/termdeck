package com.github.jvalkeal.xxx;

import com.vladsch.flexmark.util.data.DataHolder;

public interface NodeDeckRendererFactory {
	NodeDeckRenderer create(DataHolder options);
}
