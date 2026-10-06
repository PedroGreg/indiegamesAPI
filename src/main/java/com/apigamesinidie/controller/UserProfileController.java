package com.apigamesinidie.controller;

import com.apigamesinidie.assembler.PlatformModelAssembler;
import com.apigamesinidie.assembler.UserProfileModelAssembler;
import com.apigamesinidie.model.Platform;
import com.apigamesinidie.model.UserProfile;
import com.apigamesinidie.service.PlatformService;
import com.apigamesinidie.service.UserProfileService;
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

@Tag(name = "Requisições de Perfil do Usuário")
@RestController
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileModelAssembler assembler;

    public UserProfileController(UserProfileService service, UserProfileModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }
    
    @Tag(name = "Criar")
    @Operation(summary = "Criar(POST)")
    @ApiResponse(responseCode = "201", description = "Perfil de usuario criado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<UserProfile>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo perfil de usuario",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = UserProfile.class),
                            examples = @ExampleObject(value = "{ \"bio\": \"Esse é meu perfil\" }"))
            )
            @RequestBody @Valid UserProfile userProfile){
        UserProfile userProfile1 = service.create(userProfile);
        EntityModel<UserProfile> createUserProfile = assembler.toModel(userProfile1);
        return new ResponseEntity<>(createUserProfile, HttpStatus.CREATED);
    }

    @Tag(name = "Buscar todos")
    @Operation(summary = "Buscar todos(GET)")
    @ApiResponse(responseCode = "200", description = "Perfil de usuarios", content = @Content)
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<UserProfile>>> findAll(@PageableDefault(size = 10)Pageable pageable){
        Page<UserProfile> pageUserProfile = service.findAll(pageable);
        PagedModel<EntityModel<UserProfile>> allUserProfile = PagedModel.of(
                pageUserProfile.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageUserProfile.getSize(), pageUserProfile.getNumber(), pageUserProfile.getTotalElements(), pageUserProfile.getTotalPages())
                );
        return new ResponseEntity<>(allUserProfile, HttpStatus.OK);
    }

    @Tag(name = "Buscar por ID")
    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Perfil de usuario", content = @Content)
    @ApiResponse(responseCode = "404", description = "Perfil de usuario não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserProfile>> findById(@PathVariable Long id){
        UserProfile userProfile1 = service.findById(id);
        EntityModel<UserProfile> findIdUserProfile= assembler.toModel(userProfile1);
        return new ResponseEntity<>(findIdUserProfile, HttpStatus.OK);
    }

//    @Tag(name = "Buscar por nome", description = "Buscar por nome")
//    @Operation(summary = "Buscar por nome(GET)")
//    @ApiResponse(responseCode = "200", description = "Perfil de usuarios", content = @Content)
//    @ApiResponse(responseCode = "204", description = "Perfil de usuario não encontrada", content = @Content)
//    @GetMapping("/search")
//    public ResponseEntity<PagedModel<EntityModel<UserProfile>>> findByName(
//            @RequestParam String name,
//            @PageableDefault(size = 10) Pageable pageable){
//        Page<UserProfile> pageUserProfile = service.findByName(name, pageable);
//        PagedModel<EntityModel<UserProfile>> allNameUserProfile = PagedModel.of(
//                pageUserProfile.getContent().stream().map(assembler::toModel).toList(),
//                new PagedModel.PageMetadata(pageUserProfile.getSize(), pageUserProfile.getNumber(), pageUserProfile.getTotalElements(), pageUserProfile.getTotalPages())
//        );
//        return new ResponseEntity<>(allNameUserProfile, HttpStatus.OK);
//    }

    @Tag(name = "Atualizar")
    @Operation(summary = "Atualizar(PUT)")
    @ApiResponse(responseCode = "200", description = "Perfil de usuario atualizado", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserProfile>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo Perfil de usuario",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = UserProfile.class),
                            examples = @ExampleObject(value = "{ \"bio\": \"Esse é meu perfil atualizado\" }"))
            )
            @RequestBody @Valid UserProfile userProfile){
        UserProfile userProfile1 = service.update(id, userProfile);
        EntityModel<UserProfile>  updateUserProfile = assembler.toModel(userProfile1);
        return new ResponseEntity<>(updateUserProfile, HttpStatus.OK);
    }

    @Tag(name = "Deletar")
    @Operation(summary = "Deletar(DELETE)")
    @ApiResponse(responseCode = "204", description = "Perfil de usuario deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
