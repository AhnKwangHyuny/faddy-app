//package faddy.global.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//
//@Aspect
//@Slf4j
//@Component
//public class ExceptionLoggingAOP {
//
//    @AfterThrowing(pointcut = "execution(* *(..))", throwing = "ex")
//    public void logException(JoinPoint joinPoint, Throwable ex) {
//
//        log.warn("예외 발생 위치: {}.{} | 에러 코드: {} | 에러 메세지: {} | 스레드: {} | 프로세스 ID: {}",
//                joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName(),
//                ex.getClass().getSimpleName(),
//                ex.getMessage(),
//                Thread.currentThread().getName(),
//                ProcessHandle.current().pid());
//    }
//
//}
