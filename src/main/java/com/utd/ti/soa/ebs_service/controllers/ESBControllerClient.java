package com.utd.ti.soa.ebs_service.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.utd.ti.soa.ebs_service.model.Client;
import com.utd.ti.soa.ebs_service.utils.Auth;


@RestController
@RequestMapping("/api/v1/esb")

public class ESBControllerClient {
    private final WebClient webClient = WebClient.create("https://clients-production-0c32.up.railway.app");
    private final Auth auth = new Auth();

    @GetMapping(value = "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllClients(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){

        try{

            if(!auth.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválidoo");
            }

            System.out.println("Enviando solicitud a node Clients");

            String response = webClient.get()
                .uri("/api/clients/clients")
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return ResponseEntity.ok().body(response);

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }

    }

    @PostMapping(value = "/clients/createClient", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody Client client) {
        try {
            if (!auth.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }
            System.out.println("Enviando solicitud a Node.js para crear cliente");
            String response = webClient.post()
                .uri("/api/clients/createClient")
                .bodyValue(client)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PutMapping(value = "/clients/updateClient/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id, @RequestBody Client client) {
        try {
            if (!auth.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }
            System.out.println("Enviando solicitud a Node.js para actualizar cliente");
            String response = webClient.put()
                .uri("/api/clients/updateAddress/" + id)
                .bodyValue(client)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return ResponseEntity.ok().body(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }  
    }

    @PutMapping(value = "/clients/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id) {
        try {
            if (!auth.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }
            System.out.println("Enviando solicitud a Node.js para eliminar cliente");
            String response = webClient.put()
                .uri("/api/clients/deleteClient/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return ResponseEntity.ok().body(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

}
