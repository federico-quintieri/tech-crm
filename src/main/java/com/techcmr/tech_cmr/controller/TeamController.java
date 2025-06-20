package com.techcmr.tech_cmr.controller;

import org.springframework.web.bind.annotation.RestController;

import com.techcmr.tech_cmr.dto.TeamDTO;
import com.techcmr.tech_cmr.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    // GET /teams - Recupera tutti i team
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.findAllTeams();
        return ResponseEntity.ok(teams);
    }

    // GET /teams/{id} - Recupera un team per ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        try {
            TeamDTO team = teamService.findTeamById(id);
            return ResponseEntity.ok(team);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /teams - Crea un nuovo team
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        TeamDTO createdTeam = teamService.createTeam(teamDTO);
        return ResponseEntity.ok(createdTeam);
    }

    // PUT /teams/{id} - Aggiorna un team esistente
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTeam(@PathVariable Long id, @RequestBody TeamDTO teamDTO) {
        try {
            teamService.updateTeam(id, teamDTO);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /teams/{id} - Elimina un team
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        try {
            teamService.deleteTeam(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
