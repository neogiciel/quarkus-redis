package com.neogiciel;


import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;

import com.neogiciel.model.Personne;
import com.neogiciel.model.Service;
import com.neogiciel.model.ServicePersonne;
import com.neogiciel.redis.RedisManager;
import com.neogiciel.service.BddManager;
import com.neogiciel.util.Trace;


@Path("/")
public class ApiController {

    @ConfigProperty(name = "application.version") 
    String applicationVersion;

    //Metriques
    @Inject
    MeterRegistry registry;

    //BddManager
    @Inject
    BddManager bdd; 

    //RedisManager
    @Inject
    RedisManager redisBdd; 
    

    /*
     * test
     */
    @CacheResult(cacheName = "test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/test")
    public String test() {
        Trace.info("Applel REST : /api/test");        
        String response = getJSON("test", String.valueOf(bdd.test())).toString();
        //redisBdd.setCache("test",response);
        return response;
     }

    /*
     * invalide le cache
     */
     @CacheInvalidate(cacheName = "listepersonne")
     @GET
     @Produces(MediaType.APPLICATION_JSON)
     @Path("/api/invalidate")
     public String invalidate() {
         Trace.info("Applel REST : /api/invalidate");        
         //redisBdd.invalidateCache("listepersonne");
         return getJSON("invalidate", String.valueOf(bdd.invalidate())).toString();
 
      }
 
     /*
     * redis avec cache gere a la main
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/redismanager")
    public String redisAvecCacheManager() {
        Trace.info("Applel REST : /api/redis");        
        if(redisBdd.isCachAvailable("redis") == true){
            Trace.info("Applel REST : le cache existe");        
            Trace.info("Applel REST : cache = "+redisBdd.getCache("redis"));        
            return redisBdd.getCache("redis");
        }else{
            Trace.info("Applel REST : le cache n existe pas");        
            String response = getJSON("redis", String.valueOf(bdd.redis())).toString();
            redisBdd.setCache("redis",response);
            Trace.info("Applel REST : cache = "+response);        
            return response;
        }

    }
    /*
     * redis 
     */
    @CacheResult(cacheName = "redis")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/redis")
    public String redis() {
        Trace.info("Applel REST : /api/redis");        
        String response = getJSON("redis", String.valueOf(bdd.redis())).toString();
        //redisBdd.setCache("redis",response);
        return response;
     }

    /*
     * api
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api")
    public String api() {
        Trace.info("Applel REST : API");        
        registry.counter("api", Tags.of("name", "api")).increment();
        return getJSON("api", "api quarkus").toString();
    }

    /*
     * liste 
     */
    @CacheResult(cacheName = "listepersonne")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/liste")
    public String liste() {
        Trace.info("Applel REST : /api/liste");        
        registry.counter("api liste", Tags.of("name", "liste")).increment();
        List<Personne> liste = bdd.getListPersonnes();

        if(liste.size()> 0){
            JSONArray jsonArray = new Personne().totListeJSON(liste);
            //redisBdd.setCache("listepersonne",jsonArray.toString());
            return jsonArray.toString();
        }
        return getJSON("nb", "0").toString();
     }

    /*
     * get 
     */
    @CacheResult(cacheName = "personne")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/get/{id}")
    public String get(int id) {
       Trace.info("Applel REST : /api/get/{"+id+")");        
       registry.counter("api get", Tags.of("name", "get = "+id)).increment();
       Personne personne = bdd.getPersonneFromId(id);
       redisBdd.setCache("personne",personne.toJSON(personne).toString());
       return personne.toJSON(personne).toString();
    }

    /*
     * add
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/add")
    public String add() {
        Trace.info("Applel REST : /api/add");        
        registry.counter("api add", Tags.of("name", "add ")).increment();
        Long id = bdd.addPersonneSQL(new Personne("toto1","toto1",26));
        return getJSON("add", "ajoute avec succes = "+id).toString();
    }

    /*
     * delete
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/delete/{id}")
    public String delete(int id) {
        Trace.info("Applel REST : /api/delete{"+id+"}");        
        registry.counter("api delete", Tags.of("name", "delete"+id)).increment();

        bdd.deletePersonneSQL(id);

        return getJSON("delete", "supprime avec succes = "+id).toString();

    }

    /*
     * update
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/update/{id}")
    public String update(int id) {
        Trace.info("Applel REST : /api/delete{"+id+"}");        
        registry.counter("api update", Tags.of("name", "update"+id)).increment();

        Personne personne = new Personne("cyril","despres",44);
        personne.id = id;
        bdd.updatePersonneSQL(personne);


        return getJSON("update", "modifie avec succes = "+id).toString();

    }


    /*
     * version
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/version")
    public String version() {
        Trace.info("Applel REST : /api/version");        
        return getJSON("version",applicationVersion).toString();

    }

    
    /*
     * service liste 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/service/liste")
    public String serviceliste() {
        Trace.info("Applel REST : /api/service/liste");        
        registry.counter("api service liste", Tags.of("name", "liste")).increment();
        List<Service> liste = bdd.getListServices();
        JSONArray jsonArray = new Service().totListeJSON(liste);
        return jsonArray.toString();
        
     }

    /*
     * listepersonnnefromservice 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/api/service/listepersonnnefromservice/{idservice}")
    public String listepersonnnefromservice(int idservice) {
        Trace.info("Applel REST : /api/service/listepersonnnefromservice = "+idservice);        
        registry.counter("api listepersonnnefromservice", Tags.of("name", "listepersonnnefromservice")).increment();
               
        List<ServicePersonne> liste = bdd.getListPersonneFromServices(idservice);

        if(liste.size()> 0){
            JSONArray jsonArray = new ServicePersonne().totListeJSON(liste);
            return jsonArray.toString();
        }
        return getJSON("nb", "0").toString();
        
     }

     /*
      * getJSON
      */
     public JSONObject getJSON(String value,String key){
        JSONObject obj = new JSONObject();
        obj.put(value, key);
        return obj;
     }



}
