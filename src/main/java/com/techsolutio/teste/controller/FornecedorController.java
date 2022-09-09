package com.techsolutio.teste.controller;

import java.util.List;

import javax.validation.Valid;

import com.techsolutio.teste.model.Fornecedor;
import com.techsolutio.teste.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/fornecedor")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FornecedorController {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @GetMapping
    public ResponseEntity <List <Fornecedor>> getAll(){
        return ResponseEntity.ok(fornecedorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> getById(@PathVariable Long id){
        return fornecedorRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fornecedor> postFornecedor (@Valid @RequestBody Fornecedor fornecedor){
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorRepository.save(fornecedor));
    }

    @PutMapping
    public ResponseEntity<Fornecedor> putFornecedor (@Valid @RequestBody Fornecedor fornecedor){
        return fornecedorRepository.findById(fornecedor.getId())
                .map(resposta -> ResponseEntity.ok().body(fornecedorRepository.save(fornecedor)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFornecedor(@PathVariable Long id) {
        return fornecedorRepository.findById(id)
                .map(resposta -> {
                    fornecedorRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
