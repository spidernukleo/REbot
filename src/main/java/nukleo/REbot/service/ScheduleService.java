package nukleo.REbot.service;

import lombok.AllArgsConstructor;
import nukleo.REbot.repository.RedisRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduleService {

    private final RedisRepository redisRepository;

    @Scheduled(cron = "0 0 0 * * *", zone="Europe/Rome")
    public void resetKings(){
        redisRepository.clearAllKings();
    }
}
