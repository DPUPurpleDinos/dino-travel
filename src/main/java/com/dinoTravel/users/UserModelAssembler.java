package com.dinoTravel.users;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Provides methods to turn a User into a RepresentationModel
 * and allows links to be added to the Model.
 */
@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    /**
     * Converts a User to a EntityModel of a User object
     * @param user the User object to convert to an EntityModel
     * @return the User as an EntityModel
     */
    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getUser_id())).withSelfRel());
    }
}