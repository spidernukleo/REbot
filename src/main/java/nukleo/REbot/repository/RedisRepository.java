package nukleo.REbot.repository;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Service
public class RedisRepository {
    private final JedisPool pool = new JedisPool("localhost", 6379);

    public void setKing(Long chatid, String userFirstName){
        try (Jedis jedis = pool.getResource()) {
            jedis.set("king"+chatid,userFirstName);
        }
    }

    public String getKing(Long chatid){
        try (Jedis jedis = pool.getResource()) {
            return jedis.get("king"+chatid);
        }
    }

    public void clearAllKings(){
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys("king");
            if(!keys.isEmpty()){
                jedis.del(keys.toArray(new String[0]));
            }
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
