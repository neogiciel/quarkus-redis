package com.neogiciel.redis;

import io.quarkus.redis.datasource.RedisDataSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.inject.Singleton;

@Singleton
public class RedisManager{

    //Base de donnees Redis
    private RedisDataSource redisDS;
    
    //@ConfigProperty(name = "quarkus.cache.redis.expire-after-write")
    private String expireSecond;
    
        
    /*
     * Constructeur
     */
    public RedisManager(RedisDataSource redisDS) {
        this.redisDS = redisDS;
        //cmd = redisDS.string(String.class);
    }

    /*
     * setCache(String key, String value)
     */
    public void setCache(String key, String value) {
        redisDS.execute("SET",key,value).toString();
        //redisDS.execute("EXPIRE",key,expireSecond).toString();
    }


    /*
     * invalidateCache(String key) 
     */
    public void invalidateCache(String key) {
        redisDS.execute("DEL",key);
    }        
     
    /*
     * getCache(String key) 
     */
    public String getCache(String key) {
        return  redisDS.execute("GET",key).toString();
    }

    /*
     * getCache(String key) 
     */
    public boolean isCachAvailable(String key){
        if(getCache(key)!=null)
            return true;
        return false;
    }

}

