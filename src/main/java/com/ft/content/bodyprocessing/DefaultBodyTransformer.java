package com.ft.content.bodyprocessing;


import static org.apache.commons.lang.StringUtils.isBlank;

import com.ft.api.ucm.model.v1.Body;
import com.ft.api.ucm.model.v1.StringBasedBody;

public class DefaultBodyTransformer implements BodyTransformer {

	private BodyProcessor bodyProcessor;
    private String mediaType;
	
	public DefaultBodyTransformer(BodyProcessor bodyProcessor, String mediaType) {
		this.bodyProcessor = bodyProcessor;
        this.mediaType = mediaType;
	}

	@Override
	public Body transform(String body, BodyProcessingContext bodyProcessingContext) {
		if (isBlank(body)) {
			return new StringBasedBody(null, null);
		}
		String transformedBody = bodyProcessor.process(body, bodyProcessingContext);
		return new StringBasedBody(transformedBody, mediaType);
	}

}
