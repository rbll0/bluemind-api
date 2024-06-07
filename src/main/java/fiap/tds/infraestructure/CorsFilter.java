package fiap.tds.infraestructure;


import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;


/**
 *  Filtro para permitir solicitações CORS (Cross-Origin Resource Sharing).
 */

@Provider
public class CorsFilter implements ContainerResponseFilter {

    /**
     * Método que aplica o filtro CORS ás respostas HTTP.
     *
     * @param requestContext o contexto da solicitação.
     * @param responseContext contexto da resposta do servidor.
     */

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext
    ) throws IOException{
        /**
         * Obtém os cabeçalhos da resposta HTTP.
         * MultivaluedMap é utilizado para representar um mapa que pode conter múltiplos valores para uma única chave.
         */

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        // Adiciona os cabeçalhos necessários para permitir solicitações CORS.
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD, PATCH");
        headers.add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        headers.add("Access-Control-Allow-Credentials", "true");
    }

}
