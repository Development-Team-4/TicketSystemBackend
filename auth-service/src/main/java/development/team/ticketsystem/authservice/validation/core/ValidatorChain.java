package development.team.ticketsystem.authservice.validation.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ValidatorChain<T> {

    private final T value;
    private final List<Predicate<T>> rules = new ArrayList<>();

    private ValidatorChain(T value) {
        this.value = value;
    }

    public static <T> ValidatorChain<T> of(T value) {
        return new ValidatorChain<>(value);
    }

    public ValidatorChain<T> rule(Predicate<T> rule) {
        rules.add(rule);
        return this;
    }

    public void validate(Supplier<? extends RuntimeException> exceptionSupplier) {
        for (Predicate<T> rule : rules) {
            if (!rule.test(value)) {
                throw exceptionSupplier.get();
            }
        }
    }
}