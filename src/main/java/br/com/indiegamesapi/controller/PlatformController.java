package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.PlatformModelAssembler;
import br.com.indiegamesapi.model.Platform;
import br.com.indiegamesapi.service.PlatformService;
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

@Tag(name = "Plataformas")
@RestController
@RequestMapping("/api/platform")
public class PlatformController {

    private final PlatformService service;
    private final PlatformModelAssembler assembler;

    public PlatformController(PlatformService service, PlatformModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Operation(summary = "Criar uma nova plataforma")
    @ApiResponse(responseCode = "201", description = "Plataforma criada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Buscar todas as plataformas)")
    @ApiResponse(responseCode = "200", description = "Lista com todas as plataformas", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Platform.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Platform>>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        Page<Platform> pagePlatform = service.findAll(pageable);
        PagedModel<EntityModel<Platform>> allPlatform = PagedModel.of(
                pagePlatform.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pagePlatform.getSize(), pagePlatform.getNumber(), pagePlatform.getTotalElements(), pagePlatform.getTotalPages())
                );
        return new ResponseEntity<>(allPlatform, HttpStatus.OK);
    }

    @Operation(summary = "Buscar plataforma por ID")
    @ApiResponse(responseCode = "200", description = "Plataforma encontrada por ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class)))
    @ApiResponse(responseCode = "404", description = "Plataforma não encontrada com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Platform>> findById(@PathVariable Long id){
        Platform platform1 = service.findById(id);
        EntityModel<Platform> findIdPlatform= assembler.toModel(platform1);
        return new ResponseEntity<>(findIdPlatform, HttpStatus.OK);
    }

    @Operation(summary = "Buscar plataforma por nome")
    @ApiResponse(responseCode = "200", description = "Lista com todos os desenvolvedores pelo nome enviado", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Platform.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Atualizar plataforma")
    @ApiResponse(responseCode = "200", description = "Plataforma atualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
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

    @Operation(summary = "Deletar plataforma(Não é permitido DELETE CASCADE)")
    @ApiResponse(responseCode = "204", description = "Plataforma deletada", content = @Content)
    @ApiResponse(responseCode = "404", description = "Desenvolvedor não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
