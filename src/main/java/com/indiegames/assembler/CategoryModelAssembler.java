package com.indiegames.assembler;

import com.indiegames.controller.CategoryController;
import com.indiegames.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    public EntityModel<Category> toModel(Category category){
        return EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"),
                linkTo(methodOn(CategoryController.class).findById(category.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(CategoryController.class).findByName(category.getName(), Pageable.unpaged())).withRel("find by name").withType("GET"),
                linkTo(methodOn(CategoryController.class).create(category)).withRel("create").withType("POST"),
                linkTo(methodOn(CategoryController.class).update(category.getId(), category)).withRel("update").withType("PUT"),
                linkTo(methodOn(CategoryController.class).delete(category.getId())).withRel("delete").withType("DELETE")
                );
    }
}
