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
 * A StockProduit.
 */
@Entity
@Table(name = "stock_produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StockProduit implements Serializable {

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

    @Column(name = "date_suppression")
    private LocalDate dateSuppression;

    @OneToMany(mappedBy = "stockProduit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categorieProduit", "stockProduit", "reductionProduit" }, allowSetters = true)
    private Set<Produit> produitIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockProduit id(Long id) {
        this.id = id;
        return this;
    }

    public Long getQuantite() {
        return this.quantite;
    }

    public StockProduit quantite(Long quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public StockProduit dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public StockProduit dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateSuppression() {
        return this.dateSuppression;
    }

    public StockProduit dateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
        return this;
    }

    public void setDateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public Set<Produit> getProduitIds() {
        return this.produitIds;
    }

    public StockProduit produitIds(Set<Produit> produits) {
        this.setProduitIds(produits);
        return this;
    }

    public StockProduit addProduitId(Produit produit) {
        this.produitIds.add(produit);
        produit.setStockProduit(this);
        return this;
    }

    public StockProduit removeProduitId(Produit produit) {
        this.produitIds.remove(produit);
        produit.setStockProduit(null);
        return this;
    }

    public void setProduitIds(Set<Produit> produits) {
        if (this.produitIds != null) {
            this.produitIds.forEach(i -> i.setStockProduit(null));
        }
        if (produits != null) {
            produits.forEach(i -> i.setStockProduit(this));
        }
        this.produitIds = produits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockProduit)) {
            return false;
        }
        return id != null && id.equals(((StockProduit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockProduit{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateSuppression='" + getDateSuppression() + "'" +
            "}";
    }
}
