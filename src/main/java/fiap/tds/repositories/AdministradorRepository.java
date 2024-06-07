package fiap.tds.repositories;

import fiap.tds.exception.DatabaseException;
import fiap.tds.infraestructure.DatabaseConnection;
import fiap.tds.models.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministradorRepository {

    public void inserir(Administrador administrador) throws DatabaseException {
        String sql = "INSERT INTO tb_administrador (nome_adm, credencial_adm, senha_adm) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, administrador.getNome());
            stmt.setString(2, administrador.getCredencial());
            stmt.setString(3, administrador.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inserir administrador: " + e.getMessage(), e);
        }
    }

    public void atualizar(Administrador administrador) throws DatabaseException {
        String sql = "UPDATE tb_administrador SET nome_adm = ?, credencial_adm = ?, senha_adm = ? WHERE id_adm = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, administrador.getNome());
            stmt.setString(2, administrador.getCredencial());
            stmt.setString(3, administrador.getSenha());
            stmt.setInt(4, administrador.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar administrador: " + e.getMessage(), e);
        }
    }

    public void deletar(int id) throws DatabaseException {
        String sql = "DELETE FROM tb_administrador WHERE id_adm = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar administrador: " + e.getMessage(), e);
        }
    }

    public Administrador buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM tb_administrador WHERE id_adm = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Administrador admin = new Administrador();
                    admin.setId(rs.getInt("id_adm"));
                    admin.setNome(rs.getString("nome_adm"));
                    admin.setCredencial(rs.getString("credencial_adm"));
                    admin.setSenha(rs.getString("senha_adm"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar administrador: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Administrador> listarTodos() throws DatabaseException {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT * FROM tb_administrador";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Administrador admin = new Administrador();
                admin.setId(rs.getInt("id_adm"));
                admin.setNome(rs.getString("nome_adm"));
                admin.setCredencial(rs.getString("credencial_adm"));
                admin.setSenha(rs.getString("senha_adm"));
                administradores.add(admin);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar administradores: " + e.getMessage(), e);
        }
        return administradores;
    }

    public Administrador buscarPorCredencial(String credencial) throws DatabaseException {
        String sql = "SELECT * FROM tb_administrador WHERE credencial_adm = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, credencial);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Administrador admin = new Administrador();
                    admin.setId(rs.getInt("id_adm"));
                    admin.setNome(rs.getString("nome_adm"));
                    admin.setCredencial(rs.getString("credencial_adm"));
                    admin.setSenha(rs.getString("senha_adm"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar administrador: " + e.getMessage(), e);
        }
        return null;
    }
}
