package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Notreal
 */
@Entity
@Table(name = "ordered_ticket")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderedTicket.findAll", query = "SELECT o FROM OrderedTicket o"),
    @NamedQuery(name = "OrderedTicket.findById", query = "SELECT o FROM OrderedTicket o WHERE o.id = :id"),
    @NamedQuery(name = "OrderedTicket.findBySupportingDocumentData",
            query = "SELECT o FROM OrderedTicket o WHERE o.supportingDocumentData = :supportingDocumentData"),
    @NamedQuery(name = "OrderedTicket.findByFirstName", query = "SELECT o FROM OrderedTicket o WHERE o.firstName = :firstName"),
    @NamedQuery(name = "OrderedTicket.findByLastName", query = "SELECT o FROM OrderedTicket o WHERE o.lastName = :lastName"),
    @NamedQuery(name = "OrderedTicket.findByMiddleName", query = "SELECT o FROM OrderedTicket o WHERE o.middleName = :middleName"),
    @NamedQuery(name = "OrderedTicket.findByDOB", query = "SELECT o FROM OrderedTicket o WHERE o.dob = :dob"),
    @NamedQuery(name = "OrderedTicket.findByLicensePlate", query = "SELECT o FROM OrderedTicket o WHERE o.licensePlate = :licensePlate")
})
public class OrderedTicket implements Serializable {
    private static final long serialVersionUID = -3955506709924344607L;    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "SUPPORTING_DOCUMENT_DATA")
    private String supportingDocumentData;
    @Size(max = 30)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(max = 40)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Size(max = 30)
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @JoinColumn(name = "SUPPORTING_DOCUMENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SupportingDocument supportingDocument;
    @JoinColumn(name = "CUSTOMER_ORDER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CustomerOrder customerOrder;
    @JoinColumn(name = "TICKET_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Ticket ticket;
    @Transient
    private String dobString;
    @Size(max = 10)
    @Column(name="LICENSE_PLATE")
    private String licensePlate;
    

    public OrderedTicket() {
    }

    @XmlTransient
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSupportingDocumentData() {
        return supportingDocumentData;
    }

    public void setSupportingDocumentData(String supportingDocumentData) {
        this.supportingDocumentData = supportingDocumentData;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getDobString() {
        return dobString;
    }

    public void setDobString(String dobString) {
        this.dobString = dobString;
    }

    public SupportingDocument getSupportingDocument() {
        return supportingDocument;
    }

    public void setSupportingDocument(SupportingDocument supportingDocument) {
        this.supportingDocument = supportingDocument;
    }

    @XmlTransient
    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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
        if (!(object instanceof OrderedTicket)) {
            return false;
        }
        OrderedTicket other = (OrderedTicket) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderedTicket[ id=" + id + " ]";
    }
}
