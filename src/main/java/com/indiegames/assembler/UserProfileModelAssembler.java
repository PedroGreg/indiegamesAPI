package com.indiegames.assembler;

import com.indiegames.controller.UserProfileController;
import com.indiegames.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserProfileModelAssembler implements RepresentationModelAssembler<UserProfile, EntityModel<UserProfile>> {
    public EntityModel<UserProfile> toModel(UserProfile userProfile){
        return EntityModel.of(userProfile,
                linkTo(methodOn(UserProfileController.class).findAll(Pageable.unpaged())).withRel("user-profiles").withType("GET"),
                linkTo(methodOn(UserProfileController.class).findById(userProfile.getId())).withSelfRel().withType("GET"),
                linkTo(methodOn(UserProfileController.class).create(userProfile)).withRel("create").withType("POST"),
                linkTo(methodOn(UserProfileController.class).update(userProfile.getId(), userProfile)).withRel("update").withType("PUT"),
                linkTo(methodOn(UserProfileController.class).delete(userProfile.getId())).withRel("delete").withType("DELETE")
                );
    }
}
