package com.indiegames.controller;

import com.indiegames.assembler.UserModelAssembler;
import com.indiegames.model.User;
import com.indiegames.service.UserService;
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

@Tag(name = "Requisições de Usuarios")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;
    private final UserModelAssembler assembler;

    public UserController(UserService service, UserModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Tag(name = "Criar")
    @Operation(summary = "Criar(POST)")
    @ApiResponse(responseCode = "201", description = "Usuario criada", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<User>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo usuario",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"name\": \"Pedro Gregorio\",\n" +
                                    "  \"email\": \"pedro@email.com\",\n" +
                                    "  \"userProfile\": {\n" +
                                    "    \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\"\n" +
                                    "  }\n" +
                                    "}"))
            )
            @RequestBody @Valid User user){
        User user1 = service.create(user);
        EntityModel<User> createUser = assembler.toModel(user1);
        return new ResponseEntity<>(createUser, HttpStatus.CREATED);
    }

    @Tag(name = "Buscar todos")
    @Operation(summary = "Buscar todas(GET)")
    @ApiResponse(responseCode = "200", description = "Usuarios", content = @Content)
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<User>>> findAll(@PageableDefault(size = 10)Pageable pageable){
        Page<User> pageUser = service.findAll(pageable);
        PagedModel<EntityModel<User>> allUser = PagedModel.of(
                pageUser.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageUser.getSize(), pageUser.getNumber(), pageUser.getTotalElements(), pageUser.getTotalPages())
                );
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @Tag(name = "Buscar por ID")
    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Usuario", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findById(@PathVariable Long id){
        User user1 = service.findById(id);
        EntityModel<User> findIdUser= assembler.toModel(user1);
        return new ResponseEntity<>(findIdUser, HttpStatus.OK);
    }

    @Tag(name = "Buscar por nome")
    @Operation(summary = "Buscar por nome(GET)")
    @ApiResponse(responseCode = "200", description = "Usuarios", content = @Content)
    @ApiResponse(responseCode = "204", description = "Usuarios", content = @Content)
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<User>>> findByName(
            @RequestParam String name,
            @PageableDefault(size = 10) Pageable pageable){
        Page<User> pageUser = service.findByName(name, pageable);
        PagedModel<EntityModel<User>> allNameUser = PagedModel.of(
                pageUser.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageUser.getSize(), pageUser.getNumber(), pageUser.getTotalElements(), pageUser.getTotalPages())
        );
        return new ResponseEntity<>(allNameUser, HttpStatus.OK);
    }

    @Tag(name = "Atualizar")
    @Operation(summary = "Atualizar(PUT)")
    @ApiResponse(responseCode = "200", description = "Usuario atualizado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo usuario",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = User.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Pedro Gregorio\", \"email\": \"pedro@email.com\" }"))
            )
            @RequestBody @Valid User user){
        User user1 = service.update(id, user);
        EntityModel<User>  updateUser = assembler.toModel(user1);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
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
