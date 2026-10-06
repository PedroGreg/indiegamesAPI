package com.apigamesinidie.assembler;

import com.apigamesinidie.controller.DeveloperController;
import com.apigamesinidie.model.Category;
import com.apigamesinidie.model.Developer;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeveloperModelAssembler implements RepresentationModelAssembler<Developer, EntityModel<Developer>> {
    public EntityModel<Developer> toModel(Developer developer){
        return EntityModel.of(developer,
                linkTo(methodOn(DeveloperController.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"),
                linkTo(methodOn(DeveloperController.class).findById(developer.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(DeveloperController.class).findByName(developer.getName(), Pageable.unpaged())).withRel("find by name").withType("GET"),
                linkTo(methodOn(DeveloperController.class).create(developer)).withRel("create").withType("POST"),
                linkTo(methodOn(DeveloperController.class).update(developer.getId(), developer)).withRel("update").withType("PUT"),
                linkTo(methodOn(DeveloperController.class).delete(developer.getId())).withRel("delete").withType("DELETE")
                );
    }
}
