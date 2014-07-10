package com.ft.content.bodyprocessing.transformer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransformationHelperTest {

	private TransformationHelper tobeTested;
	
	@Mock
	private FieldTransformer firstTransformer;
	@Mock
	private FieldTransformer secondTransformer;
	
	@Before
	public void setUp() throws Exception {
		tobeTested = new TransformationHelper();
	}
	
	@After
	public void tearDown() throws Exception{
		tobeTested = null;
	}
	
	@Test
	public void testApplyTransformer() throws Exception{
		//given
		List<FieldTransformer> fieldTransformers = new ArrayList<FieldTransformer>();
		fieldTransformers.add(firstTransformer);
		fieldTransformers.add(secondTransformer);
		
		//when
		tobeTested.applyTransformers("test", fieldTransformers);
		
		//then
		verify(firstTransformer, times(1)).transform(anyString());
		verify(secondTransformer, times(1)).transform(anyString());
	}
	
	@Test(expected=TransformationException.class)
	public void testTransformationException() throws Exception{
		//given
		List<FieldTransformer> fieldTransformers = new ArrayList<FieldTransformer>();
		fieldTransformers.add(firstTransformer);
		fieldTransformers.add(secondTransformer);
		
		when(firstTransformer.transform(anyString())).thenThrow(new TransformationException());
		
		//when
		tobeTested.applyTransformers("test", fieldTransformers);
	}
}
