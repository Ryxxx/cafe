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
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "details_commande_id")
    private Long detailsCommandeId;

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

    @OneToMany(mappedBy = "commande")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userId", "paimentId", "commande" }, allowSetters = true)
    private Set<DetailsCommande> detailsCommandeIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commande id(Long id) {
        this.id = id;
        return this;
    }

    public Long getDetailsCommandeId() {
        return this.detailsCommandeId;
    }

    public Commande detailsCommandeId(Long detailsCommandeId) {
        this.detailsCommandeId = detailsCommandeId;
        return this;
    }

    public void setDetailsCommandeId(Long detailsCommandeId) {
        this.detailsCommandeId = detailsCommandeId;
    }

    public Long getQuantite() {
        return this.quantite;
    }

    public Commande quantite(Long quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Commande dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return this.dateModification;
    }

    public Commande dateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
        return this;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public Produit getProduitId() {
        return this.produitId;
    }

    public Commande produitId(Produit produit) {
        this.setProduitId(produit);
        return this;
    }

    public void setProduitId(Produit produit) {
        this.produitId = produit;
    }

    public Set<DetailsCommande> getDetailsCommandeIds() {
        return this.detailsCommandeIds;
    }

    public Commande detailsCommandeIds(Set<DetailsCommande> detailsCommandes) {
        this.setDetailsCommandeIds(detailsCommandes);
        return this;
    }

    public Commande addDetailsCommandeId(DetailsCommande detailsCommande) {
        this.detailsCommandeIds.add(detailsCommande);
        detailsCommande.setCommande(this);
        return this;
    }

    public Commande removeDetailsCommandeId(DetailsCommande detailsCommande) {
        this.detailsCommandeIds.remove(detailsCommande);
        detailsCommande.setCommande(null);
        return this;
    }

    public void setDetailsCommandeIds(Set<DetailsCommande> detailsCommandes) {
        if (this.detailsCommandeIds != null) {
            this.detailsCommandeIds.forEach(i -> i.setCommande(null));
        }
        if (detailsCommandes != null) {
            detailsCommandes.forEach(i -> i.setCommande(this));
        }
        this.detailsCommandeIds = detailsCommandes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", detailsCommandeId=" + getDetailsCommandeId() +
            ", quantite=" + getQuantite() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateModification='" + getDateModification() + "'" +
            "}";
    }
}
