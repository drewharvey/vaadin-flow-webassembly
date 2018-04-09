
# Webcam Effects with Vaadin Flow and Webassembly

Simple web application that displays contents of a webcam and enables
several basic image filters. This was created as a simple example of 
using a webassembly module in the Vaadin Flow framework.

Learn more about [Vaadin Flow Framework](https://vaadin.com/flow).

This application uses the [WebDSP webassembly](https://github.com/shamadee/web-dsp) 
module for filter effects.

## Starting the Server ##  

To start the server, open a terminal and run `mvn jetty:run` and open 
[http://localhost:8080](http://localhost:8080) in browser.

## Loading a Webassembly Module on the Client ##

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

Now that we have the webdsp mobdule loaded, we can use it to manipulate images,
or frames coming in from our webcam, by adding effects.  We can use the module
we loaded in the `this._webdsp` object to access its functions.

```
// example:
this._webdsp.invert(params)
```

## Controlling the Webassembly Module from the Server ##

Typically in a Vaadin Flow application, you will want to control the UI
logic from the server side.  Now that we have our webassembly module loaded
on the client side, how do we utilize this from the server? 

### Basics of a Vaadin Flow Component ###

We want to create a new Vaadin Flow component that will wrap the functionality 
of the webdsp module.  This component will contain a server side class that
will send and receive information with its client side counterpart (which contains
the webdsp module). This way we can control the UI flow/logic on the server side.

To be specific, we are creating a component based on the template API.
You can learn more about creating these components here: 
[Creating A Simple Component Using the Template API](https://vaadin.com/docs/v10/flow/polymer-templates/tutorial-template-basic.html)


This component will consist of:

 * Server side class
 * Client side class (Polymer component)
 * Data model interface (used to map properties between server and client objects)
 
In this application, these classes are:

 * Server - WebDSP.java
 * Client - webdsp-element.html
 * Interface - WebDspModel (inside WebDSP.java)
 
### Updating Client Properties from the Server ###

The way we can trigger the client side to do something, is by updating the 
client classes properties (Polymer component properties). These properties
should have some kind of observer that will update the UI when they are
changed.  This is where the Data model interface comes in handy. By specifying
getters and setters for the available properties, we simply need to call these
methods on the server side, and Vaadin Flow will update the properties on
the client side.

Basic flow of events:

 1. Application calls public setter in WebDSP.java (server side class).
 1. WebDSP.java calls setter in WebDspModel (data model interface).
 1. WebDspModel updates property webdsp-element.html (client side class).
 1. webdsp-element.html observes these property changes and updates UI.

In this application the client class (`webdsp-element.html`) contains a `filter`
property.  This tells the class which filter to use on the image. We also have
an observer attached to the `filter` property that actually does the filter
change logic when the property is updated. So from the server we just need to 
somehow change this client side `filter` property to trigger a UI change.

We do this by adding a setter method for the filter property in the Data model
interface (`WebDspModel` in WebDSP.java). We add the `setFilter(String filter)`
method to the interface.  We then create a `setFilter(String filter)` method
to the WebDSP.java public methods, which will access the setter we defined in the
WebDspModel interface.

```
// define setter in interface
// calling this will update the property on the client side
public interface WebDspModel extends TemplateModel {
    void setFilter(String filter);
}

```

```
// expose the interface method by creating a public setFilter method 
public void setFilter(Filter filter) {
    getModel().setFilter(filter.name());
}
```

### Using the Component ###

Now we have a Vaadin Flow component, WebDSP, that loads a webassembly module on
the client side and can be controlled by the server.  We can instantiate
our component and add it to any Vaadin layout. We can then set the filter
value which will update the client side.

```
VerticalLayout layout = new VerticalLayout();

// add component to layout
WebDSP dsp = new WebDSP();
layout.add(dsp);

// trigger UI change by setting filter value
dsp.setFilter("invert");
```

A more realistic example would be to assign different filters to buttons
(as we have in this application).  When a specific button is clicked,
the filter value gets updated and the client side reflects those changes.
*Note: Here we added an enum for controlling filter values (`WebDSP.Filter`),
thus changing `setFilter(String)` to `setFilter(WebDSP.Filter)`.*

```
VerticalLayout layout = new VerticalLayout();

WebDSP dsp = new WebDSP();
layout.add(dsp);

Button invertBtn = new Button("Invert");
invertBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.INVERT));
buttons.add(invertBtn);

Button dewdropBtn = new Button("Dewdrops");
dewdropBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.DEWDROPS));
buttons.add(dewdropBtn);
```

## Summary ##

In order to utilize webassembly modules in a Vaadin Flow application, we just
need to wrap the module in a component. This component consist of:

 - Client class:
   - Contains properties that can be updated and control the view of the UI.
 - Server class:
   - Contains logic and public methods for updating the client properties.
 - Data model interface:
   - Defines getters and setters for client properties so we can easily access
  them from the server side.