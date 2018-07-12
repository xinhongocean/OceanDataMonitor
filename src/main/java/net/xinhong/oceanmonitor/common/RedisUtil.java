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
    private JedisCluster oceanJedisCluster;
    @Resource
    private JedisCluster weatherJedisCluster;

    Map<String, String> redisInfo;
    Map<String, Long> redissize;


    public Map<String, String> getRedisInfo(String machine) {
        updateRedisInfo(machine);
        return redisInfo;
    }

    public Map<String, Long> getRedisSize(String machine) {
        updateRedisInfo(machine);
        return redissize;
    }

    // 获取redis 服务器信息
    private void updateRedisInfo(String machine) {
        Map<String, JedisPool> nodesMap = null;
        if (machine.equals("107") || machine.equals("114"))
            nodesMap = weatherJedisCluster.getClusterNodes();
        else {
            nodesMap = oceanJedisCluster.getClusterNodes();
        }
        redisInfo = new HashMap<>();
        redissize = new HashMap<>();

        for (String str : nodesMap.keySet()) {
            JedisPool jedisPool =null;
            Jedis jedis=null;
            try {
                jedisPool = nodesMap.get(str);
                jedis = jedisPool.getResource();
                Client client = jedis.getClient();
                client.info();
                String info = client.getBulkReply();
                redisInfo.put(str, info);
            } catch (Exception e) {
                logger.error(e.getMessage());
                redisInfo.put(str, "fail");
            }finally {
                jedis.close();
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
