package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import utils.TicketAudit;

/**
 *
 * @author Notreal
 */
@Entity
@EntityListeners({TicketAudit.class})
@Table(name = "ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
    @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Ticket t WHERE t.id = :id"),
    @NamedQuery(name = "Ticket.findByName", query = "SELECT t FROM Ticket t WHERE t.name = :name"),
    @NamedQuery(name = "Ticket.findByPrice", query = "SELECT t FROM Ticket t WHERE t.price = :price"),
    @NamedQuery(name = "Ticket.findByDescription", query = "SELECT t FROM Ticket t WHERE t.description = :description"),
    @NamedQuery(name = "Ticket.findByLastUpdate", query = "SELECT t FROM Ticket t WHERE t.lastUpdate = :lastUpdate"),
    @NamedQuery(name = "Ticket.findByDataLabel", query = "SELECT t FROM Ticket t WHERE t.dataLabel = :dataLabel"),
    @NamedQuery(name = "Ticket.findByCategoryId", query = "SELECT t FROM Ticket t WHERE t.category.id = :categoryId ORDER BY t.name")})
public class Ticket implements Serializable {
    private static final long serialVersionUID = -7055147737298759868L;    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRICE")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Size(max = 256)
    @Column(name = "DATA_LABEL")
    private String dataLabel;
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket")
    private List<OrderedTicket> orderedTicketList;

    public Ticket() {
    }

    public Ticket(String name, BigDecimal price, String description, Date lastUpdate) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.lastUpdate = lastUpdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @XmlTransient
    public List<OrderedTicket> getOrderedTicketList() {
        return orderedTicketList;
    }

    public void setOrderedTicketList(List<OrderedTicket> orderedTicketList) {
        this.orderedTicketList = orderedTicketList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ticket)) {
            return false;
        }
        Ticket other = (Ticket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + '}';
    }
}
