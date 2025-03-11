package faddy.views.service.scheduler;

import faddy.views.service.adapter.useCase.ViewCountSyncAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViewCountScheduler {

    private final ViewCountSyncAdapter viewCountSyncAdapter;

    @Scheduled(fixedRate =360000*5 ) // 1시간마다 실행 (밀리초 단위)
    public void updateViewCounts() {
        log.info("조회수 동기화 작업을 시작합니다.");
        viewCountSyncAdapter.updateViewCountsInDatabase();
        log.info("조회수 동기화 작업을 완료했습니다.");
    }

}
