package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Panier.
 */
@Entity
@Table(name = "panier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Panier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "quantite")
    private Long quantite;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @JsonIgnoreProperties(value = { "categorieProduit", "stockProduit", "reductionProduit" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Produit produitId;

    @OneToMany(mappedBy = "panier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userId", "panier" }, allowSetters = true)
    private Set<SessionPanier> sessionPanierIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Panier id(Long id) {
        this.id = id;
        return this;
    }

    public Long getQuantite() {
        return this.quantite;
    }

    public Panier quantite(Long quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Panier dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public Panier dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public Produit getProduitId() {
        return this.produitId;
    }

    public Panier produitId(Produit produit) {
        this.setProduitId(produit);
        return this;
    }

    public void setProduitId(Produit produit) {
        this.produitId = produit;
    }

    public Set<SessionPanier> getSessionPanierIds() {
        return this.sessionPanierIds;
    }

    public Panier sessionPanierIds(Set<SessionPanier> sessionPaniers) {
        this.setSessionPanierIds(sessionPaniers);
        return this;
    }

    public Panier addSessionPanierId(SessionPanier sessionPanier) {
        this.sessionPanierIds.add(sessionPanier);
        sessionPanier.setPanier(this);
        return this;
    }

    public Panier removeSessionPanierId(SessionPanier sessionPanier) {
        this.sessionPanierIds.remove(sessionPanier);
        sessionPanier.setPanier(null);
        return this;
    }

    public void setSessionPanierIds(Set<SessionPanier> sessionPaniers) {
        if (this.sessionPanierIds != null) {
            this.sessionPanierIds.forEach(i -> i.setPanier(null));
        }
        if (sessionPaniers != null) {
            sessionPaniers.forEach(i -> i.setPanier(this));
        }
        this.sessionPanierIds = sessionPaniers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Panier)) {
            return false;
        }
        return id != null && id.equals(((Panier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Panier{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}
