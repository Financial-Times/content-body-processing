package com.ft.api.content.items.v1.services.bodyprocessing;

import static org.springframework.util.Assert.notNull;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodyProcessorChain implements BodyProcessor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BodyProcessorChain.class);

	private List<BodyProcessor> bodyProcessors;
	
	public BodyProcessorChain(List<BodyProcessor> bodyProcessors) {
		notNull(bodyProcessors, "bodyProcessors cannot be null");
		this.bodyProcessors = bodyProcessors;
	}

	@Override
	public String process(String body, BodyProcessingContext bodyProcessingContext) throws BodyProcessingException {
		String processedBody = body;
        for(BodyProcessor bodyProcessor: bodyProcessors) {
			LOGGER.debug("body=[" + processedBody + "]");
			processedBody = bodyProcessor.process(processedBody, bodyProcessingContext);
		}
		return processedBody;
	}

}
