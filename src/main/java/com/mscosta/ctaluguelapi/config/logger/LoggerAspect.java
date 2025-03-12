package com.mscosta.ctaluguelapi.config.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Around("execution(* com.mscosta.ctaluguelapi.controller..*(..)) || execution(* com.mscosta.ctaluguelapi.service..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String nomeMetodo = joinPoint.getSignature().getName();
        String nomeClasse = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("[ENTRADA] {}.{}() - Args: {}", nomeClasse, nomeMetodo, joinPoint.getArgs());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        logger.info("[SUCESSO] {}.{}() - Tempo:{} ms - Retorno: {}", nomeClasse, nomeMetodo, end, result);
        return result;
    }

    public void logException(JoinPoint joinPoint, Throwable ex) {
        String nomeMetodo = joinPoint.getSignature().getName();
        String nomeClasse = joinPoint.getTarget().getClass().getSimpleName();
        logger.error("[ERRO] {}.{}() - Erro: {}", nomeClasse, nomeMetodo, ex.getMessage());
    }
}
