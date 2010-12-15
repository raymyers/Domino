Envjs({
    scriptTypes : {
        '': true, //inline and anonymous
        'text/javascript': true,
        'text/envjs': false
    },
    beforeScriptLoad:{
        '.*': function(scriptNode){
            print(scriptNode.src);
        }
    }
});
