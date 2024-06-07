package fiap.tds.service;

import fiap.tds.exception.DatabaseException;
import fiap.tds.models.Administrador;
import fiap.tds.repositories.AdministradorRepository;

import java.util.List;

/**
 * Classe de serviço para gerenciar operações relacionadas a Administrador.
 */
public class AdministradorService {
    private AdministradorRepository administradorRepository = new AdministradorRepository();

    /**
     * Adiciona um novo administrador.
     *
     * @param administrador o administrador a ser adicionado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void adicionarAdministrador(Administrador administrador) throws DatabaseException {
        // Verifica se o nome do administrador já existe
        List<Administrador> administradores = listarAdministradores();
        for (Administrador adm : administradores) {
            if (adm.getNome().equals(administrador.getNome())) {
                throw new DatabaseException("Nome do administrador já existe.");
            }
        }
        administradorRepository.inserir(administrador);
    }

    /**
     * Atualiza um administrador existente.
     *
     * @param administrador o administrador a ser atualizado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void atualizarAdministrador(Administrador administrador) throws DatabaseException {
        // Verifica se o nome do administrador já existe
        List<Administrador> administradores = listarAdministradores();
        for (Administrador adm : administradores) {
            if (adm.getNome().equals(administrador.getNome()) && adm.getId() != administrador.getId()) {
                throw new DatabaseException("Nome do administrador já existe.");
            }
        }
        administradorRepository.atualizar(administrador);
    }

    /**
     * Remove um administrador pelo seu ID.
     *
     * @param id o ID do administrador a ser removido.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void removerAdministrador(int id) throws DatabaseException {
        administradorRepository.deletar(id);
    }

    /**
     * Busca um administrador pelo seu ID.
     *
     * @param id o ID do administrador.
     * @return o administrador encontrado ou null se não for encontrado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public Administrador buscarAdministradorPorId(int id) throws DatabaseException {
        return administradorRepository.buscarPorId(id);
    }

    /**
     * Lista todos os administradores.
     *
     * @return uma lista de administradores.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public List<Administrador> listarAdministradores() throws DatabaseException {
        return administradorRepository.listarTodos();
    }

    /**
     * Valida as credenciais do administrador.
     *
     * @param credencial a credencial do administrador.
     * @param senha a senha do administrador.
     * @return true se as credenciais forem válidas, false caso contrário.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public boolean validarCredenciais(String credencial, String senha) throws DatabaseException {
        Administrador admin = administradorRepository.buscarPorCredencial(credencial);
        return admin != null && admin.getSenha().equals(senha);
    }
}
