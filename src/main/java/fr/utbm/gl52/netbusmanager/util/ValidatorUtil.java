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

    public static Validator getValidator() {
        return validatorFactory.getValidator();
    }
    
}
