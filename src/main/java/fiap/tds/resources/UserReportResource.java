package fiap.tds.resources;

import fiap.tds.exception.DatabaseException;
import fiap.tds.models.UserReport;
import fiap.tds.service.UserReportService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Classe de recurso para gerenciar operações relacionadas a UserReport via API REST.
 */
@Path("/userreports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserReportResource {

    private UserReportService userReportService = new UserReportService();

    @POST
    public Response adicionarUserReport(UserReport userReport) {
        try {
            userReportService.adicionarUserReport(userReport);
            return Response.status(Response.Status.CREATED).build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response atualizarUserReport(UserReport userReport) {
        try {
            userReportService.atualizarUserReport(userReport);
            return Response.ok().build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removerUserReport(@PathParam("id") int id) {
        try {
            userReportService.removerUserReport(id);
            return Response.ok().build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarUserReportPorId(@PathParam("id") int id) {
        try {
            UserReport userReport = userReportService.buscarUserReportPorId(id);
            if (userReport != null) {
                return Response.ok(userReport).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response listarUserReports() {
        try {
            List<UserReport> userReports = userReportService.listarUserReports();
            return Response.ok(userReports).build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
