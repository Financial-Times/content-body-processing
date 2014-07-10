package com.ft.content.bodyprocessing.transformer;

import com.ft.content.bodyprocessing.BodyProcessingContext;
import com.ft.content.bodyprocessing.BodyProcessor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class BodyProcessorTransformerTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void cannotCreateWithoutBodyProcessor() {
        expected.expect(IllegalArgumentException.class);
        new BodyProcessorTransformer(null);
    }

    @Test
    public void shouldUseBodyProcessorToTransform() throws TransformationException {
        BodyProcessor bodyProcessor = mock(BodyProcessor.class);
        BodyProcessorTransformer underTest = new BodyProcessorTransformer(bodyProcessor);
        when(bodyProcessor.process(any(String.class), any(BodyProcessingContext.class))).thenReturn("Transformed body");

        String transformedBody = underTest.transform("Input value");

        assertThat(transformedBody, is(equalTo("Transformed body")));
        verify(bodyProcessor).process(eq("Input value"), any(BodyProcessingContext.class));
    }
}
