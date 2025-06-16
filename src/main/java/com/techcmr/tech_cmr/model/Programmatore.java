package com.techcmr.tech_cmr.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Programmatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String specializzazioni;
    private boolean disponibile;
    private LocalDateTime assuntoIl;

    @ManyToMany(mappedBy = "programmatori")
    private List<Team> teams;

    @OneToMany(mappedBy = "programmatore")
    private List<Task> tasks;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSpecializzazioni() {
        return this.specializzazioni;
    }

    public void setSpecializzazioni(String specializzazioni) {
        this.specializzazioni = specializzazioni;
    }

    public boolean isDisponibile() {
        return this.disponibile;
    }

    public boolean getDisponibile() {
        return this.disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public LocalDateTime getAssuntoIl() {
        return this.assuntoIl;
    }

    public void setAssuntoIl(LocalDateTime assuntoIl) {
        this.assuntoIl = assuntoIl;
    }

    public List<Team> getTeams() {
        return this.teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
