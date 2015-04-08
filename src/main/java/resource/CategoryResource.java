package resource;

import entity.Category;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import session.CategoryFacade;

/**
 * REST Web Service
 *
 * @author Notreal
 */
@Path("category")
public class CategoryResource {
    
    @Inject
    CategoryFacade categoryFacade;

    public CategoryResource() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Category> findAll() {
        return categoryFacade.findAll();
    }
}
