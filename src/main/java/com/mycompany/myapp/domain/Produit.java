package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Column(name = "sku")
    private String sku;

    @Column(name = "prix")
    private Double prix;

    @Column(name = "reduction_id")
    private Long reductionId;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_modification")
    private LocalDate dateModification;

    @Column(name = "date_suppression")
    private LocalDate dateSuppression;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produitIds" }, allowSetters = true)
    private CategorieProduit categorieProduit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produitIds" }, allowSetters = true)
    private StockProduit stockProduit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produitIds" }, allowSetters = true)
    private ReductionProduit reductionProduit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Produit nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Produit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return this.sku;
    }

    public Produit sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Produit prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Long getReductionId() {
        return this.reductionId;
    }

    public Produit reductionId(Long reductionId) {
        this.reductionId = reductionId;
        return this;
    }

    public void setReductionId(Long reductionId) {
        this.reductionId = reductionId;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Produit dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public Produit dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public LocalDate getDateSuppression() {
        return this.dateSuppression;
    }

    public Produit dateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
        return this;
    }

    public void setDateSuppression(LocalDate dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public CategorieProduit getCategorieProduit() {
        return this.categorieProduit;
    }

    public Produit categorieProduit(CategorieProduit categorieProduit) {
        this.setCategorieProduit(categorieProduit);
        return this;
    }

    public void setCategorieProduit(CategorieProduit categorieProduit) {
        this.categorieProduit = categorieProduit;
    }

    public StockProduit getStockProduit() {
        return this.stockProduit;
    }

    public Produit stockProduit(StockProduit stockProduit) {
        this.setStockProduit(stockProduit);
        return this;
    }

    public void setStockProduit(StockProduit stockProduit) {
        this.stockProduit = stockProduit;
    }

    public ReductionProduit getReductionProduit() {
        return this.reductionProduit;
    }

    public Produit reductionProduit(ReductionProduit reductionProduit) {
        this.setReductionProduit(reductionProduit);
        return this;
    }

    public void setReductionProduit(ReductionProduit reductionProduit) {
        this.reductionProduit = reductionProduit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", sku='" + getSku() + "'" +
            ", prix=" + getPrix() +
            ", reductionId=" + getReductionId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            ", dateSuppression='" + getDateSuppression() + "'" +
            "}";
    }
}
