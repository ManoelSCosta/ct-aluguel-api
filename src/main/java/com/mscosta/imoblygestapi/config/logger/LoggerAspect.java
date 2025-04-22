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
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Around("execution(* com.mscosta.ctaluguelapi.controller..*(..)) || execution(* com.mscosta.ctaluguelapi.service..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String nomeMetodo = joinPoint.getSignature().getName();
        String nomeClasse = joinPoint.getTarget().getClass().getSimpleName();

        String argsSerializado = Arrays.stream(joinPoint.getArgs())
                        .map(this::serializarArgumento)
                                .toList()
                                        .toString();

        logger.info("=> {}#{}({})", nomeClasse, nomeMetodo, argsSerializado);


        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        String resultSerializado = serializarArgumento(result);
        logger.info("<= {}#{} [{}ms] {}", nomeClasse, nomeMetodo, (end - start), resultSerializado);
        return result;
    }

    private String serializarArgumento(Object arg) {
        if(arg == null) {
            return "null";
        }

        if(arg.getClass().isPrimitive() || isWrapperOrString(arg)){
            return arg.toString();
        }

        try {
            return mapper.writeValueAsString(arg);
        } catch (Exception e) {
            return String.format("Erro ao serializar argumento: %s", e.getMessage());
        }
    }

    private boolean isWrapperOrString(Object arg) {
        return arg instanceof Boolean ||
                arg instanceof Number ||
                arg instanceof String ||
                arg instanceof Character;
    }

    @AfterThrowing(
            pointcut = "execution(* com.mscosta.ctaluguelapi.controller..*(..)) || execution(* com.mscosta.ctaluguelapi.service..*(..))",
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String nomeMetodo = joinPoint.getSignature().getName();
        String nomeClasse = joinPoint.getTarget().getClass().getSimpleName();
        String argsSerializado = Arrays.stream(joinPoint.getArgs())
                .map(this::serializarArgumento)
                .toList()
                .toString();

        logger.error("[ERRO] {}.{}() - Argumentos: {} - Erro: {}", nomeClasse, nomeMetodo, argsSerializado, ex.getMessage());
    }
}
