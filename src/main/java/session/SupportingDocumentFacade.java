package session;

import entity.SupportingDocument;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Notreal
 */
@Stateless
public class SupportingDocumentFacade extends AbstractFacade<SupportingDocument> {
    
    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SupportingDocumentFacade() {
        super(SupportingDocument.class);
    }
    
}
