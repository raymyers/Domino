package com.cadrlife.domino;

import java.io.IOException;
import java.nio.charset.Charset;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.cadrlife.domino.internal.FakeHttpRequestFactory;
import com.google.common.io.Resources;

public class Domino {
	private Context cx;
	private Scriptable scope;
	private FakeHttpRequestFactory fakeHttpRequestFactory;
	private boolean preloadJQuery = true;
	
	public void init() {
		cx = Context.enter();
		cx.setOptimizationLevel(-1);
		cx.setLanguageVersion(Context.VERSION_1_5);
		scope = cx.initStandardObjects();
		//evalString("function print(message) {java.lang.System.out.println(message);}");
		evalResource("env.rhino.js");
		evalResource("envjs-config.js");
//		evalResource("override-xmlhttprequest.js");
		if (isPreloadJQuery()) {
			evalResource("jquery-1.4.4.min.js");
		}
		fakeHttpRequestFactory = new FakeHttpRequestFactory();
		ScriptableObject.putProperty(scope, "FakeHttpRequestFactory", fakeHttpRequestFactory);
	}
	

	public String evalString(String string) {
		return Context.toString(cx.evaluateString(scope, string, "string", 1, null));
	}
	
	public void navigateTo(String location) {
		evalString("window.location='" + location + "'");
	}

	public JQueryFacade $(String selector) {
		return new JQueryFacade(this, selector);
	}

	private void evalResource(String resourceName) {
		try {
			String js = Resources.toString(Resources.getResource(Domino.class,resourceName), Charset.defaultCharset());
			cx.evaluateString(scope, js, resourceName, 1, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public void setPreloadJQuery(boolean preloadJQuery) {
		this.preloadJQuery = preloadJQuery;
	}


	public boolean isPreloadJQuery() {
		return preloadJQuery;
	}
	
}
