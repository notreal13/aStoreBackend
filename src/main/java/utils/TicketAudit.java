package utils;

import entity.Ticket;
import java.util.Date;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author Notreal
 */
public class TicketAudit {

    @PrePersist
    public void setLastUpdate(Ticket ticket) {
        ticket.setLastUpdate(new Date());
    }
    
    @PreUpdate
    public void setLastUpdate2(Ticket ticket) {
        ticket.setLastUpdate(new Date());
    }

}
