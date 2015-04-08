/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Notreal
 */
@Entity
@Table(name = "supporting_document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SupportingDocument.findAll", query = "SELECT s FROM SupportingDocument s"),
    @NamedQuery(name = "SupportingDocument.findById", query = "SELECT s FROM SupportingDocument s WHERE s.id = :id"),
    @NamedQuery(name = "SupportingDocument.findByName", query = "SELECT s FROM SupportingDocument s WHERE s.name = :name"),
    @NamedQuery(name = "SupportingDocument.findByDocType", query = "SELECT s FROM SupportingDocument s WHERE s.docType = :docType")})
public class SupportingDocument implements Serializable {
    private static final long serialVersionUID = 6895581723442846415L;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOC_TYPE")
    private int docType;
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supportingDocument")
    private List<OrderedTicket> orderedTicketList;

    public SupportingDocument() {
    }

    public SupportingDocument(String name, int docType) {
        this.name = name;
        this.docType = docType;
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

    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    // public int getCategoryId() {
    //     return this.category.getId();
    // }

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
        if (!(object instanceof SupportingDocument)) {
            return false;
        }
        SupportingDocument other = (SupportingDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SupportingDocument[ id=" + id + " ]";
    }
}
