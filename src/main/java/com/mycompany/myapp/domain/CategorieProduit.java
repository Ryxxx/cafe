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
 * A CategorieProduit.
 */
@Entity
@Table(name = "categorie_produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CategorieProduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @Column(name = "date_suppression")
    private LocalDate dateSuppression;

    @OneToMany(mappedBy = "categorieProduit")
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

    public CategorieProduit id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public CategorieProduit nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public CategorieProduit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public CategorieProduit dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public CategorieProduit dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateSuppression() {
        return this.dateSuppression;
    }

    public CategorieProduit dateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
        return this;
    }

    public void setDateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public Set<Produit> getProduitIds() {
        return this.produitIds;
    }

    public CategorieProduit produitIds(Set<Produit> produits) {
        this.setProduitIds(produits);
        return this;
    }

    public CategorieProduit addProduitId(Produit produit) {
        this.produitIds.add(produit);
        produit.setCategorieProduit(this);
        return this;
    }

    public CategorieProduit removeProduitId(Produit produit) {
        this.produitIds.remove(produit);
        produit.setCategorieProduit(null);
        return this;
    }

    public void setProduitIds(Set<Produit> produits) {
        if (this.produitIds != null) {
            this.produitIds.forEach(i -> i.setCategorieProduit(null));
        }
        if (produits != null) {
            produits.forEach(i -> i.setCategorieProduit(this));
        }
        this.produitIds = produits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategorieProduit)) {
            return false;
        }
        return id != null && id.equals(((CategorieProduit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategorieProduit{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateSuppression='" + getDateSuppression() + "'" +
            "}";
    }
}
