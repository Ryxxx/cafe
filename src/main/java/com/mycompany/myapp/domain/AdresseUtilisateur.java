package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AdresseUtilisateur.
 */
@Entity
@Table(name = "adresse_utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdresseUtilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "adresse_ligne_1")
    private String adresseLigne1;

    @Column(name = "adresse_ligne_2")
    private String adresseLigne2;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "pays")
    private String pays;

    @Column(name = "telephone")
    private String telephone;

    @OneToOne
    @JoinColumn(unique = true)
    private User userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdresseUtilisateur id(Long id) {
        this.id = id;
        return this;
    }

    public String getAdresseLigne1() {
        return this.adresseLigne1;
    }

    public AdresseUtilisateur adresseLigne1(String adresseLigne1) {
        this.adresseLigne1 = adresseLigne1;
        return this;
    }

    public void setAdresseLigne1(String adresseLigne1) {
        this.adresseLigne1 = adresseLigne1;
    }

    public String getAdresseLigne2() {
        return this.adresseLigne2;
    }

    public AdresseUtilisateur adresseLigne2(String adresseLigne2) {
        this.adresseLigne2 = adresseLigne2;
        return this;
    }

    public void setAdresseLigne2(String adresseLigne2) {
        this.adresseLigne2 = adresseLigne2;
    }

    public String getVille() {
        return this.ville;
    }

    public AdresseUtilisateur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public AdresseUtilisateur codePostal(String codePostal) {
        this.codePostal = codePostal;
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return this.pays;
    }

    public AdresseUtilisateur pays(String pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public AdresseUtilisateur telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUserId() {
        return this.userId;
    }

    public AdresseUtilisateur userId(User user) {
        this.setUserId(user);
        return this;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdresseUtilisateur)) {
            return false;
        }
        return id != null && id.equals(((AdresseUtilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdresseUtilisateur{" +
            "id=" + getId() +
            ", adresseLigne1='" + getAdresseLigne1() + "'" +
            ", adresseLigne2='" + getAdresseLigne2() + "'" +
            ", ville='" + getVille() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", pays='" + getPays() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
