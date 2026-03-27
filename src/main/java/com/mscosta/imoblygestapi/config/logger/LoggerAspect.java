package com.mscosta.imoblygestapi.config.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerAspect.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String APPLICATION_PACKAGES_POINTCUT =
            "execution(* com.mscosta.imoblygestapi.controller..*(..)) || " +
                    "execution(* com.mscosta.imoblygestapi.service..*(..))";

    static {
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Around(APPLICATION_PACKAGES_POINTCUT)
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        final String methodSignature = buildMethodSignature(joinPoint);
        final String arguments = serializeArguments(joinPoint.getArgs());

        LOG.info("=> {}({})", methodSignature, arguments);

        final long startTime = System.currentTimeMillis();
        final Object result = joinPoint.proceed();
        final long elapsedTime = System.currentTimeMillis() - startTime;

        LOG.info("<= {} [{}ms] {}", methodSignature, elapsedTime, serializeArgument(result));
        return result;
    }

    @AfterThrowing(pointcut = APPLICATION_PACKAGES_POINTCUT, throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        final String methodSignature = buildMethodSignature(joinPoint);
        final String arguments = serializeArguments(joinPoint.getArgs());

        LOG.error("[ERRO] {} - Argumentos: {} - Erro: {}", methodSignature, arguments, ex.getMessage());
    }

    private String buildMethodSignature(JoinPoint joinPoint) {
        final String className = joinPoint.getTarget().getClass().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();
        return className + "#" + methodName;
    }

    private String serializeArguments(Object[] args) {
        return Arrays.stream(args)
                .map(this::serializeArgument)
                .toList()
                .toString();
    }

    private String serializeArgument(Object arg) {
        if (arg == null) {
            return "null";
        }

        if (isPrimitiveOrWrapperOrString(arg)) {
            return arg.toString();
        }

        try {
            return MAPPER.writeValueAsString(arg);
        } catch (Exception e) {
            return String.format("Erro ao serializar argumento: %s", e.getMessage());
        }
    }

    private boolean isPrimitiveOrWrapperOrString(Object arg) {
        return arg.getClass().isPrimitive()
                || arg instanceof Boolean
                || arg instanceof Number
                || arg instanceof String
                || arg instanceof Character;
    }
}
