package com.ft.api.content.items.v1.services.bodyprocessing.xml.eventhandlers;

public class UnexpectedElementStructureException extends RuntimeException {

    private static final long serialVersionUID = 5763609104620978952L;

    public UnexpectedElementStructureException(String message) {
        super(message);
    }

}
