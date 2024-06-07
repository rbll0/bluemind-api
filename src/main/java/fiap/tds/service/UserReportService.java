package fiap.tds.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.tds.exception.DatabaseException;
import fiap.tds.models.UserReport;
import fiap.tds.repositories.UserReportRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe de serviço para gerenciar operações relacionadas a UserReport.
 */
public class UserReportService {
    private UserReportRepository userReportRepository = new UserReportRepository();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final String VIA_CEP_BASE_URL = "https://viacep.com.br/ws/";
    private static final String VERIFIER_BASE_URL = "https://verifier.meetchopra.com/verify/";
    private static final String VERIFIER_API_TOKEN = "92928b756e623357b3bd80e8dc90deae85bb184eda58afa5e71d7060430664ed9aa0d163b4a1ea1cc81357753dcd4a26";
    private static final Logger logger = LogManager.getLogger(UserReportService.class);

    /**
     * Codifica uma string para ser usada em uma URL.
     *
     * @param value o valor a ser codificado.
     * @return o valor codificado.
     * @throws UnsupportedEncodingException se a codificação não for suportada.
     */
    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    /**
     * Verifica a validade do email do usuário.
     *
     * @param email o email a ser verificado.
     * @return true se o email for válido, caso contrário, false.
     * @throws DatabaseException se ocorrer um erro durante a verificação.
     */
    private boolean verificaEmail(String email) throws DatabaseException {
        if (email == null || email.isEmpty()) {
            throw new DatabaseException("Email é nulo ou vazio.");
        }

        try {
            String encodedEmail = encodeValue(email);
            String url = VERIFIER_BASE_URL + encodedEmail + "?token=" + VERIFIER_API_TOKEN.trim();

            logger.info("Verificando email: " + email + " com URL: " + url);

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    logger.info("Resposta da verificação de email: " + jsonResponse);

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode responseJson = objectMapper.readTree(jsonResponse);

                    boolean isValid = responseJson.get("status").asBoolean();
                    if (!isValid) {
                        String errorMessage = responseJson.get("error").get("message").asText();
                        logger.warn("Email inválido: " + errorMessage);
                    }

                    return isValid;
                } catch (IOException e) {
                    logger.error("Erro ao verificar email: ", e);
                    throw new DatabaseException("Erro ao verificar email.", e);
                }
            } catch (IOException e) {
                logger.error("Erro ao configurar a requisição de verificação de email: ", e);
                throw new DatabaseException("Erro ao configurar a requisição de verificação de email.", e);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Erro ao codificar o email: ", e);
            throw new DatabaseException("Erro ao codificar o email.", e);
        }
    }

    /**
     * Busca o endereço a partir do CEP.
     *
     * @param cep o CEP a ser buscado.
     * @return o JSON contendo o endereço.
     * @throws DatabaseException se ocorrer um erro durante a busca.
     */
    private JsonNode buscaEnderecoPorCep(String cep) throws DatabaseException {
        if (cep == null || cep.isEmpty()) {
            throw new DatabaseException("CEP é nulo ou vazio.");
        }

        String url = VIA_CEP_BASE_URL + cep + "/json/";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                logger.info("Resposta da busca de CEP: " + jsonResponse);

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(jsonResponse);
            } catch (IOException e) {
                logger.error("Erro ao buscar endereço por CEP: ", e);
                throw new DatabaseException("Erro ao buscar endereço por CEP.", e);
            }
        } catch (IOException e) {
            logger.error("Erro ao configurar a requisição de busca de CEP: ", e);
            throw new DatabaseException("Erro ao configurar a requisição de busca de CEP.", e);
        }
    }

    /**
     * Adiciona um novo usuário.
     *
     * @param userReport o usuário a ser adicionado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void adicionarUserReport(UserReport userReport) throws DatabaseException {
        if (userReport == null) {
            throw new DatabaseException("UserReport é nulo.");
        }

        if (userReport.getEmail() == null || userReport.getCep() == null) {
            throw new DatabaseException("Email ou CEP é nulo.");
        }

        // Verifica se o email do usuário é válido
        if (!EMAIL_PATTERN.matcher(userReport.getEmail()).matches()) {
            throw new DatabaseException("Email do usuário é inválido.");
        }

        if (!verificaEmail(userReport.getEmail())) {
            throw new DatabaseException("Email do usuário é inválido.");
        }

        JsonNode endereco = buscaEnderecoPorCep(userReport.getCep());
        if (endereco.get("erro") != null && endereco.get("erro").asBoolean()) {
            throw new DatabaseException("CEP do usuário é inválido.");
        }

        userReportRepository.inserir(userReport);
    }

    /**
     * Atualiza um usuário existente.
     *
     * @param userReport o usuário a ser atualizado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void atualizarUserReport(UserReport userReport) throws DatabaseException {
        if (userReport == null) {
            throw new DatabaseException("UserReport é nulo.");
        }

        if (userReport.getEmail() == null || userReport.getCep() == null) {
            throw new DatabaseException("Email ou CEP é nulo.");
        }

        // Verifica se o email do usuário é válido
        if (!EMAIL_PATTERN.matcher(userReport.getEmail()).matches()) {
            throw new DatabaseException("Email do usuário é inválido.");
        }

        if (!verificaEmail(userReport.getEmail())) {
            throw new DatabaseException("Email do usuário é inválido.");
        }

        JsonNode endereco = buscaEnderecoPorCep(userReport.getCep());
        if (endereco.get("erro") != null && endereco.get("erro").asBoolean()) {
            throw new DatabaseException("CEP do usuário é inválido.");
        }

        userReportRepository.atualizar(userReport);
    }

    /**
     * Remove um usuário pelo seu ID.
     *
     * @param id o ID do usuário a ser removido.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public void removerUserReport(int id) throws DatabaseException {
        userReportRepository.deletar(id);
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id o ID do usuário.
     * @return o usuário encontrado ou null se não for encontrado.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public UserReport buscarUserReportPorId(int id) throws DatabaseException {
        return userReportRepository.buscarPorId(id);
    }

    /**
     * Lista todos os usuários.
     *
     * @return uma lista de usuários.
     * @throws DatabaseException se ocorrer um erro durante a operação.
     */
    public List<UserReport> listarUserReports() throws DatabaseException {
        return userReportRepository.listarTodos();
    }
}
