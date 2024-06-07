package fiap.tds.repositories;

import fiap.tds.exception.DatabaseException;
import fiap.tds.infraestructure.DatabaseConnection;
import fiap.tds.models.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o repositório para operações de CRUD da entidade Report.
 */

public class ReportRepository {

    /**
     * Insere um novo relatório no banco de dados.
     *
     * @param report o relatório a ser inserido.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void inserir(Report report) throws DatabaseException {
        String sql = "INSERT INTO tb_report (user_report_id, tipo, descricao, latitude, longitude, data_hora, midia) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, report.getUserReportId());
            stmt.setString(2, report.getTipo());
            stmt.setString(3, report.getDescricao());
            stmt.setDouble(4, report.getLatitude());
            stmt.setDouble(5, report.getLongitude());
            stmt.setTimestamp(6, report.getDataHora());
            stmt.setString(7, report.getMidia());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir relatório: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza um relatório existente no banco de dados.
     *
     * @param report o relatório a ser atualizado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void atualizar(Report report) throws DatabaseException {
        String sql = "UPDATE tb_report SET user_report_id = ?, tipo = ?, descricao = ?, latitude = ?, longitude = ?, data_hora = ?, midia = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, report.getUserReportId());
            stmt.setString(2, report.getTipo());
            stmt.setString(3, report.getDescricao());
            stmt.setDouble(4, report.getLatitude());
            stmt.setDouble(5, report.getLongitude());
            stmt.setTimestamp(6, report.getDataHora());
            stmt.setString(7, report.getMidia());
            stmt.setInt(8, report.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar relatório: " + e.getMessage(), e);
        }
    }

    /**
     * Remove um relatório do banco de dados.
     *
     * @param id o ID do relatório a ser removido.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void deletar(int id) throws DatabaseException {
        String sql = "DELETE FROM tb_report WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar relatório: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um relatório no banco de dados pelo seu ID.
     *
     * @param id o ID do relatório.
     * @return o relatório encontrado ou null se não for encontrado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public Report buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM tb_report WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Report report = new Report();
                    report.setId(rs.getInt("id"));
                    report.setUserReportId(rs.getInt("user_report_id"));
                    report.setTipo(rs.getString("tipo"));
                    report.setDescricao(rs.getString("descricao"));
                    report.setLatitude(rs.getDouble("latitude"));
                    report.setLongitude(rs.getDouble("longitude"));
                    report.setDataHora(rs.getTimestamp("data_hora"));
                    report.setMidia(rs.getString("midia"));
                    return report;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar relatório: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Lista todos os relatórios do banco de dados.
     *
     * @return uma lista de relatórios.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public List<Report> listarTodos() throws DatabaseException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM tb_report";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Report report = new Report();
                report.setId(rs.getInt("id"));
                report.setUserReportId(rs.getInt("user_report_id"));
                report.setTipo(rs.getString("tipo"));
                report.setDescricao(rs.getString("descricao"));
                report.setLatitude(rs.getDouble("latitude"));
                report.setLongitude(rs.getDouble("longitude"));
                report.setDataHora(rs.getTimestamp("data_hora"));
                report.setMidia(rs.getString("midia"));
                reports.add(report);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar relatórios: " + e.getMessage(), e);
        }
        return reports;
    }
}
