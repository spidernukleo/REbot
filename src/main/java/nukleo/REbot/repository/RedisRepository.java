package nukleo.REbot.repository;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisRepository {
    private final JedisPool pool = new JedisPool("localhost", 6379);

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
