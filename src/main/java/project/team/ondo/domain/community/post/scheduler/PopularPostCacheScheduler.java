package project.team.ondo.domain.community.post.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.team.ondo.domain.community.post.cache.PopularPostCacheItem;
import project.team.ondo.domain.community.post.cache.PopularPostCacheRepository;
import project.team.ondo.domain.community.post.repository.PopularPostQueryRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularPostCacheScheduler {

    private final PopularPostQueryRepository popularPostQueryRepository;
    private final PopularPostCacheRepository popularPostCacheRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void refresh() {
        List<PopularPostCacheItem> items = popularPostQueryRepository.fetchTop10ByRecentLikes();
        popularPostCacheRepository.save(items);
        log.info("인기 게시물 캐시 갱신 완료: {}건", items.size());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        refresh();
    }
}
