package eu.bugnion.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "fiscal_code")
    private String fiscalCode;

    @Column(name = "vat")
    private String vat;

    @OneToMany(mappedBy = "client")
    private Set<Patent> patents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public Client uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public Client firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Client lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public Client fiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
        return this;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getVat() {
        return vat;
    }

    public Client vat(String vat) {
        this.vat = vat;
        return this;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public Set<Patent> getPatents() {
        return patents;
    }

    public Client patents(Set<Patent> patents) {
        this.patents = patents;
        return this;
    }

    public Client addPatent(Patent patent) {
        this.patents.add(patent);
        patent.setClient(this);
        return this;
    }

    public Client removePatent(Patent patent) {
        this.patents.remove(patent);
        patent.setClient(null);
        return this;
    }

    public void setPatents(Set<Patent> patents) {
        this.patents = patents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", fiscalCode='" + getFiscalCode() + "'" +
            ", vat='" + getVat() + "'" +
            "}";
    }
}
