package com.ft.api.content.items.v1.services;

import com.ft.api.content.items.v1.services.bodyprocessing.BodyProcessingContext;
import com.ft.unifiedContentModel.model.Body;

public interface BodyTransformer {
    Body transform(String body, BodyProcessingContext bodyProcessingContext);
}
