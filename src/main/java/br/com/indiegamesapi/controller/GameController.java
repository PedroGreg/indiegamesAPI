package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.GameModelAssembler;
import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.service.GameService;
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

@Tag(name = "Games")
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService service;
    private final GameModelAssembler assembler;

    public GameController(GameService service, GameModelAssembler assembler){
        this.assembler = assembler;
        this.service = service;
    }

    @Operation(summary = "Criar um novo game")
    @ApiResponse(responseCode = "201", description = "Game criado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ExceptionResponseModel.class, ProblemDetail.class})))
    @ApiResponse(responseCode = "404", description = "Um ou mais ID enviado(s) não foi(ram) encontrado(s)(Categoria, Desenvolvedor ou Plataforma)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PostMapping()
    public ResponseEntity<EntityModel<Game>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo Game",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Game.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Undertale\", \"status\" : \"DEVELOPMENT\", \"developer\" : { \"id\" : \"1\" }, \"category\" : { \"id\" : \"1\" }, \"platforms\" : [{ \"id\" : \"1\"}] }"))
            )
            @RequestBody @Valid Game game){
        Game game1 = service.create(game);
        EntityModel<Game> createGame = assembler.toModel(game1);
        return new ResponseEntity<>(createGame, HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar todos os games")
    @ApiResponse(responseCode = "200", description = "Lista com todos os games", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Game.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Game>>> findAll(@ParameterObject @PageableDefault(size = 10)Pageable pageable){
        Page<Game> pageGame = service.findAll(pageable);
        PagedModel<EntityModel<Game>> allGame = PagedModel.of(
                pageGame.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageGame.getSize(), pageGame.getNumber(), pageGame.getTotalElements(), pageGame.getTotalPages())
                );
        return new ResponseEntity<>(allGame, HttpStatus.OK);
    }

    @Operation(summary = "Buscar por ID(GET)")
    @ApiResponse(responseCode = "200", description = "Game encontrado por ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Game>> findById(@PathVariable Long id){
        Game game1 = service.findById(id);
        EntityModel<Game> findIdGame= assembler.toModel(game1);
        return new ResponseEntity<>(findIdGame, HttpStatus.OK);
    }

    @Operation(summary = "Buscar game por nome")
    @ApiResponse(responseCode = "200", description = "Lista com todos os games pelo nome enviado", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Game.class))))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<Game>>> findByName(
            @RequestParam String name,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable){
        Page<Game> pageGame = service.findByName(name, pageable);
        PagedModel<EntityModel<Game>> allNameGame = PagedModel.of(
                pageGame.getContent().stream().map(assembler::toModel).toList(),
                new PagedModel.PageMetadata(pageGame.getSize(), pageGame.getNumber(), pageGame.getTotalElements(), pageGame.getTotalPages())
        );
        return new ResponseEntity<>(allNameGame, HttpStatus.OK);
    }

    @Operation(summary = "Atualizar game")
    @ApiResponse(responseCode = "200", description = "Game atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)))
    @ApiResponse(responseCode = "400", description = "Houve algum erro nos dados enviados na requisição", content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ExceptionResponseModel.class, ProblemDetail.class})))
    @ApiResponse(responseCode = "404", description = "Game não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Game>> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novo Game",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema= @Schema(implementation = Game.class),
                            examples = @ExampleObject(value = "{ \"name\": \"Undertale\", \"status\" : \"RELEASED\", \"developer\" : { \"id\" : \"1\" }, \"category\" : { \"id\" : \"1\" }, \"platforms\" : [{ \"id\" : \"1\"}] }"))
            )
            @RequestBody @Valid Game game){
        Game game1 = service.update(id, game);
        EntityModel<Game>  updateGame = assembler.toModel(game1);
        return new ResponseEntity<>(updateGame, HttpStatus.OK);
    }

    @Operation(summary = "Deletar um game")
    @ApiResponse(responseCode = "204", description = "Game deletado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Game não encontrado com esse ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @ApiResponse(responseCode = "429", description = "Muitas requisições feitas num período de 1 minuto, por favor aguarde o tempo indicado e tente novamente", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"error\":\"Too many requests, try again in 30s\"}")))
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Valid Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
