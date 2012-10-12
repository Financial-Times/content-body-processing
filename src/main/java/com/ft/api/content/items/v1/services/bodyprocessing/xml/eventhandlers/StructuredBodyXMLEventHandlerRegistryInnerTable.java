package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

public class StructuredBodyXMLEventHandlerRegistryInnerTable extends XMLEventHandlerRegistry {

	private Map<String, XMLEventHandler> startElementEventHandlersWrapper = new HashMap<String,XMLEventHandler>();
	private Map<String, XMLEventHandler> endElementEventHandlersWrapper = new HashMap<String,XMLEventHandler>();

	private StructuredBodyXMLEventHandlerRegistry handlerRegistry;

	public StructuredBodyXMLEventHandlerRegistryInnerTable(StructuredBodyXMLEventHandlerRegistry structuredBodyXMLEventHandlerRegistry) {
		this.handlerRegistry = structuredBodyXMLEventHandlerRegistry;

		this.registerStartAndEndElementEventHandlerWrapper(new RetainWithSpecificAttributesXMLEventHandler("colspan", "rowspan"),
				"table", "thead", "tbody",
				"tfoot", "tr", "caption",
				"td", "th");
	}

	@Override
	public XMLEventHandler getEventHandler(StartElement event) {
		XMLEventHandler eventHandler = this.startElementEventHandlersWrapper.get(event.asStartElement().getName().getLocalPart().toLowerCase());
		if (eventHandler == null) {
				eventHandler = handlerRegistry.getEventHandler(event.asStartElement());
		}
		return eventHandler;
	}

	@Override
	public XMLEventHandler getEventHandler(EndElement event) {
		XMLEventHandler eventHandler = this.endElementEventHandlersWrapper.get(event.asEndElement().getName().getLocalPart().toLowerCase());
		if (eventHandler == null) {
				eventHandler = handlerRegistry.getEventHandler(event.asEndElement());
		}
		return eventHandler;
	}

	public XMLEventHandler getEventHandler(Characters event) {
		return  handlerRegistry.getEventHandler(event);
	}

	private void registerStartAndEndElementEventHandlerWrapper(XMLEventHandler
				startEndElementEventHandler, String... names) {
		notNull(startEndElementEventHandler, "startEndElementEventHandler cannot be null");
		notNull(names, "names cannot be null");
		notEmpty(names, "names cannot be empty");

		for(String name: names) {
			this.startElementEventHandlersWrapper.put(name.toLowerCase(), startEndElementEventHandler);
			this.endElementEventHandlersWrapper.put(name.toLowerCase(), startEndElementEventHandler);
		}
	}
}
