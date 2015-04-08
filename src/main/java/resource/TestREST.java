package resource;

import entity.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.EmailService;
import session.RouteFacade;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import session.UserFacade;

/**
 *
 * @author Notreal
 */
@Path("test")
@RequestScoped
public class TestREST {

    @Inject
    private RouteFacade routeFacade;

    @Inject
    private EmailService emailService;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getHello() {
        return "<xml>aStore - hello</xml>";
    }

    @GET
    @Path("r")
    @Produces(MediaType.APPLICATION_XML)
    public Response testRedirect() {
        URI uri = URI.create(uriInfo.getBaseUri() + "test/u");
        return Response.temporaryRedirect(uri).build();
    }

    @GET
    @Path("m")
    public String testMail() throws Exception {
//        emailService.sendEmail("notreal13@gmail.com", "mail from wildfly", "bla-bla-bla!!!", Collections.EMPTY_MAP);
//        emailSessionBean.sendEmail("dmitry_kul@mail.ru", "mail from Glassfish", "bla-bla-bla!!!");
        return "Sending mail...";
    }

    @GET
    @Path("bad")
    public String testBad() {
        return "BadRequset";
    }

    private String getText2(String filename) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
        String result = "";
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = reader.readLine();
                }
                result = sb.toString();
            }
        }
        return result;
    }

    @GET
    @Path("nm")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewMail() {
        emailService.sendActivationEmail("notreal13@gmail.com", "new mail", "444");
        return "ok";
    }

    @Context
    UriInfo uriInfo;

    @Inject
    UserFacade userFacade;

    @GET
    @Path("a")
    @Produces(MediaType.APPLICATION_JSON)
    public String getActivationLink() {
        String link = uriInfo.getBaseUri() + "user/activation/" + userFacade.getToken("oleg@setco.ru");
        emailService.sendActivationEmail("notreal13@gmail.com", "new mail activation", link);
        return "send the mail ok";
    }

    @GET
    @Path("u")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser() {
        return userFacade.findByUsername("oleg@setco.ru");
    }

    private String getText(String filename) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(filename);
        String result = "";
        if (url != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(url.getFile()))) {
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = reader.readLine();
                }
                result = sb.toString();
            }
        }
        return result;
    }

    @GET
    @Path("i")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testJson() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", "qwerty")
                .build();

        return Response.status(Response.Status.OK).entity(jsonObject).build();
    }

    @GET
    @Path("j")
    @Produces(MediaType.APPLICATION_JSON)
    public String testJSONMarchaller() {
        JsonObject jsonObject = Json.createObjectBuilder()
                // simple pairs
                .add("firstName", "John2")
                .add("lastName", "Smith")
                .add("age", 25)
                // nested object
                .add("address", Json.createObjectBuilder()
                        .add("streetAddress", "21 2nd Street")
                        .add("city", "New York")
                        .add("state", "NY")
                        .add("postalCode", "10021"))
                // nested object array
                .add("phoneNumber", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("type", "home")
                                .add("number", "212 555-1234"))
                        .add(Json.createObjectBuilder()
                                .add("type", "fax")
                                .add("number", "646 555-4567")))
                .build();
        // 2. write the object to a string
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = Json.createWriter(stringWriter)) {
            writer.write(jsonObject);
        }
        String toString = stringWriter.toString();
//        return toString;
        return jsonObject.toString();

    }
}
