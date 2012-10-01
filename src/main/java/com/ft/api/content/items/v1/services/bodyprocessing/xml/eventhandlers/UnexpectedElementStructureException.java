package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

import com.ft.api.utils.exception.ExpectedException;

public class UnexpectedElementStructureException extends ExpectedException {

    private static final long serialVersionUID = 5763609104620978952L;

    public UnexpectedElementStructureException(String message) {
        super(message);
    }

}
