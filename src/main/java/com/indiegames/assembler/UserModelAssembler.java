package com.indiegames.assembler;

import com.indiegames.controller.UserController;
import com.indiegames.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    public EntityModel<User> toModel(User user){
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).findAll(Pageable.unpaged())).withRel("categories").withType("GET"),
                linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(UserController.class).findByName(user.getName(), Pageable.unpaged())).withRel("find by name").withType("GET"),
                linkTo(methodOn(UserController.class).create(user)).withRel("create").withType("POST"),
                linkTo(methodOn(UserController.class).update(user.getId(), user)).withRel("update").withType("PUT"),
                linkTo(methodOn(UserController.class).delete(user.getId())).withRel("delete").withType("DELETE")
                );
    }
}
