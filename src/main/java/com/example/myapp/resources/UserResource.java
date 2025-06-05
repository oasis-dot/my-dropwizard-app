package com.example.myapp.resources;

import com.example.myapp.core.User;
import com.example.myapp.db.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDAO;

    public UserResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @UnitOfWork
    public Response createUser(@Valid User user) {
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("User with email " + user.getEmail() + " already exists.")
                           .build();
        }
        if (userDAO.findByPhone(user.getPhone()).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                           .entity("User with phone " + user.getPhone() + " already exists.")
                           .build();
        }
        User createdUser = userDAO.create(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getUserById(@PathParam("id") @NotNull Long id) {
        Optional<User> user = userDAO.findById(id);
        return user.map(u -> Response.ok(u).build())
                   .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @UnitOfWork
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Response updateUser(@PathParam("id") @NotNull Long id, @Valid User userUpdate) {
        return userDAO.findById(id).map(existingUser -> {
            Optional<User> userWithNewEmail = userDAO.findByEmail(userUpdate.getEmail());
            if (userWithNewEmail.isPresent() && userWithNewEmail.get().getId() != id) {
                return Response.status(Response.Status.CONFLICT)
                               .entity("Another user with email " + userUpdate.getEmail() + " already exists.")
                               .build();
            }

            existingUser.setName(userUpdate.getName());
            existingUser.setEmail(userUpdate.getEmail());
            User updatedUser = userDAO.update(existingUser);
            return Response.ok(updatedUser).build();
        }).orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") @NotNull Long id) {
        return userDAO.findById(id).map(user -> {
            userDAO.delete(user);
            return Response.noContent().build();
        }).orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
