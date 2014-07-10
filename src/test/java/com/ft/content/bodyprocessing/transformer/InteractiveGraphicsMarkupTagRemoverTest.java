package com.ft.content.bodyprocessing.transformer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InteractiveGraphicsMarkupTagRemoverTest {
    
    private final InteractiveGraphicsMarkupTagRemover markupTagRemover = new InteractiveGraphicsMarkupTagRemover();
    
    @Test
    public void testTransfromEmtyFieldValue() throws Exception {
        assertNull(markupTagRemover.transform(null));
        assertNull(markupTagRemover.transform(""));
    }
    
    @Test
    public void testTransfom() throws Exception {
        String initialStoryBody = "Lorem Ipsum <div class=\"interactive-comp\">is </div>simply dummy text of the " +
        		"printing\nand <div>typesetting industry.</div>";
        String expectedStoryBody = "Lorem Ipsum simply dummy text of the printing\nand <div>typesetting industry.</div>";
        assertEquals(expectedStoryBody, markupTagRemover.transform(initialStoryBody));
    }
}
