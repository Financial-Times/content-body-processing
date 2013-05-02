package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;



public class StructuredWordPressSourcedBodyXMLEventHandlerRegistry extends XMLEventHandlerRegistry {



	public StructuredWordPressSourcedBodyXMLEventHandlerRegistry() {
		//default is to skip events - any start or end tags or entities not configured below will be excluded, as will comments
		super.registerDefaultEventHandler(new StripXMLEventHandler());
		//tags to include
		super.registerStartAndEndElementEventHandler(new RetainWithoutAttributesXMLEventHandler(),
				"h1","h2", "h3", "h4", "h5", "h6",
				"ol", "ul", "li",
				"p",
				"br", "strong", "em", "small", "sub", "sup",
				"itemBody"); // itemBody included as it will be a root node wrapping the body text so that the xml being written out is valid
		
		// to be retained with attributes
		super.registerStartElementEventHandler(new LinkTagXMLEventHandler(), "a");
		super.registerEndElementEventHandler(new LinkTagXMLEventHandler(), "a");
					
		// to be transformed
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("span", "class", "ft-underlined"), "u");
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("span", "class", "ft-bold"), "b");
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("span", "class", "ft-italic"), "i");
		
		//html5 tags to remove with all contents
		super.registerStartElementEventHandler(new StripElementAndContentsXMLEventHandler(), 
				"applet", "audio", 
				"base", "basefont", "button", 
				"canvas", "caption",  "col", "colgroup", "command", 
				"datalist", "del", "dir", 
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
				"script", "select", "source", "strike", "style", "table",
				"tbody", "td", "textarea", "tfoot", "th", "thead", "tr", "track",
				"video",
				"wbr");
		
		// TODO - replace this with the correct handling for tweets. It's here now because otherwise we get 
		// parts of stuff between the comments output
		// for embedded tweets, strip everything between the initial and final comments. Any other comments will just be removed.
		super.registerCommentsEventHandler(new StripEmbeddedTweetXMLEventHandler());
		// characters (i.e. normal text) will be output
		super.registerCharactersEventHandler(new RetainXMLEventHandler());
		// specific entity references should be retained
		super.registerEntityReferenceEventHandler(new RetainXMLEventHandler(), "nbsp");
		
	}

}
