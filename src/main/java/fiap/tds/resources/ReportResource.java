package fiap.tds.resources;

import fiap.tds.exception.DatabaseException;
import fiap.tds.models.Report;
import fiap.tds.service.ReportService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Classe de recurso para gerenciar operações relacionadas a Report via API REST.
 */
@Path("/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {
    private ReportService reportService = new ReportService();

    /**
     * Adiciona um novo relatório.
     *
     * @param report o relatório a ser adicionado.
     * @return resposta HTTP com status 201 (Created) ou 500 (Internal Server Error).
     */
    @POST
    public Response adicionarReport(Report report) {
        try {
            reportService.adicionarReport(report);
            return Response.status(Response.Status.CREATED).build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Atualiza um relatório existente.
     *
     * @param report o relatório a ser atualizado.
     * @return resposta HTTP com status 200 (OK) ou 500 (Internal Server Error).
     */
    @PUT
    public Response atualizarReport(Report report) {
        try {
            reportService.atualizarReport(report);
            return Response.ok().build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Remove um relatório pelo seu ID.
     *
     * @param id o ID do relatório a ser removido.
     * @return resposta HTTP com status 200 (OK) ou 500 (Internal Server Error).
     */
    @DELETE
    @Path("/{id}")
    public Response removerReport(@PathParam("id") int id) {
        try {
            reportService.removerReport(id);
            return Response.ok().build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Busca um relatório pelo seu ID.
     *
     * @param id o ID do relatório.
     * @return resposta HTTP com o relatório encontrado ou status 500 (Internal Server Error).
     */
    @GET
    @Path("/{id}")
    public Response buscarReportPorId(@PathParam("id") int id) {
        try {
            Report report = reportService.buscarReportPorId(id);
            if (report != null) {
                return Response.ok(report).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Lista todos os relatórios.
     *
     * @return resposta HTTP com a lista de relatórios ou status 500 (Internal Server Error).
     */
    @GET
    public Response listarReports() {
        try {
            List<Report> reports = reportService.listarReports();
            return Response.ok(reports).build();
        } catch (DatabaseException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
