package com.indiegames.controller;

import com.indiegames.assembler.DeveloperModelAssembler;
import com.indiegames.model.Developer;
import com.indiegames.service.DeveloperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Requisições de Desenvolvedores")
@RestController
@RequestMapping("/api/developer")
public class DeveloperController {

    private final DeveloperService service;
    private final DeveloperModelAssembler assembler;

    public DeveloperController(DeveloperService service, DeveloperModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Tag(name = "Criar")
    @Operation(summary = "Criar(POST)")
    @ApiResponse(responseCode = "201", description = "Desenvolvedor criado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<Developer>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nova desenvolvedor",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Developer.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Toby Fox\" }"))
            )
            @RequestBody @Valid Developer developer){
        Developer developer1 = service.create(developer);
        EntityModel<Developer> createDeveloper = assembler.toModel(developer1);
        return new ResponseEntity<>(createDeveloper, HttpStatus.CREATED);
    }

    @Tag(name = "Buscar todos")
    @Operation(summary = "Buscar todos(GET)")
    @ApiResponse(responseCode = "200", description = "Desenvolvedores", content = @Content)
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Developer>>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        Page<Developer> pageDeveloper = service.findAll(pageable);
        PagedModel<EntityModel<Developer>> allDeveloper = PagedModel.of(
                pageDeveloper.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageDeveloper.getSize(), pageDeveloper.getNumber(), pageDeveloper.getTotalElements(), pageDeveloper.getTotalPages())
                );
        return new ResponseEntity<>(allDeveloper, HttpStatus.OK);
    }

    @Tag(name = "Buscar por ID")
    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Desenvolvedor", content = @Content)
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Developer>> findById(@PathVariable Long id){
        Developer developer1 = service.findById(id);
        EntityModel<Developer> findIdDeveloper= assembler.toModel(developer1);
        return new ResponseEntity<>(findIdDeveloper, HttpStatus.OK);
    }

    @Tag(name = "Buscar por nome")
    @Operation(summary = "Buscar por nome(GET)")
    @ApiResponse(responseCode = "200", description = "Desenvolvedores", content = @Content)
    @ApiResponse(responseCode = "204", description = "Desenvolvedor não encontrado", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<Developer>>> findByName(
            @RequestParam String name,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Developer> pageDeveloper = service.findByName(name, pageable);
        PagedModel<EntityModel<Developer>> allNameDeveloper = PagedModel.of(
                pageDeveloper.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageDeveloper.getSize(), pageDeveloper.getNumber(), pageDeveloper.getTotalElements(), pageDeveloper.getTotalPages())
        );
        return new ResponseEntity<>(allNameDeveloper, HttpStatus.OK);
    }

    @Tag(name = "Atualizar")
    @Operation(summary = "Atualizar(PUT)")
    @ApiResponse(responseCode = "200", description = "Desenvolvedor atualizado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Developer>> update(@PathVariable Long id,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Novo desenvolvedor",
                                                                required = true,
                                                                content = @Content(mediaType = "application/json",
                                                                        schema= @Schema(implementation = Developer.class),
                                                                        examples = @ExampleObject(value = "{ \"name\": \"Toby Fox\" }"))
                                                        )
                                                        @RequestBody @Valid Developer developer){
        Developer developer1 = service.update(id, developer);
        EntityModel<Developer>  updateDeveloper = assembler.toModel(developer1);
        return new ResponseEntity<>(updateDeveloper, HttpStatus.OK);
    }

    @Tag(name = "Deletar")
    @Operation(summary = "Deletar(DELETE)")
    @ApiResponse(responseCode = "204", description = "Desenvolvedor deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
