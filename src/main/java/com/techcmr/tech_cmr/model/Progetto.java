package com.techcmr.tech_cmr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.techcmr.tech_cmr.enums.StatoProgetto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;

@Entity
public class Progetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "progetto")
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    private StatoProgetto stato;

    private LocalDate dataInizio;
    private LocalDate dataFinePrevista;
    private LocalDate dataFineEffettiva;

    private LocalDateTime creatoIl;

    // getters e setters


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

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public StatoProgetto getStato() {
        return this.stato;
    }

    public void setStato(StatoProgetto stato) {
        this.stato = stato;
    }

    public LocalDate getDataInizio() {
        return this.dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFinePrevista() {
        return this.dataFinePrevista;
    }

    public void setDataFinePrevista(LocalDate dataFinePrevista) {
        this.dataFinePrevista = dataFinePrevista;
    }

    public LocalDate getDataFineEffettiva() {
        return this.dataFineEffettiva;
    }

    public void setDataFineEffettiva(LocalDate dataFineEffettiva) {
        this.dataFineEffettiva = dataFineEffettiva;
    }

    public LocalDateTime getCreatoIl() {
        return this.creatoIl;
    }

    public void setCreatoIl(LocalDateTime creatoIl) {
        this.creatoIl = creatoIl;
    }

}
