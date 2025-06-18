package com.techcmr.tech_cmr.dto;


/**
 * RoleDTO è un oggetto di trasferimento dati (DTO) che viene usato
 * per trasferire le informazioni sui ruoli tra client e server
 * senza esporre direttamente l'entità JPA.
 */
public class RoleDTO {

    private Long id;
    private String name;

    // Costruttori
    public RoleDTO() {} // Costruttore vuoto richiesto da Spring
    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter e Setter: permettono di accedere/modificare i dati in modo controllato
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
