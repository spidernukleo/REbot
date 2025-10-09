package nukleo.REbot.repository;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class RedisRepository {
    private final JedisPool pool = new JedisPool("localhost", 6379);

    public void setKing(Long chatId, String userFirstName) {
        try (Jedis jedis = pool.getResource()) {
            String key = "king" + chatId;
            jedis.set(key, userFirstName);

            // Calculate seconds until next midnight
            ZoneId zone = ZoneId.of("Europe/Rome");
            ZonedDateTime now = ZonedDateTime.now(zone);
            ZonedDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(zone);
            long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();

            // Set expiration
            jedis.expire(key, (int) secondsUntilMidnight);
        }
    }

    public String getKing(Long chatid){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("king"+chatid);
        }
    }

    public boolean canExecute(Long chatId, long millis) {
        String key = "chat" + chatId;
        try (Jedis jedis = pool.getResource()) {
            String last = jedis.get(key);
            long now = System.currentTimeMillis();
            if (last != null && now - Long.parseLong(last) < millis) return false;
            jedis.set(key, String.valueOf(now));
            return true;
        }
    }
}
