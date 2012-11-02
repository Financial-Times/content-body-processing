package com.ft.api.content.items.v1.services;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.api.ucm.model.v1.Body;

public interface BodyTransformer {
    Body transform(String body, BodyProcessingContext bodyProcessingContext);
}
