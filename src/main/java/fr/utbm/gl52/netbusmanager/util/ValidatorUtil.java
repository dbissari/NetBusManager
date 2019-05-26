/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.gl52.netbusmanager.util;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author bright
 */
public class ValidatorUtil {
    
    private static final ValidatorFactory validatorFactory;
    
    static {
        try {
            validatorFactory = Validation.buildDefaultValidatorFactory();
        } catch (Throwable ex) {
            System.err.println("Initial ValidatorFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * @return the validator
     */
    public static Validator getValidator() {
        return validatorFactory.getValidator();
    }
    
}
