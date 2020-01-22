package application.rest.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import application.Visitor;
import application.store.CloudantVisitorStore;
import application.store.VisitorStore;
// import application.store.VisitorStoreFactory;
import java.util.ArrayList;
import java.util.List;


@ApplicationPath("api")
@Path("/visitors")
public class Example extends Application{

    //Our database store
    //VisitorStore store = VisitorStoreFactory.getInstance();
    VisitorStore store;

    public Example() {

        CloudantVisitorStore cvif = new CloudantVisitorStore();

        if (cvif.getDB() != null) {
            this.store = cvif;
        }

    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVisitors() {

        if (store == null) {
            return "[]";
        }

        List<String> names = new ArrayList<String>();
        for (Visitor doc : store.getAll()) {
            String name = doc.getName();
            if (name != null){
                names.add(name);
            }
        }
        return new Gson().toJson(names);
    }

    /**
     * Creates a new Visitor.
     *
     * REST API example:
     * <code>
     * POST http://localhost:9080/api/visitors
     * <code>
     * POST Body:
     * <code>
     * {
     *   "name":"Bob"
     * }
     * </code>
     * Response:
     * <code>
     * {
     *   "id":"123",
     *   "name":"Bob"
     * }
     * </code>
     * @param visitor The new Visitor to create.
     * @return The Visitor after it has been stored.  This will include a unique ID for the Visitor.
     */
    @POST
    @Produces("application/text")
    @Consumes("application/json")
    public String newToDo(Visitor visitor) {
        if(store == null) {
            return String.format("%s 씨, 데이터베이스에 연결되지 않았습니다", visitor.getName());
        }
        store.persist(visitor);
        return String.format("%s 씨: 응모되었습니다.!", visitor.getName());


    }

}
