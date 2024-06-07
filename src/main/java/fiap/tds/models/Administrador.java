package fiap.tds.models;


    /**
     *  Classe que representa um administrador do sistema.
     */
public class Administrador {
    private int id;
    private String nome;
    private String credencial;
    private String senha;

    /**
     * Construtor vazio.
     */
    public Administrador() {
    }

    /**
     * Construtor completo.
     *
     * @param id o id do administrador.
     * @param nome o nome do administrador.
     * @param credencial a credencial do administrador.
     * @param senha a senha do administrador.
     */

    public Administrador(int id, String nome, String credencial, String senha) {
        this.id = id;
        this.nome = nome;
        this.credencial = credencial;
        this.senha = senha;
    }

    /**
     * Método para obter o id do administrador.
     *
     * @return o id do administrador.
     */

    public int getId() {
        return id;
    }

    /**
     * Método para definir o id do administrador.
     *
     * @param id o id do administrador.
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método para obter o nome do administrador.
     *
     * @return o nome do administrador.
     */

    public String getNome() {
        return nome;
    }

    /**
     * Método para definir o nome do administrador.
     *
     * @param nome o nome do administrador.
     */

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Método para obter a credencial do administrador.
     *
     * @return a credencial do administrador.
     */

    public String getCredencial() {
        return credencial;
    }

    /**
     * Método para definir a credencial do administrador.
     *
     * @param credencial a credencial do administrador.
     */

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }

    /**
     * Método para obter a senha do administrador.
     *
     * @return a senha do administrador.
     */

    public String getSenha() {
        return senha;
    }

    /**
     * Método para definir a senha do administrador.
     *
     * @param senha a senha do administrador.
     */

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Método para verificar se um administrador é igual a outro.
     *
     * @param o o objeto a ser comparado.
     * @return true se os administradores forem iguais, false caso contrário.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Administrador that = (Administrador) o;

        if (id != that.id) return false;
        if (!nome.equals(that.nome)) return false;
        if (!credencial.equals(that.credencial)) return false;
        return senha.equals(that.senha);
    }
}
