package com.warthur.netty.obdserver.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * Redis连接池
 * 
 * @author Karl
 *
 */
public final class JedisUtils {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	private JedisUtils() {}
	
    private static final int DEFAULT_DB_INDEX = 0;
    private static JedisPool codisPool = null;

    private static String configFilename = "conf/codis.properties";
	private static Properties prop = FileUtils.readPropertiesFile(configFilename);

    static {
    	try {
	        JedisPoolConfig codisConfig = new JedisPoolConfig();
	        codisConfig.setMaxTotal(Integer.valueOf(prop.getProperty("codis.pool.maxTotal")));
	        codisConfig.setMaxIdle(Integer.valueOf(prop.getProperty("codis.pool.maxIdle")));
	        codisConfig.setMinIdle(Integer.valueOf(prop.getProperty("codis.pool.minIdle")));
	        codisConfig.setMaxWaitMillis(Integer.valueOf(prop.getProperty("codis.pool.maxWait")));
	        codisConfig.setTestOnBorrow("true".equalsIgnoreCase(prop.getProperty("codis.pool.testOnBorrow")));
	        codisConfig.setTestOnReturn("true".equalsIgnoreCase(prop.getProperty("codis.pool.testOnReturn")));
	        codisConfig.setTestWhileIdle("true".equalsIgnoreCase(prop.getProperty("codis.pool.testWhileIdle")));
	
	        codisPool = new JedisPool(codisConfig,
	        		prop.getProperty("codis.ip"),
	                Integer.valueOf(prop.getProperty("codis.port")),
	                Integer.valueOf(prop.getProperty("codis.timeout")),
	                prop.getProperty("codis.password"));
    	} catch (Exception e) {
    		logger.error("Initialize codis pool failed!", e);
		}
    }

    /**
     * Get a jedis from codis pool.<br>
     * Recommend usage: try-with-resouces<br>
     * @param dbIndex
     * @return
     */
    public static Jedis getCodis(int... dbIndex) {
        Jedis jedis = null;
        if (codisPool != null) {
            jedis = codisPool.getResource();
            if (dbIndex == null || dbIndex.length == 0) {
                jedis.select(DEFAULT_DB_INDEX);
            } else {
                jedis.select(dbIndex[0]);
            }
            return jedis;
        } else {
            throw new RuntimeException("Codis pool has not been initialized!");
        }
    }

 
}
