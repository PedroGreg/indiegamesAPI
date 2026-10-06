package com.apigamesinidie.assembler;

import com.apigamesinidie.controller.DeveloperController;
import com.apigamesinidie.controller.PlatformController;
import com.apigamesinidie.model.Developer;
import com.apigamesinidie.model.Platform;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlatformModelAssembler implements RepresentationModelAssembler<Platform, EntityModel<Platform>> {
    public EntityModel<Platform> toModel(Platform platform){
        return EntityModel.of(platform,
                linkTo(methodOn(PlatformController.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"),
                linkTo(methodOn(PlatformController.class).findById(platform.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(PlatformController.class).findByName(platform.getName(), Pageable.unpaged())).withRel("find by name").withType("GET"),
                linkTo(methodOn(PlatformController.class).create(platform)).withRel("create").withType("POST"),
                linkTo(methodOn(PlatformController.class).update(platform.getId(), platform)).withRel("update").withType("PUT"),
                linkTo(methodOn(PlatformController.class).delete(platform.getId())).withRel("delete").withType("DELETE")
                );
    }
}
