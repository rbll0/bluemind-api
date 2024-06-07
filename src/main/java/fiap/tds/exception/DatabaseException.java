package fiap.tds.exception;



/**
 * Classe  de exceção personalizada para erros de banco de dados.
 */
public class DatabaseException extends Throwable {

    /**
     *  Construtor para criar uma exceção com uma mensagem específica
     *
     * @param message a mensgem de erro.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     *  Construtor para criar uma exceção com uma mensagem específica e uma causa.
     *
     * @param message a mensgem de erro.
     * @param cause a causa da exceção.
     */

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
}
}