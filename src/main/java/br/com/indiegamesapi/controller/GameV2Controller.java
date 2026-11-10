package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.GameModelAssembler;
import br.com.indiegamesapi.dto.response.GameV2ResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// NOVO (versionamento por URL): versão 2 das consultas de games.
//   /api/game  (ou /api/game/v1) -> GameController (developer, category e platforms completos)
//   /api/game/v2                 -> este controller (só os nomes)
@Tag(name = "Games v2", description = "Versão 2: formato resumido, com só os nomes de developer, category e platforms.")
@RestController
@RequestMapping("/api/game/v2")
public class GameV2Controller {

    private final GameService service;
    private final GameModelAssembler assembler;
    private final PagedResourcesAssembler<Game> pagedAssembler;

    public GameV2Controller(GameService service, GameModelAssembler assembler, PagedResourcesAssembler<Game> pagedAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Operation(summary = "Buscar todos os games (v2)", description = "Paginação: page, size e sort.")
    @ApiResponse(responseCode = "200", description = "Lista paginada no formato v2", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = GameV2ResponseDTO.PAGE_EXAMPLE)))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<GameV2ResponseDTO>>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        Page<Game> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page, assembler::toV2Model));
    }

    @Operation(summary = "Buscar game por ID (v2)")
    @ApiResponse(responseCode = "200", description = "Game no formato v2", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = GameV2ResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Game não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<GameV2ResponseDTO>> findById(@Parameter(description = "ID do game", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(assembler.toV2Model(service.findById(id)));
    }
}
