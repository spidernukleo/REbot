package nukleo.REbot.repository;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class RedisRepository {
    private final JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 2000, null, 2);

    public boolean setKingIfAbsent(Long chatId, String userFirstName, String cmd) {
        try (Jedis jedis = pool.getResource()) {
            String key = "king_" + cmd + chatId;
            long expireAt = ZonedDateTime.now(ZoneId.of("Europe/Rome"))
                    .toLocalDate()
                    .plusDays(1)
                    .atStartOfDay(ZoneId.of("Europe/Rome"))
                    .toEpochSecond();
            if (jedis.setnx(key, userFirstName) == 1) {
                jedis.expireAt(key, expireAt);
                return true;
            }
            return false;
        }
    }

    public String getKing(Long chatid, String cmd){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("king_" + cmd + chatid);
        }
    }

    public void removeKing(Long chatid, String cmd){
        try (Jedis jedis = pool.getResource()) {
            jedis.del("king_" + cmd + chatid);
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
