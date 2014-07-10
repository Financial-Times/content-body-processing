package com.ft.content.bodyprocessing.transformer;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessor;
import com.ft.content.bodyprocessing.NoopBodyProcessingContext;
import static org.springframework.util.Assert.notNull;

public class BodyProcessorTransformer implements FieldTransformer {

    private final BodyProcessor bodyProcessor;
    private final BodyProcessingContext bodyProcessingContext = new NoopBodyProcessingContext();

    public BodyProcessorTransformer(BodyProcessor bodyProcessor) {
        notNull(bodyProcessor, "The argument: 'bodyProcessor' cannot be null.");
        this.bodyProcessor = bodyProcessor;
    }

    @Override
    public String transform(String fieldValue) throws TransformationException {
        return bodyProcessor.process(fieldValue, bodyProcessingContext);
    }

}
