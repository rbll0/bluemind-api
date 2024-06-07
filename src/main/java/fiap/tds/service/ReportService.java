package fiap.tds.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.tds.exception.DatabaseException;
import fiap.tds.models.Report;
import fiap.tds.models.UserReport;
import fiap.tds.repositories.ReportRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe de serviço para gerenciar operações relacionadas a Report.
 */
public class ReportService {
    private ReportRepository reportRepository = new ReportRepository();
    private UserReportRepository userReportRepository = new UserReportRepository();
    private static final List<String> TIPOS_VALIDOS = Arrays.asList("incidente", "acidente", "vida marinha");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final String VIA_CEP_BASE_URL = "https://viacep.com.br/ws/";
    private static final String VERIFIER_BASE_URL = "https://verifier.meetchopra.com/verify/";
    private static final String VERIFIER_API_TOKEN = "92928b756e623357b3bd80e8dc90deae85bb184eda58afa5e71d7060430664ed9aa0d163b4a1ea1cc81357753dcd4a26";
    private static final Logger logger = LogManager.getLogger(ReportService.class);

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

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

    private int adicionarUsuario(UserReport userReport) throws DatabaseException {
        userReportRepository.inserir(userReport);
        UserReport user = userReportRepository.buscarPorEmail(userReport.getEmail());
        if (user == null) {
            throw new DatabaseException("Erro ao inserir usuário no banco de dados");
        }
        return user.getId();
    }

    public void adicionarReport(Report report) throws DatabaseException {
        if (!TIPOS_VALIDOS.contains(report.getTipo().toLowerCase())) {
            throw new DatabaseException("Tipo de relatório inválido.");
        }

        if (!verificaEmail(report.getEmail())) {
            throw new DatabaseException("Email do usuário é inválido.");
        }

        JsonNode endereco = buscaEnderecoPorCep(report.getCep());
        if (endereco.get("erro") != null && endereco.get("erro").asBoolean()) {
            throw new DatabaseException("CEP do usuário é inválido.");
        }

        UserReport userReport = new UserReport();
        userReport.setNomeCompleto(report.getNomeCompleto());
        userReport.setEmail(report.getEmail());
        userReport.setCpf(report.getCpf());
        userReport.setCep(report.getCep());

        int userId = adicionarUsuario(userReport);

        report.setUserReportId(userId);
        reportRepository.inserir(report);
    }

    public void atualizarReport(Report report) throws DatabaseException {
        if (!TIPOS_VALIDOS.contains(report.getTipo().toLowerCase())) {
            throw new DatabaseException("Tipo de relatório inválido.");
        }

        UserReport userReport = userReportRepository.buscarPorId(report.getUserReportId());
        if (userReport == null) {
            throw new DatabaseException("Usuário não encontrado para o ID: " + report.getUserReportId());
        }
        logger.info("Usuário associado ao relatório: " + userReport);

        if (!verificaEmail(report.getEmail())) {
            throw new DatabaseException("Email do usuário é inválido.");
        }

        JsonNode endereco = buscaEnderecoPorCep(report.getCep());
        logger.info("Endereço obtido pelo CEP: " + endereco);
        if (endereco.get("erro") != null && endereco.get("erro").asBoolean()) {
            throw new DatabaseException("CEP do usuário é inválido.");
        }

        // Atualizar dados do usuário
        userReport.setNomeCompleto(report.getNomeCompleto());
        userReport.setEmail(report.getEmail());
        userReport.setCpf(report.getCpf());
        userReport.setCep(report.getCep());
        userReportRepository.atualizar(userReport);

        // Atualizar dados do relatório
        reportRepository.atualizar(report);
    }

    public void removerReport(int id) throws DatabaseException {
        reportRepository.deletar(id);
    }

    public Report buscarReportPorId(int id) throws DatabaseException {
        return reportRepository.buscarPorId(id);
    }

    public List<Report> listarReports() throws DatabaseException {
        return reportRepository.listarTodos();
    }
}
