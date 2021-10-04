package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DetailsCommande.
 */
@Entity
@Table(name = "details_commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DetailsCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "total")
    private Double total;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @OneToOne
    @JoinColumn(unique = true)
    private User userId;

    @OneToOne
    @JoinColumn(unique = true)
    private Paiement paimentId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produitId", "detailsCommandeIds" }, allowSetters = true)
    private Commande commande;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DetailsCommande id(Long id) {
        this.id = id;
        return this;
    }

    public Double getTotal() {
        return this.total;
    }

    public DetailsCommande total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public DetailsCommande dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public DetailsCommande dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public User getUserId() {
        return this.userId;
    }

    public DetailsCommande userId(User user) {
        this.setUserId(user);
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Paiement getPaimentId() {
        return this.paimentId;
    }

    public DetailsCommande paimentId(Paiement paiement) {
        this.setPaimentId(paiement);
        return this;
    }

    public void setPaimentId(Paiement paiement) {
        this.paimentId = paiement;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public DetailsCommande commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailsCommande)) {
            return false;
        }
        return id != null && id.equals(((DetailsCommande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailsCommande{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}
