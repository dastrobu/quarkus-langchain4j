package io.quarkiverse.langchain4j.guardrails;

/**
 * Exception thrown when a guardrail validation fails.
 * <p>
 * This exception is not intended to be used in guardrail implementation. Instead, guardrail implementations should throw
 * {@link io.quarkiverse.langchain4j.guardrails.OutputGuardrail.ValidationException} when the validation fails.
 */
public class GuardrailException extends RuntimeException {
    public GuardrailException(String message, Throwable cause) {
        super(message, cause);
    }
}
