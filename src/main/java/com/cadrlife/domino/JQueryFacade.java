package com.cadrlife.domino;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class JQueryFacade {

	private final Domino domino;
	private List<String> chain;

	public JQueryFacade(Domino domino, String selector) {
		this.domino = domino;
		this.chain = Lists.newArrayList();
		this.chain.add("$('" + selector + "')");
	}
	
	public JQueryFacade text() {
		chain.add("text()");
		return this;
	}
	
	public JQueryFacade html() {
		chain.add("html()");
		return this;
	}
	
	public String eval() {
		return domino.evalString(Joiner.on(".").join(chain));
	}

	public JQueryFacade attr(String attribute) {
		chain.add("attr('"+ attribute + "')");
		return this;
	}
	
	@Override
	public String toString() {
		return eval();
	}
	
}
