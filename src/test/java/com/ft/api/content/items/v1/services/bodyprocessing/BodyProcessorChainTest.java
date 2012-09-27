package com.ft.api.content.items.v1.services.bodyprocessing;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(value=MockitoJUnitRunner.class)
public class BodyProcessorChainTest {
	
	private BodyProcessorChain bodyProcessor;
	
	@Mock private BodyProcessor mockFirstBodyProcessor;
	@Mock private BodyProcessor mockSecondBodyProcessor;
	@Mock private BodyProcessor mockThirdBodyProcessor;
	@Mock private BodyProcessor mockFailingBodyProcessor;
	
	private static final String BODY = "body";
	
	@Before
	public void setup() {
		when(mockFirstBodyProcessor.process(BODY, null)).thenReturn(BODY);
		when(mockSecondBodyProcessor.process(BODY, null)).thenReturn(BODY);
		when(mockThirdBodyProcessor.process(BODY, null)).thenReturn(BODY);
		when(mockFailingBodyProcessor.process(BODY, null)).thenThrow(new BodyProcessingException("Error"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void cannotCreateWithNullBodyProcessorsList() {
		bodyProcessor = new BodyProcessorChain(null);
	}
	
	@Test
	public void bodyProcessorsAreCalledInSameOrderAsBodyProcessorsList() {
		bodyProcessor = new BodyProcessorChain(Arrays.asList(mockFirstBodyProcessor, mockSecondBodyProcessor, mockThirdBodyProcessor));
		InOrder inOrder = inOrder(mockFirstBodyProcessor, mockSecondBodyProcessor, mockThirdBodyProcessor);
		bodyProcessor.process(BODY, null);
		inOrder.verify(mockFirstBodyProcessor).process(BODY, null);
		inOrder.verify(mockSecondBodyProcessor).process(BODY, null);
		inOrder.verify(mockThirdBodyProcessor).process(BODY, null);
	}
	
	@Test(expected=BodyProcessingException.class)
	public void whenBodyProcessorThrowsExceptionItGetsPropogated() {
		//TODO - is this the expected behaviour??
		bodyProcessor = new BodyProcessorChain(Arrays.asList(mockFailingBodyProcessor));
		bodyProcessor.process(BODY, null);
	}
}
