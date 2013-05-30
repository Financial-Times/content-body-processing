package com.ft.content.bodyprocessing.xml.eventhandlers;

import com.ft.content.bodyprocessing.xml.StAXTransformingBodyProcessor;


public class StructuredBodyXMLEventHandlerRegistry extends XMLEventHandlerRegistry {



	public StructuredBodyXMLEventHandlerRegistry() {
		//default is to skip events - any start or end tags not configured below will be excluded, as will comments
		super.registerDefaultEventHandler(new StripXMLEventHandler());
		//tags to include
		super.registerStartAndEndElementEventHandler(new RetainWithoutAttributesXMLEventHandler(),
				"h1","h2", "h3", "h4", "h5", "h6",
				"ol", "ul", "li",
				"p",
				"br", "strong", "em", "small", "sub", "sup",
				"itemBody"); // itemBody included as it will be a root node wrapping the body text so that the xml being written out is valid
		
		// to be retained with attributes
		super.registerStartElementEventHandler(new SlideshowXMLEventHandler(new SlideshowXMLParser(),  new AsideElementWriter(), new LinkTagXMLEventHandler()), "a");
		super.registerEndElementEventHandler(new LinkTagXMLEventHandler(), "a");
		
		super.registerStartAndEndElementEventHandler(new VideoXMLEventHandler(new VideoXMLParser(), new AsideElementWriter(), new StripElementAndContentsXMLEventHandler()), "videoPlayer");
		super.registerStartAndEndElementEventHandler(new ImageRetainWithSpecificAttributesXMLEventHandler(), "img");
		super.registerStartAndEndElementEventHandler(new PullQuoteXMLEventHandler(new PullQuoteXMLParser(new StAXTransformingBodyProcessor(this)), new AsideElementWriter()), "web-pull-quote");
		super.registerStartAndEndElementEventHandler(new InteractiveGraphicXMLEventHandler(new InteractiveGraphicXMLParser(), new AsideElementWriter()), "plainHtml");
		super.registerStartAndEndElementEventHandler(new BackgroundNewsXMLEventHandler(new BackgroundNewsXMLParser(new StAXTransformingBodyProcessor(this)), new AsideElementWriter()), "web-background-news");
		super.registerStartAndEndElementEventHandler(new DataTableXMLEventHandler(new DataTableXMLParser(new StAXTransformingBodyProcessor(new StructuredBodyXMLEventHandlerRegistryInnerTable(this))), new AsideElementWriter(), new StripElementAndContentsXMLEventHandler()), "table");
		super.registerStartAndEndElementEventHandler(new PromoBoxXMLEventHandler(new PromoBoxXMLParser(new StAXTransformingBodyProcessor(this)), new AsideElementWriter()), "promo-box");
				
		// to be transformed
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("span", "class", "ft-underlined"), "u");
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("span", "class", "ft-bold"), "b");
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("span", "class", "ft-italic"), "i");
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("h3",   "class", "ft-subhead"),    "subhead");
		super.registerStartAndEndElementEventHandler(new SimpleTransformTagXmlEventHandler("p",    "class", "ft-tagline"),    "tagline");
		super.registerStartAndEndElementEventHandler(new ImageTransformXMLEventHandler("img", "class", "ft-web-inline-picture"), "web-inline-picture");
		
		//html5 tags to remove with all contents
		super.registerStartElementEventHandler(new StripElementAndContentsXMLEventHandler(), 
				"applet", "audio", 
				"base", "basefont", "button", 
				"canvas", "col", "colgroup", "command", 
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
				"rp", "rt", "ruby", 
				"script", "select", "source", "style", 
				"tbody", "td", "textarea", "tfoot", "th", "thead", "tr", "track",
				"video",
				"wbr");
		
		super.registerStartElementEventHandler(new InlineMediaXMLEventHandler(new SlideshowXMLParser("inlineDwc"),  new AsideElementWriter(), new StripElementAndContentsXMLEventHandler()), "inlineDwc");
		
		//xml elements to remove with all contents
		super.registerStartElementEventHandler(new StripElementAndContentsXMLEventHandler(), 
				"byline", 
				"editor-choice", 
				"headline", 
				"interactive-chart", 
				"lead-body", "lead-text", "ln", 
				"photo", "photo-caption", "photo-group", 
				"promo-headline", "promo-image", "promo-intro", "promo-link", "promo-title", "promobox-body", 
				"pull-quote", "pull-quote-header", "pull-quote-text", 
				"readthrough", 
				"short-body", "skybox-body", "stories", "story", "strap",
				"videoObject",
				"web-alt-picture",
				"web-picture", "web-pull-quote-source", "web-pull-quote-text",
				"web-skybox-picture", "web-subhead", 
				 "web-thumbnail",
				"xref", "xrefs");
		// characters (i.e. normal text) will be output
		super.registerCharactersEventHandler(new RetainXMLEventHandler());
		
	}

}
