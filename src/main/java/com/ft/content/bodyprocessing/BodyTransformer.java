package com.ft.content.bodyprocessing;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.api.ucm.model.v1.Body;

public interface BodyTransformer {
    Body transform(String body, BodyProcessingContext bodyProcessingContext);
}
