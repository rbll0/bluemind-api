# API de Monitoramento Marinho

## Descrição

API de Monitoramento Marinho para registro de incidentes, acidentes e observações de vida marinha. Suporta CRUD de relatórios e usuários, validação de e-mails e CEPs, e armazenamento de links de mídia. Implementada em Java com Jersey, Grizzly, e banco de dados relacional.

## Funcionalidades

- **Administrador**:
  - Adicionar, atualizar, deletar e listar administradores
  - Autenticação de administradores

- **UserReport**:
  - Adicionar, atualizar, deletar e listar usuários
  - Validação de e-mails e CEPs

- **Report**:
  - Adicionar, atualizar, deletar e listar relatórios
  - Armazenamento de links de mídia

## Endpoints

### Administrador

- **Adicionar Administrador**: `POST /administradores`
- **Atualizar Administrador**: `PUT /administradores`
- **Deletar Administrador**: `DELETE /administradores/{id}`
- **Buscar Administrador por ID**: `GET /administradores/{id}`
- **Listar Administradores**: `GET /administradores`
- **Login Administrador**: `POST /administradores/login`

### UserReport

- **Adicionar UserReport**: `POST /userreports`
- **Atualizar UserReport**: `PUT /userreports`
- **Deletar UserReport**: `DELETE /userreports/{id}`
- **Buscar UserReport por ID**: `GET /userreports/{id}`
- **Listar UserReports**: `GET /userreports`

### Report

- **Adicionar Report**: `POST /relatorios`
- **Atualizar Report**: `PUT /relatorios`
- **Deletar Report**: `DELETE /relatorios/{id}`
- **Buscar Report por ID**: `GET /relatorios/{id}`
- **Listar Reports**: `GET /relatorios`
