package fiap.tds.repositories;

import fiap.tds.exception.DatabaseException;
import fiap.tds.infraestructure.DatabaseConnection;
import fiap.tds.models.UserReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o repositório para operações de CRUD da entidade UserReport.
 */
public class UserReportRepository {

    /**
     * Insere um novo usuário no banco de dados.
     *
     * @param userReport o usuário a ser inserido.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void inserir(UserReport userReport) throws DatabaseException {
        String sql = "INSERT INTO tb_user_report (nome_completo, email_user, cpf_user, cep_user) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userReport.getNomeCompleto());
            stmt.setString(2, userReport.getEmail());
            stmt.setString(3, userReport.getCpf());
            stmt.setString(4, userReport.getCep());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza um usuário existente no banco de dados.
     *
     * @param userReport o usuário a ser atualizado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void atualizar(UserReport userReport) throws DatabaseException {
        String sql = "UPDATE tb_user_report SET nome_completo = ?, email_user = ?, cpf_user = ?, cep_user = ? WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userReport.getNomeCompleto());
            stmt.setString(2, userReport.getEmail());
            stmt.setString(3, userReport.getCpf());
            stmt.setString(4, userReport.getCep());
            stmt.setInt(5, userReport.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Remove um usuário do banco de dados.
     *
     * @param id o ID do usuário a ser removido.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void deletar(int id) throws DatabaseException {
        String sql = "DELETE FROM tb_user_report WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar usuário: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um usuário no banco de dados pelo seu ID.
     *
     * @param id o ID do usuário.
     * @return o usuário encontrado ou null se não for encontrado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public UserReport buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM tb_user_report WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserReport userReport = new UserReport();
                    userReport.setId(rs.getInt("id_user"));
                    userReport.setNomeCompleto(rs.getString("nome_completo"));
                    userReport.setEmail(rs.getString("email_user"));
                    userReport.setCpf(rs.getString("cpf_user"));
                    userReport.setCep(rs.getString("cep_user"));
                    return userReport;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Busca um usuário no banco de dados pelo seu email.
     *
     * @param email o email do usuário.
     * @return o usuário encontrado ou null se não for encontrado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public UserReport buscarPorEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM tb_user_report WHERE email_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserReport userReport = new UserReport();
                    userReport.setId(rs.getInt("id_user"));
                    userReport.setNomeCompleto(rs.getString("nome_completo"));
                    userReport.setEmail(rs.getString("email_user"));
                    userReport.setCpf(rs.getString("cpf_user"));
                    userReport.setCep(rs.getString("cep_user"));
                    return userReport;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por email: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Lista todos os usuários do banco de dados.
     *
     * @return uma lista de usuários.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public List<UserReport> listarTodos() throws DatabaseException {
        List<UserReport> userReports = new ArrayList<>();
        String sql = "SELECT * FROM tb_user_report";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UserReport userReport = new UserReport();
                userReport.setId(rs.getInt("id_user"));
                userReport.setNomeCompleto(rs.getString("nome_completo"));
                userReport.setEmail(rs.getString("email_user"));
                userReport.setCpf(rs.getString("cpf_user"));
                userReport.setCep(rs.getString("cep_user"));
                userReports.add(userReport);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return userReports;
    }
}
