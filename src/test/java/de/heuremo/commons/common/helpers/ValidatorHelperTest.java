package de.heuremo.commons.common.helpers;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidatorHelperTest {

    @Mock
    private TextInputEditText text_field;
    @Mock
    private TextInputEditText text_field2;
    @Mock
    private TextInputLayout text_field_layout;
    @Mock
    private Context context;


    @Test
    public void fieldShouldNotBeEmptyTest() {
        when(text_field.getText()).thenReturn(new MockEditable("Test"));
        assertTrue(ValidatorHelper.isFieldEmpty(text_field, text_field_layout, context));
    }

    @Test
    public void fieldShouldNotBeEmptyWith_EmptyInput() {
        when(text_field.getText()).thenReturn(new MockEditable(""));
        assertFalse(ValidatorHelper.isFieldEmpty(text_field, text_field_layout, context));
    }

    @Test
    public void  emailValidationTestWith_SuccessInput(){
        when(text_field.getText()).thenReturn(new MockEditable("Test@test.com"));
        assertTrue(ValidatorHelper.validateEmail(text_field, text_field_layout, context));
    }

    @Test
    public void  emailValidationTestWith_WrongInput(){
        when(text_field.getText()).thenReturn(new MockEditable("Testtest.com"));
        assertFalse(ValidatorHelper.validateEmail(text_field, text_field_layout, context));
    }

    @Test
    public void  emailValidationTestWith_EmptyInput(){
        when(text_field.getText()).thenReturn(new MockEditable("Testtest.com"));
        assertFalse(ValidatorHelper.validateEmail(text_field, text_field_layout, context));
    }

    @Test
    public void  validateMobileNumberTest_WithCountryCode(){
        when(text_field.getText()).thenReturn(new MockEditable("+9203336939044"));
        assertTrue(ValidatorHelper.validateMobileNumber(text_field, text_field_layout, context));
    }

    @Test
    public void  validateMobileNumberTest_WithOutCountryCode(){
        when(text_field.getText()).thenReturn(new MockEditable("03336999999"));
        assertTrue(ValidatorHelper.validateMobileNumber(text_field, text_field_layout, context));
    }

    @Test
    public void  validateMobileNumberTest_WithShortLength(){
        when(text_field.getText()).thenReturn(new MockEditable("0333"));
        assertFalse(ValidatorHelper.validateMobileNumber(text_field, text_field_layout, context));
    }

    @Test
    public void  validateMobileNumberTest_WithLongLength(){
        when(text_field.getText()).thenReturn(new MockEditable("03335676766767676767676767"));
        assertFalse(ValidatorHelper.validateMobileNumber(text_field, text_field_layout, context));
    }

    @Test
    public void  validateMobileNumberWithOutCountryCodeTest_WithWrongInput(){
        when(text_field.getText()).thenReturn(new MockEditable("+9203psjdnnh"));
        assertFalse(ValidatorHelper.validateMobileNumber(text_field, text_field_layout, context));
    }

    @Test
    public void  validateUserNameTest_WithValidLength(){
        when(text_field.getText()).thenReturn(new MockEditable("testUser"));
        assertTrue(ValidatorHelper.validateUserName(text_field, text_field_layout, context));
    }

    @Test
    public void  validateUserNameTest_WithLengthLessThan4(){
        when(text_field.getText()).thenReturn(new MockEditable("te"));
        assertFalse(ValidatorHelper.validateUserName(text_field, text_field_layout, context));
    }

    @Test
    public void  validatePasswordTest_WithValidLength(){
        when(text_field.getText()).thenReturn(new MockEditable("123456"));
        assertTrue(ValidatorHelper.validatePassword(text_field, text_field_layout, context));
    }

    @Test
    public void  validatePasswordTest_WithLengthLessThan6(){
        when(text_field.getText()).thenReturn(new MockEditable("1234"));
        assertFalse(ValidatorHelper.validatePassword(text_field, text_field_layout, context));
    }

    @Test
    public void  validatePassAndRepeatPasswordTest_ValidCase(){
        when(text_field.getText()).thenReturn(new MockEditable("123456"));
        when(text_field2.getText()).thenReturn(new MockEditable("123456"));
        assertTrue(ValidatorHelper.validatePassAndRepeatPassword(text_field, text_field2, text_field_layout, context));
    }

    @Test
    public void  validatePassAndRepeatPasswordTest_WithFalseCase(){
        when(text_field.getText()).thenReturn(new MockEditable("123456"));
        when(text_field2.getText()).thenReturn(new MockEditable("654321"));
        assertFalse(ValidatorHelper.validatePassAndRepeatPassword(text_field, text_field2, text_field_layout, context));
    }

}