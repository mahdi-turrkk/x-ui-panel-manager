package online.gixmetir.xuipanelmanagerbackend.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* online.gixmetir.xuipanelmanagerbackend.services.app..*(..)) || execution(* online.gixmetir.xuipanelmanagerbackend.services.xui..*(..)) )")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Starting method: " + joinPoint.getSignature().getName() + "\t " + "In Class: " + joinPoint.getTarget().getClass().getName());

        Object output = joinPoint.proceed();
        logger.info("Finished method: " + joinPoint.getSignature().getName() + "\t " + "In Class: " + joinPoint.getTarget().getClass().getName());

        return output;
    }
}
