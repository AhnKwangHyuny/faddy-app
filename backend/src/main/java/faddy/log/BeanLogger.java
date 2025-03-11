package faddy.log;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class BeanLogger {

    private static final Logger logger = LoggerFactory.getLogger(BeanLogger.class);

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void logBeans() {
        // 빈 이름으로 빈을 조회합니다.
        Object bean = applicationContext.getBean("gmailMailSender");
        if (bean != null) {
            logger.info("Bean 'beanName' is registered: " + bean);
        } else {
            logger.warn("Bean 'beanName' is not registered");
        }
    }
}
