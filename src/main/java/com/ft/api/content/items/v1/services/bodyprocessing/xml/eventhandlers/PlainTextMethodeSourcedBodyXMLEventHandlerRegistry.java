package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;


public class PlainTextMethodeSourcedBodyXMLEventHandlerRegistry extends XMLEventHandlerRegistry {

	public PlainTextMethodeSourcedBodyXMLEventHandlerRegistry() {
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
		//Methode specific xml tags
		super.registerStartElementEventHandler(new StripElementAndContentsXMLEventHandler(), 
				"byline", 
				"editor-choice", 
				"headline", 
				"inlineDwc", "interactive-chart", 
				"lead-body", "lead-text", "ln", 
				"photo", "photo-caption", "photo-group", 
				"promo-box", "promo-headline", "promo-image", "promo-intro", "promo-link", "promo-title", "promobox-body", 
				"pull-quote", "pull-quote-header", "pull-quote-text", 
				"readthrough", 
				"short-body", "skybox-body", "stories", "story", "strap",
				"videoObject", "videoPlayer", 
				"web-alt-picture", "web-background-news-header", "web-background-news-text", "web-background-news", 
				"web-inline-picture", "web-picture", 
				"web-pull-quote", "web-pull-quote-source", "web-pull-quote-text", 
				"web-skybox-picture", "web-subhead", 
				"web-table", "web-thumbnail", 
				"xref", "xrefs");
		super.registerEndElementEventHandler(new ReplaceWithStringXMLEventHandler("\n"), "p", "h1", "h2", "h3", "h4", "h5", "h6");
		// so that we don't get words running together when these tags are removed
		super.registerEndElementEventHandler(new ReplaceWithStringXMLEventHandler(" "), "br", "li", "ol", "ul");
		super.registerStartElementEventHandler(new ReplaceWithStringXMLEventHandler(" "), "ol", "ul");
		// characters (i.e. normal text) will be output
		super.registerCharactersEventHandler(new RetainXMLEventHandler());
		// comments will be removed
		super.registerCommentsEventHandler(new StripXMLEventHandler());
		// nbsp will be replaced with a space
		super.registerEntityReferenceEventHandler(new ReplaceWithStringXMLEventHandler(" "), "nbsp");
		
	}

}
