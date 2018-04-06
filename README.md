
# Webcam Effects with Vaadin Flow and Webassembly

Simple web application that displays contents of a webcam and enables
several basic image filters. This was created as a simple example of 
using a webassembly module in the Vaadin Flow framework.

Learn more about [Vaadin Flow Framework](https://vaadin.com/flow).

This application uses the [WebDSP webassembly](https://github.com/shamadee/web-dsp) 
module for filter effects.

## Starting the server ##  

To start the server, open a terminal and run `mvn jetty:run` and open 
[http://localhost:8080](http://localhost:8080) in browser.

## How to use webassembly modules with Vaadin Flow ##

There is nothing special that needs to be done in order to use a webassembly
module in Vaadin Flow.  You can simply import the module in whichever client
side class you wish.  You then use the module in your client side code as you
normally would.

In this application, we import the `webdsp.wasm` module from
the `webdsp-element.html` file (Polymer component).  In order to do this we need to 
import the `webdsp.js` file (standard wrapper for the wasm) and fetch
the wasm.

```
<script src = './webdsp/webdsp.js' type = 'text/javascript'></script>
```

```
// note: loadWASM function is a fetch wrapper defined in webdsp.js
loadWASM().then(module => {
    this._webdsp = module;
});
```

