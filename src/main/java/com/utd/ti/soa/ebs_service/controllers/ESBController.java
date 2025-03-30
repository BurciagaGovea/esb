    package com.utd.ti.soa.ebs_service.controllers;

import com.utd.ti.soa.ebs_service.model.User; // Usamos User en lugar de UserDTO
import com.utd.ti.soa.ebs_service.utils.Auth;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/v1/esb")
public class ESBController {
    private final WebClient webClient = WebClient.create("https://api-production-4a9e.up.railway.app");
    private final Auth auth = new Auth();

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(@RequestBody User user,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        try {

            if (!auth.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválidooo");
            }
            

            System.out.println("📤 Enviando solicitud a Node.js con usuario: " + user.getUsername());

            String response = webClient.post()
                .uri("/api/users/createUser")  // Ruta corregida
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            System.out.println("✅ Respuesta del servicio Node.js: " + response);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity.ok().headers(headers).body(response);
        } catch (WebClientResponseException e) {
            System.err.println("❌ Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("⚠️ Error general: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @GetMapping(value = "/user/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        
        
        try {

            if(!auth.validateToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválidooo");
            }

            String response = webClient.get()
                .uri("/api/users/all")
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

    @PutMapping(value = "/user/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@PathVariable("id") String id, @RequestBody User user, @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenn) {
        try {
            if(!auth.validateToken(tokenn)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            String response = webClient.put()
                .uri("/api/users/update/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
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

    @PutMapping(value = "/user/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String tok) {
        try {
            if(!auth.validateToken(tok)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido");
            }

            String response = webClient.put()
                .uri("/api/users/delete/" + id)
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
