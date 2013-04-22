package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;



public class PlainTextWordPressSourcedBodyXMLEventHandlerRegistry extends XMLEventHandlerRegistry {
	
	public PlainTextWordPressSourcedBodyXMLEventHandlerRegistry() {
		//default is to skip events - any start or end tags not configured below will be excluded, as will comments
		super.registerDefaultEventHandler(new StripXMLEventHandler());
		//html5 tags
		super.registerStartElementEventHandler(new StripElementAndContentsXMLEventHandler(), 
				"applet", "audio", 
				"base", "basefont", "button", 
				"canvas", "caption", "col", "colgroup", 
				"command", "datalist", "del", "dir", 
				"embed", 
				"fieldset", "form", "frame", "frameset", 
				"head", 
				"iframe", "input", 
				"keygen", 
				"label", "legend", "link", 
				"map", "menu", "meta", 
				"nav", "noframes", "noscript", 
				"object", "optgroup", 
				"option", "output", 
				"param", "progress", 
				"rp", "rt", "ruby", "s",
				"script", "select", "source", "strike", "style", 
				"table", "tbody", "td", "textarea", "tfoot", "th", "thead", "tr", "track", 
				"video", 
				"wbr");
		super.registerEndElementEventHandler(new ReplaceWithStringXMLEventHandler("\n"), "p", "h1", "h2", "h3", "h4", "h5", "h6");
		// so that we don't get words running together when these tags are removed
		super.registerEndElementEventHandler(new ReplaceWithStringXMLEventHandler(" "), "br", "li", "ol", "ul");
		super.registerStartElementEventHandler(new ReplaceWithStringXMLEventHandler(" "), "ol", "ul");
		super.registerCharactersEventHandler(new RetainXMLEventHandler());
		// for embedded tweets, strip everything between the initial and final comments. Any other comments will just be removed.
		super.registerCommentsEventHandler(new StripEmbeddedTweetXMLEventHandler());
		// nbsp will be replaced with a space
		super.registerEntityReferenceEventHandler(new ReplaceWithStringXMLEventHandler(" "), "nbsp");
		
	}

}
