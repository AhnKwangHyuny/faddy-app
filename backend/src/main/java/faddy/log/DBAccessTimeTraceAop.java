package faddy.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DBAccessTimeTraceAop {
    private static final Logger logger = LoggerFactory.getLogger(DBAccessTimeTraceAop.class);

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void DBAccessMethod() {};

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionMethod() {};

    @Around("DBAccessMethod() || transactionMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            logger.info("Execution of " + className + "." + methodName + " ended in " + elapsedTime + "ms");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument " + joinPoint.getArgs()[0] + " in " + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }

}
