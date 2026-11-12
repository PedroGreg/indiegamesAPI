package br.com.indiegamesapi.assembler;

import br.com.indiegamesapi.controller.CategoryController;
import br.com.indiegamesapi.controller.CategoryV2Controller;
import br.com.indiegamesapi.controller.GameController;
import br.com.indiegamesapi.controller.GameV2Controller;
import br.com.indiegamesapi.dto.response.CategoryResponseDTO;
import br.com.indiegamesapi.dto.response.CategoryV2ResponseDTO;
import br.com.indiegamesapi.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    public EntityModel<Category> toModel(Category category){
        return EntityModel.of(category, links(category));
    }

    public EntityModel<CategoryResponseDTO> toDtoModel(Category category){
        return EntityModel.of(new CategoryResponseDTO(category), links(category));
    }

    public EntityModel<CategoryV2ResponseDTO> toV2Model(Category category, long gamesCount){
        return EntityModel.of(new CategoryV2ResponseDTO(category, gamesCount),
                linkTo(methodOn(CategoryV2Controller.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"),
                linkTo(methodOn(CategoryV2Controller.class).findById(category.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(CategoryController.class).findByName(category.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"),
                linkTo(methodOn(CategoryController.class).create(null, null)).withRel("create").withType("POST"),
                linkTo(methodOn(CategoryController.class).update(category.getId(), null, null)).withRel("update").withType("PUT"),
                linkTo(methodOn(CategoryController.class).delete(category.getId(), null)).withRel("delete").withType("DELETE"),
                linkTo(methodOn(GameController.class).findByCategory(category.getId(), Pageable.unpaged())).withRel("games").withType("GET"),
                linkTo(methodOn(GameV2Controller.class).findAll(Pageable.unpaged())).withRel("games-v2").withType("GET"),
                linkTo(methodOn(CategoryController.class).findById(category.getId())).withRel("v1").withType("GET"));
    }

    private List<Link> links(Category category){
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CategoryController.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).findById(category.getId())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).findByName(category.getName(), Pageable.unpaged())).withRel("find-by-name").withType("GET"));
        links.add(linkTo(methodOn(CategoryController.class).create(null, null)).withRel("create").withType("POST"));
        links.add(linkTo(methodOn(CategoryController.class).update(category.getId(), null, null)).withRel("update").withType("PUT"));
        links.add(linkTo(methodOn(CategoryController.class).delete(category.getId(), null)).withRel("delete").withType("DELETE"));
        links.add(linkTo(methodOn(GameController.class).findByCategory(category.getId(), Pageable.unpaged())).withRel("games").withType("GET"));
        links.add(linkTo(methodOn(CategoryV2Controller.class).findById(category.getId())).withRel("v2").withType("GET"));
        return links;
    }
}
