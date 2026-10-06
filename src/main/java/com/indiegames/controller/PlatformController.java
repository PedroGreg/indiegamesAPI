package com.indiegames.controller;

import com.indiegames.assembler.PlatformModelAssembler;
import com.indiegames.model.Platform;
import com.indiegames.service.PlatformService;
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

@Tag(name = "Requisições de plataformas")
@RestController
@RequestMapping("/api/platform")
public class PlatformController {

    private final PlatformService service;
    private final PlatformModelAssembler assembler;

    public PlatformController(PlatformService service, PlatformModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Tag(name = "Criar")
    @Operation(summary = "Criar(POST)")
    @ApiResponse(responseCode = "201", description = "Plataforma criada", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<Platform>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nova plataforma",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Platform.class),
                            examples = @ExampleObject(value = "{ \"name\": \"PC (steam)\" }"))
            )
            @RequestBody @Valid Platform platform){
        Platform platform1 = service.create(platform);
        EntityModel<Platform> createPlatform = assembler.toModel(platform1);
        return new ResponseEntity<>(createPlatform, HttpStatus.CREATED);
    }

    @Tag(name = "Buscar todos")
    @Operation(summary = "Buscar todos(GET)")
    @ApiResponse(responseCode = "200", description = "Plataformas", content = @Content)
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Platform>>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        Page<Platform> pagePlatform = service.findAll(pageable);
        PagedModel<EntityModel<Platform>> allPlatform = PagedModel.of(
                pagePlatform.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pagePlatform.getSize(), pagePlatform.getNumber(), pagePlatform.getTotalElements(), pagePlatform.getTotalPages())
                );
        return new ResponseEntity<>(allPlatform, HttpStatus.OK);
    }

    @Tag(name = "Buscar por ID")
    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Plataforma", content = @Content)
    @ApiResponse(responseCode = "404", description = "Plataforma não encontrada", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Platform>> findById(@PathVariable Long id){
        Platform platform1 = service.findById(id);
        EntityModel<Platform> findIdPlatform= assembler.toModel(platform1);
        return new ResponseEntity<>(findIdPlatform, HttpStatus.OK);
    }

    @Tag(name = "Buscar por nome")
    @Operation(summary = "Buscar por nome(GET)")
    @ApiResponse(responseCode = "200", description = "Plataformas", content = @Content)
    @ApiResponse(responseCode = "204", description = "Blank", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<Platform>>> findByName(
            @RequestParam String name,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Platform> pagePlatform = service.findByName(name, pageable);
        PagedModel<EntityModel<Platform>> allNamePlatform = PagedModel.of(
                pagePlatform.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pagePlatform.getSize(), pagePlatform.getNumber(), pagePlatform.getTotalElements(), pagePlatform.getTotalPages())
        );
        return new ResponseEntity<>(allNamePlatform, HttpStatus.OK);
    }

    @Tag(name = "Atualizar")
    @Operation(summary = "Atualizar(PUT)")
    @ApiResponse(responseCode = "200", description = "Plataforma atualizada", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Platform>> update(@PathVariable Long id,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Nova Plataforma",
                                                                required = true,
                                                                content = @Content(mediaType = "application/json",
                                                                        schema= @Schema(implementation = Platform.class),
                                                                        examples = @ExampleObject(value = "{ \"name\": \"Console\" }"))
                                                        )
                                                        @RequestBody @Valid Platform platform){
        Platform platform1 = service.update(id, platform);
        EntityModel<Platform>  updatePlatform = assembler.toModel(platform1);
        return new ResponseEntity<>(updatePlatform, HttpStatus.OK);
    }

    @Tag(name = "Deletar")
    @Operation(summary = "Deletar(DELETE)")
    @ApiResponse(responseCode = "204", description = "Plataforma deletada", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
