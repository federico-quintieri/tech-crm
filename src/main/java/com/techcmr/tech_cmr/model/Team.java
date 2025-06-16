package com.techcmr.tech_cmr.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;
    private LocalDateTime creatoIl;

    @ManyToMany
    @JoinTable(name = "team_programmatore", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "programmatore_id"))
    private List<Programmatore> programmatori;

    @OneToMany(mappedBy = "team")
    private List<Progetto> progetti;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    // Getters and setters

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDateTime getCreatoIl() {
        return this.creatoIl;
    }

    public void setCreatoIl(LocalDateTime creatoIl) {
        this.creatoIl = creatoIl;
    }

    public List<Programmatore> getProgrammatori() {
        return this.programmatori;
    }

    public void setProgrammatori(List<Programmatore> programmatori) {
        this.programmatori = programmatori;
    }

    public List<Progetto> getProgetti() {
        return this.progetti;
    }

    public void setProgetti(List<Progetto> progetti) {
        this.progetti = progetti;
    }

    public Manager getManager() {
        return this.manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

}
