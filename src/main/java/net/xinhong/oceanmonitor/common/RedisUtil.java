package net.xinhong.oceanmonitor.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@Component
public class RedisUtil {
    final static Logger logger = Logger.getLogger(RedisUtil.class);
    @Resource
    private JedisCluster jedisCluster;
    Map<String, String> redisInfo;
    Map<String, Long> redissize;


    public Map<String, String> getRedisInfo() {
        updateRedisInfo();
        return redisInfo;
    }

    public Map<String, Long> getRedisSize() {
        updateRedisInfo();
        return redissize;
    }

    // 获取redis 服务器信息
    private void updateRedisInfo() {
        Map<String, JedisPool> nodesMap = jedisCluster.getClusterNodes();
        redisInfo = new HashMap<>();
        redissize = new HashMap<>();

        for (String str : nodesMap.keySet()) {
            try {
                JedisPool jedisPool = nodesMap.get(str);
                Jedis jedis = jedisPool.getResource();
                Client client = jedis.getClient();
                client.info();
//                try {
////                    List<Long> sz=client.getIntegerMultiBulkReply();
////                    Long size = client.getIntegerReply();
////                    redissize.put(str, size);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                String info = client.getBulkReply();
                redisInfo.put(str, info);
            } catch (Exception e) {
                logger.error(e.getMessage());
                redisInfo.put(str, "fail");
            }
        }
    }

//    // 获取日志列表
//    public List<Slowlog> getLogs(long entries) {
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();
//            List<Slowlog> logList = jedis.slowlogGet(entries);
//            return logList;
//        } finally {
//            // 返还到连接池
//            jedis.close();
//        }
//    }

}
