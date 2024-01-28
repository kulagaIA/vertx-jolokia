import com.simplesw.vertx.jolokia.JolokiaHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.properties.PropertyFileAuthentication;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.BodyHandler;

public class SampleVerticle extends AbstractVerticle {

    public static final int PORT = 8080;
    public static final Logger log = LoggerFactory.getLogger(SampleVerticle.class);

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", SampleVerticle.class.getName()});
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

      AuthenticationProvider authenticationProvider = PropertyFileAuthentication.create(vertx, "auth.properties");
      router.route("/api/*").handler(BasicAuthHandler.create(authenticationProvider));

        router.route().handler(BodyHandler.create());
        router.route("/api/jolokia/*").handler(JolokiaHandler.create());



        vertx.createHttpServer().requestHandler(router).listen(PORT);

        WebClient client = WebClient.create(vertx);
        vertx.setPeriodic(1000, t -> {
            client.get(PORT, "localhost", "/api/jolokia/read/java.lang:type=Memory/HeapMemoryUsage/used")
                    //.basicAuthentication("camel", "s3cr3t")
                    .send().onComplete(res -> {
                        JsonObject json = new JsonObject(res.result().body().toString());
                        log.info("HeapMemoryUsage/used {0}", json.getValue("value"));
                    });
        });

//client.request(HttpMethod.GET, PORT, "localhost", "/jolokia/read/java.lang:type=Memory/HeapMemoryUsage/used", res -> {
//        res.map(totalBuffer -> {
//    JsonObject json = new JsonObject(totalBuffer.toString());
//    log.info("HeapMemoryUsage/used {0}", json.getValue("value"));
    }
}
