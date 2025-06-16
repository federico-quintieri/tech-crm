package com.techcmr.tech_cmr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.techcmr.tech_cmr.enums.PrioritaTask;
import com.techcmr.tech_cmr.enums.StatoTask;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "progetto_id")
    private Progetto progetto;

    @ManyToOne
    @JoinColumn(name = "programmatore_id")
    private Programmatore programmatore;

    @Enumerated(EnumType.STRING)
    private StatoTask stato;

    @Enumerated(EnumType.STRING)
    private PrioritaTask priorita;

    private LocalDateTime dataCreazione;
    private LocalDate scadenza;
    private LocalDateTime completatoIl;

    // getters e setters

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Progetto getProgetto() {
        return this.progetto;
    }

    public void setProgetto(Progetto progetto) {
        this.progetto = progetto;
    }

    public Programmatore getProgrammatore() {
        return this.programmatore;
    }

    public void setProgrammatore(Programmatore programmatore) {
        this.programmatore = programmatore;
    }

    public StatoTask getStato() {
        return this.stato;
    }

    public void setStato(StatoTask stato) {
        this.stato = stato;
    }

    public PrioritaTask getPriorita() {
        return this.priorita;
    }

    public void setPriorita(PrioritaTask priorita) {
        this.priorita = priorita;
    }

    public LocalDateTime getDataCreazione() {
        return this.dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getScadenza() {
        return this.scadenza;
    }

    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    public LocalDateTime getCompletatoIl() {
        return this.completatoIl;
    }

    public void setCompletatoIl(LocalDateTime completatoIl) {
        this.completatoIl = completatoIl;
    }


}
