package development.team.ticketsystem.authservice.validation;

import development.team.ticketsystem.authservice.exception.InvalidEmailException;
import development.team.ticketsystem.authservice.exception.InvalidNameException;
import development.team.ticketsystem.authservice.exception.InvalidPasswordException;
import development.team.ticketsystem.authservice.validation.core.ValidatorChain;
import development.team.ticketsystem.authservice.validation.rules.ValidationRules;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private static final int MAX_LENGTH = 200;
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final Validator beanValidator;

    public UserValidator(Validator beanValidator) {
        this.beanValidator = beanValidator;
    }

    public String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    public String normalizeName(String name) {
        return name == null ? null : name.trim().replaceAll(" +", " ");
    }

    public void validateEmail(String email) {
        ValidatorChain.of(email)
                .rule(ValidationRules.notNull())
                .rule(ValidationRules.notBlank())
                .rule(ValidationRules.maxLength(MAX_LENGTH))
                .rule(ValidationRules.noWhitespace())
                .rule(ValidationRules.email(beanValidator))
                .validate(InvalidEmailException::new);
    }

    public void validatePassword(String password) {
        ValidatorChain.of(password)
                .rule(ValidationRules.notNull())
                .rule(ValidationRules.minLength(MIN_PASSWORD_LENGTH))
                .rule(ValidationRules.maxLength(MAX_LENGTH))
                .rule(ValidationRules.noWhitespace())
                .rule(ValidationRules.hasLetter())
                .rule(ValidationRules.hasDigit())
                .validate(InvalidPasswordException::new);
    }

    public void validateName(String name) {
        ValidatorChain.of(name)
                .rule(ValidationRules.notNull())
                .rule(ValidationRules.notBlank())
                .rule(ValidationRules.maxLength(MAX_LENGTH))
                .validate(InvalidNameException::new);
    }

    public void validateLoginPassword(String password) {
        ValidatorChain.of(password)
                .rule(ValidationRules.notNull())
                .rule(ValidationRules.notBlank())
                .rule(ValidationRules.maxLength(MAX_LENGTH))
                .validate(InvalidPasswordException::new);
    }

}