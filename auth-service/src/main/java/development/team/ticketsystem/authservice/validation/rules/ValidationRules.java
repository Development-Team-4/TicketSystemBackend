package development.team.ticketsystem.authservice.validation.rules;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ValidationRules {

    private static final Pattern MULTIPLE_SPACES = Pattern.compile(" {2,}");
    private static final Pattern HAS_LETTER = Pattern.compile(".*[A-Za-z].*");
    private static final Pattern HAS_DIGIT = Pattern.compile(".*\\d.*");

    public static <T> Predicate<T> notNull() {
        return value -> value != null;
    }

    public static Predicate<String> maxLength(int max) {
        return value -> value.length() <= max;
    }

    public static Predicate<String> minLength(int min) {
        return value -> value.length() >= min;
    }

    public static Predicate<String> noLeadingTrailingSpaces() {
        return value -> value.equals(value.trim());
    }

    public static Predicate<String> noWhitespace() {
        return value -> value.chars().noneMatch(Character::isWhitespace);
    }

    public static Predicate<String> notBlank() {
        return value -> !value.trim().isEmpty();
    }

    public static Predicate<String> noMultipleSpaces() {
        return value -> !MULTIPLE_SPACES.matcher(value).find();
    }

    public static Predicate<String> hasLetter() {
        return value -> HAS_LETTER.matcher(value).matches();
    }

    public static Predicate<String> hasDigit() {
        return value -> HAS_DIGIT.matcher(value).matches();
    }

    public static Predicate<String> email(Validator validator) {
        return value -> {
            Set<ConstraintViolation<EmailHolder>> violations =
                    validator.validateValue(EmailHolder.class, "value", value);
            return violations.isEmpty();
        };
    }

    private static class EmailHolder {
        @Email
        String value;
    }
}