<h1>Application Quarkus Redis MySQL</h1>
<img src="https://upload.wikimedia.org/wikipedia/fr/thumb/6/6b/Redis_Logo.svg/701px-Redis_Logo.svg.png?20190421180155" height=160px>
<p>
Mise en place d'un cache distribué Redis avec MysQL
</p>
<h2>Mise en place</h2><br>
Ajout des dépendences<br>
<h2>Pom.xml</h2><br>
<p>
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-redis-cache</artifactId>
</dependency>
<h2>application.properties</h2><br>
#base de donnée redis<br>
quarkus.redis.hosts=redis://localhost:6379<br>
#expiration du cache<br>
quarkus.cache.redis.expire-after-write=20<br>
</p>
<h2>Controller ApiController.java</h2><br>
<p>
@Path("/cache")
public class ApiController {
 
    // BddManager
    @Inject
    BddManager bdd;
 
 
    /*
     * listepersonne
     */
    @CacheResult(cacheName = "listepersonne")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listepersonne")
    public String liste() {
        //Accès Base de données
        List<Personne> liste = bdd.getListPersonnes();
 
        if (liste.size() > 0) {
            JSONArray jsonArray = new Personne().totListeJSON(liste);
            return jsonArray.toString();
        }
        return getJSON("nb", "0").toString();
    }
 
    /*
     * personne
     */
    @CacheResult(cacheName = "personne")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/personne/{id}")
    public String get(int id) {
        //Accès Base de données
        Personne personne = bdd.getPersonneFromId(id);
        return personne.toJSON(personne).toString();
    }
 
 
    /*
     * invalidation du cache
     */
    @CacheInvalidate(cacheName = "listepersonne")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/invalidate")
    public String invalidate() {
        return getJSON("invalidate", String.valueOf(bdd.invalidate())).toString();
 
    }
 
    /*
     * getJSON
     */
    public JSONObject getJSON(String value, String key) {
        JSONObject obj = new JSONObject();
        obj.put(value, key);
        return obj;
    }
 
}
<p>

<h1>Compilation et Lancement</h1>
<p>
Clear: <b>mvn clean</b><br>
Mise à jour des dependences: <b>mvn dependency:resolve</b><br>
Compilation et Lancement: <b>mvn quarkus:dev</b><br>
Url du service: http://localhost:8080<br>  
</p>
