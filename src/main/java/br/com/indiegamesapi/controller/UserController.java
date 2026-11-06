package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.UserModelAssembler;
import br.com.indiegamesapi.model.User;
import br.com.indiegamesapi.service.UserService;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;
    private final UserModelAssembler assembler;

    public UserController(UserService service, UserModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Operation(summary = "Criar um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuario criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Buscar todos os usuários")
    @ApiResponse(responseCode = "200", description = "Lista com todos os usuários", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<User>>> findAll(@ParameterObject  @PageableDefault(size = 10)Pageable pageable){
        Page<User> pageUser = service.findAll(pageable);
        PagedModel<EntityModel<User>> allUser = PagedModel.of(
                pageUser.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageUser.getSize(), pageUser.getNumber(), pageUser.getTotalElements(), pageUser.getTotalPages())
                );
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuário por ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado por ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "404", description = "Usuario não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> findById(@PathVariable Long id){
        User user1 = service.findById(id);
        EntityModel<User> findIdUser= assembler.toModel(user1);
        return new ResponseEntity<>(findIdUser, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuário por nome")
    @ApiResponse(responseCode = "200", description = "Lista com todos os usuários pelo nome enviado", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<User>>> findByName(
            @RequestParam String name,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<User> pageUser = service.findByName(name, pageable);
        PagedModel<EntityModel<User>> allNameUser = PagedModel.of(
                pageUser.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageUser.getSize(), pageUser.getNumber(), pageUser.getTotalElements(), pageUser.getTotalPages())
        );
        return new ResponseEntity<>(allNameUser, HttpStatus.OK);
    }

    @Operation(summary = "Atualizar usuário")
    @ApiResponse(responseCode = "200", description = "Usuario atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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


    @Operation(summary = "Deletar usuário")
    @ApiResponse(responseCode = "204", description = "Usuario deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
