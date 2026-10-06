package com.apigamesinidie.controller;

import com.apigamesinidie.assembler.GameModelAssembler;
import com.apigamesinidie.model.Game;
import com.apigamesinidie.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Requisições de Categorias")
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService service;
    private final GameModelAssembler assembler;

    public GameController(GameService service, GameModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Tag(name = "Criar")
    @Operation(summary = "Criar(POST)")
    @ApiResponse(responseCode = "201", description = "Usuario criada", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<Game>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nova usuario",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Game.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Undertale\", \"status\" : \"\", \"developer\" : \"1\", \"category\" : \"1\", \"platforms\" : \"1\" }"))
            )
            @RequestBody @Valid Game game){
        Game game1 = service.create(game);
        EntityModel<Game> createGame = assembler.toModel(game1);
        return new ResponseEntity<>(createGame, HttpStatus.CREATED);
    }

    @Tag(name = "Buscar todos")
    @Operation(summary = "Buscar todas(GET)")
    @ApiResponse(responseCode = "200", description = "Usuarios", content = @Content)
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Game>>> findAll(@PageableDefault(size = 10)Pageable pageable){
        Page<Game> pageGame = service.findAll(pageable);
        PagedModel<EntityModel<Game>> allGame = PagedModel.of(
                pageGame.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageGame.getSize(), pageGame.getNumber(), pageGame.getTotalElements(), pageGame.getTotalPages())
                );
        return new ResponseEntity<>(allGame, HttpStatus.OK);
    }

    @Tag(name = "Buscar por ID")
    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Usuario", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Game>> findById(@PathVariable Long id){
        Game game1 = service.findById(id);
        EntityModel<Game> findIdGame= assembler.toModel(game1);
        return new ResponseEntity<>(findIdGame, HttpStatus.OK);
    }

    @Tag(name = "Buscar por nome")
    @Operation(summary = "Buscar por nome(GET)")
    @ApiResponse(responseCode = "200", description = "Usuarios", content = @Content)
    @ApiResponse(responseCode = "204", description = "Usuarios", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<Game>>> findByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable){
        Page<Game> pageGame = service.findByName(name, pageable);
        PagedModel<EntityModel<Game>> allNameGame = PagedModel.of(
                pageGame.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageGame.getSize(), pageGame.getNumber(), pageGame.getTotalElements(), pageGame.getTotalPages())
        );
        return new ResponseEntity<>(allNameGame, HttpStatus.OK);
    }

    @Tag(name = "Atualizar")
    @Operation(summary = "Atualizar(PUT)")
    @ApiResponse(responseCode = "200", description = "Usuario atualizado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Game>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo usuario",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Game.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Deltarune\", \"status\" : \"\", \"developer\" : \"1\", \"category\" : \"1\", \"platforms\" : \"1\" }"))
            )
            @RequestBody @Valid Game game){
        Game game1 = service.update(id, game);
        EntityModel<Game>  updateGame = assembler.toModel(game1);
        return new ResponseEntity<>(updateGame, HttpStatus.OK);
    }

    @Tag(name = "Deletar")
    @Operation(summary = "Deletar(DELETE)")
    @ApiResponse(responseCode = "204", description = "Usuario deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
