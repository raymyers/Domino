
/**
 *
 * @class XMLHttpRequest
 * @author Originally implemented by Yehuda Katz
 *
 */

// this implementation can be used without requiring a DOMParser
// assuming you dont try to use it to get xml/html documents
var domparser;

XMLHttpRequest = function(){
    this.headers = {};
    this.responseHeaders = {};
    this.aborted = false;//non-standard
};

// defined by the standard: http://www.w3.org/TR/XMLHttpRequest/#xmlhttprequest
// but not provided by Firefox.  Safari and others do define it.
XMLHttpRequest.UNSENT = 0;
XMLHttpRequest.OPEN = 1;
XMLHttpRequest.HEADERS_RECEIVED = 2;
XMLHttpRequest.LOADING = 3;
XMLHttpRequest.DONE = 4;

XMLHttpRequest.prototype = {
    open: function(method, url, async, user, password){
        //console.log('openning xhr %s %s %s', method, url, async);
        this.readyState = 1;
        this.async = (async === false)?false:true;
        this.method = method || "GET";
        this.url = Envjs.uri(url);
        this.onreadystatechange();
    },
    setRequestHeader: function(header, value){
        this.headers[header] = value;
    },
    
    send: function(data, parsedoc/*non-standard*/){
        var _this = this;
        parsedoc = (parsedoc === undefined)?true:!!parsedoc;
        function fromJavaMap(javaMap) {
        	var jsMap = {};
        	var keys = javaMap.keySet().toArray();
        	for (var i=0; i<keys.length; i++) {
        		jsMap[keys[i]] = javaMap.get(keys[i]);
        	}
        	return jsMap;
        }
        function makeRequest() {
            var cookie = Envjs.getCookies(_this.url);
            if(cookie){
                _this.setRequestHeader('COOKIE', cookie);
            }
            var fakeReq = FakeHttpRequestFactory.newInstance();
            fakeReq.setMethod(_this.method);
            fakeReq.setUrl(_this.url);
            fakeReq.setData(data);
            fakeReq.execute();
            _this.responseHeaders = fromJavaMap(fakeReq.getResponseHeaders());
            _this.responseText = '' + fakeReq.getResponseText();
            _this.status = 0 + fakeReq.getResponseStatus();
            function processResponse() {
                if (!_this.aborted){
                    var doc = null,
                        domparser,
                        cookie;
                    // try to parse the document if we havent explicitly set a
                    // flag saying not to and if we can assure the text at least
                    // starts with valid xml
                    if ( parsedoc && _this.responseText.match(/^\s*</) ) {
                        domparser = domparser||new DOMParser();
                        try {
                            //console.log("parsing response text into xml document");
                            doc = domparser.parseFromString(_this.responseText+"", 'text/xml');
                        } catch(e) {
                            //Envjs.error('response XML does not appear to be well formed xml', e);
                            console.warn('parseerror \n%s', e);
                            doc = document.implementation.createDocument('','error',null);
                            doc.appendChild(doc.createTextNode(e+''));
                        }
                    }else{
                        //Envjs.warn('response XML does not appear to be xml');
                    }
                    
                    try{
                        cookie = _this.getResponseHeader('SET-COOKIE');
                        if(cookie){
                             Envjs.setCookie(_this.url, cookie);
                        }
                    }catch(e){
                        console.warn("Failed to set cookie");
                    }
                    _this.__defineGetter__("responseXML", function(){
                        return doc;
                    });
                }
            };
            processResponse();
            _this.readyState = 4;

            if (!_this.aborted){
                _this.onreadystatechange();
            }
        }

        if (this.async){
            //TODO: what we really need to do here is rejoin the
            //      current thread and call onreadystatechange via
            //      setTimeout so the callback is essentially applied
            //      at the end of the current callstack
            //console.log('requesting async: %s', this.url);
            Envjs.runAsync(makeRequest);
        }else{
            //console.log('requesting sync: %s', this.url);
            makeRequest();
        }
    },
    abort: function(){
        this.aborted = true;
    },
    onreadystatechange: function(){
        //Instance specific
    },
    getResponseHeader: function(header){
        //$debug('GETTING RESPONSE HEADER '+header);
        var rHeader, returnedHeaders;
        if (this.readyState < 3){
            throw new Error("INVALID_STATE_ERR");
        } else {
            returnedHeaders = [];
            for (rHeader in this.responseHeaders) {
                if (rHeader.match(new RegExp(header, "i"))) {
                    returnedHeaders.push(this.responseHeaders[rHeader]);
                }
            }

            if (returnedHeaders.length){
                //$debug('GOT RESPONSE HEADER '+returnedHeaders.join(", "));
                return returnedHeaders.join(", ");
            }
        }
        return null;
    },
    getAllResponseHeaders: function(){
        var header, returnedHeaders = [];
        if (this.readyState < 3){
            throw new Error("INVALID_STATE_ERR");
        } else {
            for (header in this.responseHeaders) {
                returnedHeaders.push( header + ": " + this.responseHeaders[header] );
            }
        }
        return returnedHeaders.join("\r\n");
    },
    async: true,
    readyState: 0,
    responseText: "",
    status: 0,
    statusText: ""
};
