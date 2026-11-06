package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.UserProfileModelAssembler;
import br.com.indiegamesapi.model.UserProfile;
import br.com.indiegamesapi.service.UserProfileService;
// ADICIONADO: import de ArraySchema.
// Por quê / o que faz: usado nas respostas de listagem (findAll / findByName) para indicar uma lista do modelo.
import io.swagger.v3.oas.annotations.media.ArraySchema;
// ADICIONADO: import de ExceptionResponseModel.
// Por quê / o que faz: usado como schema das respostas 404 (e 400 no Game).
import br.com.indiegamesapi.exception.ExceptionResponseModel;
// ADICIONADO: import de ProblemDetail.
// Por quê / o que faz: usado como schema das respostas 400 geradas pelo Spring.
import org.springframework.http.ProblemDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
// ADICIONADO: import de ParameterObject.
// Por quê / o que faz: necessário para o @ParameterObject usado no findAll (paginação no Swagger).
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Perfil do Usuário")
@RestController
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileModelAssembler assembler;

    public UserProfileController(UserProfileService service, UserProfileModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Operation(summary = "Criar um novo perfil de usuário")
    @ApiResponse(responseCode = "201", description = "Perfil de usuario criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Buscar todos os perfis de usuário")
    @ApiResponse(responseCode = "200", description = "Lista com todos os perfis de usuário", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserProfile.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<UserProfile>>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        Page<UserProfile> pageUserProfile = service.findAll(pageable);
        PagedModel<EntityModel<UserProfile>> allUserProfile = PagedModel.of(
                pageUserProfile.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageUserProfile.getSize(), pageUserProfile.getNumber(), pageUserProfile.getTotalElements(), pageUserProfile.getTotalPages())
                );
        return new ResponseEntity<>(allUserProfile, HttpStatus.OK);
    }

    @Operation(summary = "Buscar perfil de usuário por ID")
    @ApiResponse(responseCode = "200", description = "Perfil de usuário encontrado por ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(responseCode = "404", description = "Perfil de usuario não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Atualizar perfil de usuário")
    @ApiResponse(responseCode = "200", description = "Perfil de usuario atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Perfil de usuário não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Deletar perfil de usuário(Não é permitido DELETE CASCADE)")
    @ApiResponse(responseCode = "204", description = "Perfil de usuário deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Perfil de usuário não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
