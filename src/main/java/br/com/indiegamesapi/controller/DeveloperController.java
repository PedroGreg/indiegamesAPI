package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.DeveloperModelAssembler;
import br.com.indiegamesapi.model.Developer;
import br.com.indiegamesapi.service.DeveloperService;
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

@Tag(name = "Desenvolvedores")
@RestController
@RequestMapping("/api/developer")
public class DeveloperController {

    private final DeveloperService service;
    private final DeveloperModelAssembler assembler;

    public DeveloperController(DeveloperService service, DeveloperModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Operation(summary = "Criar um novo desenvolvedor")
    @ApiResponse(responseCode = "201", description = "Desenvolvedor criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Developer.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<Developer>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo desenvolvedor",
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

    @Operation(summary = "Buscar todos os desenvolvedores")
    @ApiResponse(responseCode = "200", description = "Lista com todos os desenvolvedores", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Developer.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Developer>>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        Page<Developer> pageDeveloper = service.findAll(pageable);
        PagedModel<EntityModel<Developer>> allDeveloper = PagedModel.of(
                pageDeveloper.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageDeveloper.getSize(), pageDeveloper.getNumber(), pageDeveloper.getTotalElements(), pageDeveloper.getTotalPages())
                );
        return new ResponseEntity<>(allDeveloper, HttpStatus.OK);
    }

//    @Tag(name = "Buscar por ID")
    @Operation(summary = "Buscar desenvolvedor por ID")
    @ApiResponse(responseCode = "200", description = "Desenvolvedor encontrado por ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Developer.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Developer>> findById(@PathVariable Long id){
        Developer developer1 = service.findById(id);
        EntityModel<Developer> findIdDeveloper= assembler.toModel(developer1);
        return new ResponseEntity<>(findIdDeveloper, HttpStatus.OK);
    }

    @Operation(summary = "Buscar desenvolvedor por nome")
    @ApiResponse(responseCode = "200", description = "Lista com todos os desenvolvedores pelo nome enviado", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Developer.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Atualizar desenvolvedor")
    @ApiResponse(responseCode = "200", description = "Desenvolvedor atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Developer.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Deletar desenvolvedor(Não é permitido DELETE CASCADE)")
    @ApiResponse(responseCode = "204", description = "Desenvolvedor deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
