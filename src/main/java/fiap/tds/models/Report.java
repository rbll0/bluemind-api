package fiap.tds.models;

import java.sql.Timestamp;

/**
 * Classe que representa um relatório feito por um usuário no sistema.
 */
public class Report {
    private int id;
    private int userReportId; // Este é o ID do UserReport associado
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String cep;
    private String tipo;
    private String descricao;
    private double latitude;
    private double longitude;
    private Timestamp dataHora;
    private String midia; // Alterado para String para armazenar o link da imagem

    public Report() {
    }

    public Report(int id, int userReportId, String nomeCompleto, String email, String cpf, String cep, String tipo, String descricao, double latitude, double longitude, Timestamp dataHora, String midia) {
        this.id = id;
        this.userReportId = userReportId;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.cpf = cpf;
        this.cep = cep;
        this.tipo = tipo;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
        this.midia = midia;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserReportId() {
        return userReportId;
    }

    public void setUserReportId(int userReportId) {
        this.userReportId = userReportId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public String getMidia() {
        return midia;
    }

    public void setMidia(String midia) {
        this.midia = midia;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", userReportId=" + userReportId +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", cep='" + cep + '\'' +
                ", tipo='" + tipo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", dataHora=" + dataHora +
                ", midia='" + midia + '\'' +
                '}';
    }
}
