package fiap.tds.resources;

import fiap.tds.exception.DatabaseException;
import fiap.tds.models.Administrador;
import fiap.tds.service.AdministradorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Classe de recurso para gerenciar operações relacionadas a Administrador via API REST.
 */
@Path("/administradores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdministradorResource {
    private AdministradorService administradorService = new AdministradorService();

    /**
     * Adiciona um novo administrador.
     *
     * @param administrador o administrador a ser adicionado.
     * @return resposta HTTP com status 201 (Created) ou 500 (Internal Server Error).
     */
    @POST
    public Response adicionarAdministrador(Administrador administrador) {
        try {
            administradorService.adicionarAdministrador(administrador);
            return Response.status(Response.Status.CREATED).build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Atualiza um administrador existente.
     *
     * @param administrador o administrador a ser atualizado.
     * @return resposta HTTP com status 200 (OK) ou 500 (Internal Server Error).
     */
    @PUT
    public Response atualizarAdministrador(Administrador administrador) {
        try {
            administradorService.atualizarAdministrador(administrador);
            return Response.ok().build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Remove um administrador pelo seu ID.
     *
     * @param id o ID do administrador a ser removido.
     * @return resposta HTTP com status 200 (OK) ou 500 (Internal Server Error).
     */
    @DELETE
    @Path("/{id}")
    public Response removerAdministrador(@PathParam("id") int id) {
        try {
            administradorService.removerAdministrador(id);
            return Response.ok().build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Busca um administrador pelo seu ID.
     *
     * @param id o ID do administrador.
     * @return resposta HTTP com o administrador encontrado ou status 500 (Internal Server Error).
     */
    @GET
    @Path("/{id}")
    public Response buscarAdministradorPorId(@PathParam("id") int id) {
        try {
            Administrador administrador = administradorService.buscarAdministradorPorId(id);
            if (administrador != null) {
                return Response.ok(administrador).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Lista todos os administradores.
     *
     * @return resposta HTTP com a lista de administradores ou status 500 (Internal Server Error).
     */
    @GET
    public Response listarAdministradores() {
        try {
            List<Administrador> administradores = administradorService.listarAdministradores();
            return Response.ok(administradores).build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Autentica um administrador com base na credencial e senha.
     *
     * @param admin o administrador contendo a credencial e senha.
     * @return resposta HTTP com status 200 (OK) se a autenticação for bem-sucedida ou 401 (Unauthorized) se falhar.
     */
    @POST
    @Path("/login")
    public Response loginAdministrador(Administrador admin) {
        try {
            boolean isValid = administradorService.validarCredenciais(admin.getCredencial(), admin.getSenha());
            if (isValid) {
                return Response.ok().entity("{\"message\":\"Login successful\"}").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"Invalid credentials\"}").build();
            }
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
