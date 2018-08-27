/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.openweather.challenge.openweatherapp.ui.addcity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by manuel on 27,August,2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AddCityViewModelTest {
    private static final String FAKE_STRING = "HELLO WORLD";
    @Mock
    Context mMockContext;

    @Test
    public void insertWeather() {
    }



    @Test
    public void readStringFromContext_LocalizedString() {
        // Given a mocked Context injected into the object under test...
        when(mMockContext.getString(R.string.hello_word))
                .thenReturn(FAKE_STRING);
        ClassUnderTest myObjectUnderTest = new ClassUnderTest(mMockContext);

        // ...when the string is returned from the object under test...
        String result = myObjectUnderTest.getHelloWorldString();

        // ...then the result should be the expected one.
        assertThat(result, is(FAKE_STRING));
    }
}