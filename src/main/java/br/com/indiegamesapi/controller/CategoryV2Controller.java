package br.com.indiegamesapi.controller;

import br.com.indiegamesapi.assembler.CategoryModelAssembler;
import br.com.indiegamesapi.dto.response.CategoryV2ResponseDTO;
import br.com.indiegamesapi.exception.ExceptionResponseModel;
import br.com.indiegamesapi.model.Category;
import br.com.indiegamesapi.service.CategoryService;
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
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "v2 - Categorias")
@RestController
@RequestMapping("/api/category/v2")
public class CategoryV2Controller {

    private final CategoryService service;
    private final CategoryModelAssembler assembler;
    private final PagedResourcesAssembler<Category> pagedAssembler =
            new PagedResourcesAssembler<>(new HateoasPageableHandlerMethodArgumentResolver(), null);

    public CategoryV2Controller(CategoryService service, CategoryModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Buscar todas as categorias (v2)", description = "Paginação: page, size e sort.")
    @ApiResponse(responseCode = "200", description = "Lista paginada no formato v2", content = @Content(mediaType = "application/hal+json", schema = @Schema(type = "object", example = CategoryV2ResponseDTO.PAGE_EXAMPLE)))
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<CategoryV2ResponseDTO>>> findAll(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        Page<Category> page = service.findAll(pageable);
        return ResponseEntity.ok(pagedAssembler.toModel(page,
                category -> assembler.toV2Model(category, service.countGames(category.getId()))));
    }

    @Operation(summary = "Buscar categoria por ID (v2)")
    @ApiResponse(responseCode = "200", description = "Categoria no formato v2", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = CategoryV2ResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponseModel.class)))
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CategoryV2ResponseDTO>> findById(@Parameter(description = "ID da categoria", example = "1") @PathVariable Long id) {
        Category category = service.findById(id);
        return ResponseEntity.ok(assembler.toV2Model(category, service.countGames(id)));
    }
}
