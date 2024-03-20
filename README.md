<h1>Application Quarkus Redis MySQL</h1>
<img src="https://upload.wikimedia.org/wikipedia/fr/thumb/6/6b/Redis_Logo.svg/701px-Redis_Logo.svg.png?20190421180155" height=160px>
<p>
Mise en place d'un cache distribué Redis avec MysQL
</p>
<h2>Mise en place</h2><br>
Ajout des dépendences<br>
<h2>Pom.xml</h2><br>
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-redis-cache</artifactId>
</dependency>
<h2>application.properties</h2><br>
#base de donnée redis
quarkus.redis.hosts=redis://localhost:6379
#expiration du cache
quarkus.cache.redis.expire-after-write=20

<h2>ApiController.java</h2><br>

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
Application permettant de gérer un cache distribué avec Redis
Utilisation de la base de données MySQL
</p>
<h2>Application Quarkus Redis MySQL</h2>
## Utilisation des anonations  


<h1>Application PHP K8s</h1>
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/PHP-logo.svg/2560px-PHP-logo.svg.png" height=160px>
<p>
Exemple de déploeiement d'un site PHP 8 et MySQL dans un cluster K8s<br>
</p>
<h2>Le site PHP</h2>
<p>
<li>Le site est disponible au sein du dossier src</li><br>
<li>La confifguration apache et php est situé sous dossier conf</li><br>
<li>Les extensions PHP utilisés sont mysqli et pdo_mysql</li><br>
</p>
<h2>Déploiement K8S</h2>
<p>
Le déploiement sous K8S se fait par l'intermédiaire d'un Dockerfile<br>
Le déploiement global dans le cluster s'effectue via le fichier kube.yml
</p>

