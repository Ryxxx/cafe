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
 * A ReductionProduit.
 */
@Entity
@Table(name = "reduction_produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReductionProduit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "pourcentage_reduction")
    private Double pourcentageReduction;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @Column(name = "date_suppression")
    private LocalDate dateSuppression;

    @OneToMany(mappedBy = "reductionProduit")
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

    public ReductionProduit id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public ReductionProduit nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public ReductionProduit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPourcentageReduction() {
        return this.pourcentageReduction;
    }

    public ReductionProduit pourcentageReduction(Double pourcentageReduction) {
        this.pourcentageReduction = pourcentageReduction;
        return this;
    }

    public void setPourcentageReduction(Double pourcentageReduction) {
        this.pourcentageReduction = pourcentageReduction;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public ReductionProduit actif(Boolean actif) {
        this.actif = actif;
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public ReductionProduit dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public ReductionProduit dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateSuppression() {
        return this.dateSuppression;
    }

    public ReductionProduit dateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
        return this;
    }

    public void setDateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public Set<Produit> getProduitIds() {
        return this.produitIds;
    }

    public ReductionProduit produitIds(Set<Produit> produits) {
        this.setProduitIds(produits);
        return this;
    }

    public ReductionProduit addProduitId(Produit produit) {
        this.produitIds.add(produit);
        produit.setReductionProduit(this);
        return this;
    }

    public ReductionProduit removeProduitId(Produit produit) {
        this.produitIds.remove(produit);
        produit.setReductionProduit(null);
        return this;
    }

    public void setProduitIds(Set<Produit> produits) {
        if (this.produitIds != null) {
            this.produitIds.forEach(i -> i.setReductionProduit(null));
        }
        if (produits != null) {
            produits.forEach(i -> i.setReductionProduit(this));
        }
        this.produitIds = produits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReductionProduit)) {
            return false;
        }
        return id != null && id.equals(((ReductionProduit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReductionProduit{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", pourcentageReduction=" + getPourcentageReduction() +
            ", actif='" + getActif() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateSuppression='" + getDateSuppression() + "'" +
            "}";
    }
}
