<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jspf/permissoes.jspf" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargoUsuario = (Cargo) sessao.getAttribute("cargoUsuario");
    boolean podeEditarRelatorio = temPermissaoCadastro(cargoUsuario, "relatorios");
    request.setAttribute("podeEditarRelatorio", podeEditarRelatorio);
    String homeDestino = request.getContextPath() + "/home.jsp";
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestão de Relatórios de Saúde</title>
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylerelatorios.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= homeDestino %>">SiGeNA</a></div>
        </header>
        <main class="relatorio-main">
            <h1>Gestão de Relatórios de Saúde</h1>

                <c:if test="${not empty mensagemSucesso}">
                    <div class="alert sucesso">${mensagemSucesso}</div>
                </c:if>
                <c:if test="${not empty mensagemErro}">
                    <div class="alert erro">${mensagemErro}</div>
                </c:if>
                <c:if test="${not empty erro}">
                    <div class="alert erro">${erro}</div>
                </c:if>

                    <h2>
            <section class="card" id="form-relatorio" style="display: block;">
                        <c:choose>
                            <c:when test="${not empty relatorioEdicao}">Editar Relatório</c:when>
                            <c:otherwise>Criar Novo Relatório</c:otherwise>
                        </c:choose>
                    </h2>
                    <c:choose>
                        <c:when test="${podeEditarRelatorio}">
                            <form method="post" action="RelatorioSaudeController" class="formulario">
                                <input type="hidden" name="acao" value="${empty relatorioEdicao ? 'criar' : 'atualizar'}">
                                <c:if test="${not empty relatorioEdicao}">
                                    <input type="hidden" name="relatorioId" value="${relatorioEdicao.id}">
                                </c:if>

                                <label>Animal:</label>
                                <select name="animalId" required>
                                    <option value="">Selecione um animal</option>
                                    <c:forEach var="animal" items="${animais}">
                                        <option value="${animal.id}"
                                                <c:if test="${not empty relatorioEdicao && animal.id == relatorioEdicao.animal.id}">selected</c:if>>
                                            ${animal.nome}
                                        </option>
                                    </c:forEach>
                                </select>

                                <label>Data do Check-up:</label>
                                <input type="date" name="dataRelatorio"
                                       value="${not empty relatorioEdicao ? relatorioEdicao.dataRelatorio : ''}" required>

                                <label>Peso (kg):</label>
                                <input type="number" name="peso" step="0.01" min="0"
                                       value="${not empty relatorioEdicao ? relatorioEdicao.peso : ''}"
                                       placeholder="Ex: 12.5">

                                <label>Status do animal:</label>
                                <div class="status-checkbox">
                                    <label class="checkbox-inline">
                                        <input type="checkbox" name="apto" value="APTO"
                                               <c:if test="${empty relatorioEdicao || relatorioEdicao.status == 'APTO'}">checked</c:if>>
                                        Apto para atividades
                                    </label>
                                    <span class="status-toggle-hint">Desmarque para registrar como inapto.</span>
                                </div>

                                <label>Observações:</label>
                                <textarea name="observacoes" rows="4" placeholder="Detalhes adicionais sobre o exame."><c:out value='${not empty relatorioEdicao ? relatorioEdicao.observacoes : ""}'/></textarea>

                                <div class="form-actions">
                                    <button type="submit" class="btn salvar">
                                        <c:choose>
                                            <c:when test="${not empty relatorioEdicao}">Atualizar Relatório</c:when>
                                            <c:otherwise>Salvar Relatório</c:otherwise>
                                        </c:choose>
                                    </button>
                                    <c:if test="${not empty relatorioEdicao}">
                                        <a class="btn cancelar" href="RelatorioSaudeController">Cancelar edição</a>
                                    </c:if>
                                </div>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="aviso-permissao">Apenas gestores e veterinários podem cadastrar ou editar relatórios. Utilize o histórico abaixo para consulta.</p>
                        </c:otherwise>
                    </c:choose>
            </section>

            <section class="card" id="historico" style="display: block;">
                    <div class="card-header">
                        <h2>Histórico de Relatórios</h2>
                        <form method="get" action="RelatorioSaudeController" class="filtro">
                            <input type="hidden" name="acao" value="historico">
                            <label for="animalFiltro">Filtrar por animal:</label>
                            <select id="animalFiltro" name="animalId">
                                <option value="">Todos</option>
                                <c:forEach var="animal" items="${animais}">
                                    <option value="${animal.id}"
                                            <c:if test="${animal.id == animalSelecionado}">selected</c:if>>
                                        ${animal.nome}
                                    </option>
                                </c:forEach>
                            </select>
                            <label for="statusFiltro">Status:</label>
                            <select id="statusFiltro" name="statusFiltro">
                                <option value="">Todos</option>
                                <option value="APTO" <c:if test="${statusSelecionado == 'APTO'}">selected</c:if>>Aptos</option>
                                <option value="INAPTO" <c:if test="${statusSelecionado == 'INAPTO'}">selected</c:if>>Inaptos</option>
                            </select>
                            <button type="submit" class="btn filtro-btn">Consultar Histórico</button>
                        </form>
                    </div>

                    <div class="tabela-wrapper">
                        <table>
                            <thead>
                                <tr>
                                    <th>Animal</th>
                                    <th>Data</th>
                                    <th>Peso</th>
                                    <th>Status</th>
                                    <th>Observações</th>
                                    <th class="acoes-header">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty relatorios}">
                                        <tr>
                                            <td colspan="6" class="sem-registros">Nenhum relatório encontrado.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="relatorio" items="${relatorios}">
                                            <tr>
                                                <td>${relatorio.animal.nome}</td>
                                                <td>${relatorio.dataRelatorioFormatado}</td>
                                                <td>
                                                <c:choose>
                                                        <c:when test="${not empty relatorio.peso}">
                                                            ${relatorio.peso} kg
                                                        </c:when>
                                                        <c:otherwise>
                                                            -
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${relatorio.status == 'APTO'}">
                                                            <span class="status-badge apto">Apto</span>
                                                        </c:when>
                                                        <c:when test="${relatorio.status == 'INAPTO'}">
                                                            <span class="status-badge inapto">Inapto</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="col-observacoes">
                                                    <c:out value='${relatorio.observacoes}'/>
                                                </td>
                                                <td class="acoes">
                                                    <div class="acoes-wrapper">
                                                        <c:choose>
                                                            <c:when test="${podeEditarRelatorio}">
                                                                <a class="btn editar" href="RelatorioSaudeController?acao=editar&id=${relatorio.id}">Editar</a>
                                                                <form method="post" action="RelatorioSaudeController" onsubmit="return confirm('Deseja realmente excluir este relatório?');">
                                                                    <input type="hidden" name="acao" value="excluir">
                                                                    <input type="hidden" name="relatorioId" value="${relatorio.id}">
                                                                    <button type="submit" class="btn excluir">Excluir</button>
                                                                </form>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="acao-restrita">Somente leitura</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
            </section>

            <section class="card" id="observacoes" style="display: block;">
                    <h2>Acrescentar Observações</h2>
                    <c:choose>
                        <c:when test="${podeEditarRelatorio}">
                            <form method="post" action="RelatorioSaudeController" class="formulario">
                                <input type="hidden" name="acao" value="adicionarObservacao">
                                <label>Relatório:</label>
                                <select name="relatorioObservacaoId" required>
                                    <option value="">Selecione um relatório</option>
                                    <c:forEach var="relatorio" items="${relatorios}">
                                        <option value="${relatorio.id}">
                                            ${relatorio.animal.nome} - ${relatorio.dataRelatorioFormatado}
                                        </option>
                                    </c:forEach>
                                </select>

                                <label>Nova Observação:</label>
                                <textarea name="novaObservacao" rows="3" placeholder="Informe o complemento desejado." required></textarea>

                                <button type="submit" class="btn salvar">Adicionar Observação</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="aviso-permissao">Observações adicionais podem ser registradas somente por gestores.</p>
                        </c:otherwise>
                    </c:choose>
            </section>
        </main>
    </body>
</html>
