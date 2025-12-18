<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.util.Cargo" %>
<%@ page import="sigena.model.domain.Doacao" %>
<%@ page import="sigena.model.domain.ReciboDoacao" %>

<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    if (cargo == null) {
        response.sendRedirect("home.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SiGeNA - Doações</title>
    <link rel="stylesheet" href="CSS/style.css">
    <link rel="stylesheet" href="CSS/stylefuncionario.css">
</head>
<body>

<header>
    <div class="titulo">
        <a href="<%= request.getContextPath() + (cargo == Cargo.GERENTE ? "/home-gerente.jsp" : "/home.jsp") %>">
            SiGeNA
        </a>
    </div>
</header>

<div class="container">
    <h1>Gestão de Doações</h1>

    <div class="botoes-acoes">
        <a href="cadastrar-doacao.jsp" class="btn">Cadastrar Nova Doação</a>
    </div>

    <form method="get" action="doacoes" class="filtro">
        <input type="text" name="doador" placeholder="Nome do doador" value="${param.doador}" />

        <select name="tipo">
            <option value="">Todos os tipos</option>
            <option value="MONETARIA" ${param.tipo == 'MONETARIA' ? 'selected' : ''}>Monetária</option>
            <option value="OUTRO" ${param.tipo == 'OUTRO' ? 'selected' : ''}>Outro</option>
        </select>

        <input type="text" name="valorDescricao" placeholder="Valor ou descrição" value="${param.valorDescricao}" />
        <input type="date" name="data" value="${param.data}" />

        <select name="recibo">
            <option value="">Com ou sem recibo</option>
            <option value="SIM" ${param.recibo == 'SIM' ? 'selected' : ''}>Somente com recibo</option>
            <option value="NAO" ${param.recibo == 'NAO' ? 'selected' : ''}>Somente sem recibo</option>
        </select>

        <button type="submit" class="btn">Pesquisar</button>
    </form>

    <div class="historico">
        <h2>Lista de Doações</h2>

        <table>
            <thead>
                <tr>
                    <th>Doador</th>
                    <th>Tipo</th>
                    <th>Valor / Descrição</th>
                    <th>Observações</th>
                    <th>Data</th>
                    <th>Recibo</th>
                    <th>Ações</th>
                </tr>
            </thead>

            <tbody>
            <%
                java.util.List<Doacao> lista = (java.util.List<Doacao>) request.getAttribute("doacoes");
                if (lista != null) {
                    for (Doacao d : lista) {
            %>
                <tr>
                    <td><%= d.getNomeDoador() %></td>
                    <td><%= d.getTipo().name() %></td>

                    <td>
                        <%
                            if (d.isMonetaria()) {
                                out.print(d.getValorMonetario() != null
                                    ? String.format("R$ %.2f", d.getValorMonetario())
                                    : "-");
                            } else {
                                out.print(d.getDescricaoOutro() != null ? d.getDescricaoOutro() : "-");
                            }
                        %>
                    </td>

                    <td>
                        <%
                            String obs = d.getObservacoes();
                            if (obs != null && !obs.isBlank()) {
                        %>
                            <button type="button" class="btn-pequeno"
                                    onclick="toggleObs('obs-<%= d.getId() %>')">
                                Ver
                            </button>

                            <div id="obs-<%= d.getId() %>"
                                 style="display:none; margin-top:6px; background:#f5f5f5; padding:6px; border-radius:4px;">
                                <%= obs %>
                            </div>
                        <%
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>

                    <td><%= d.getDataDoacao() != null ? d.getDataDoacao() : "-" %></td>

                    <td>
                        <%
                            if (d.isReciboEmitido()) {
                                out.print("Emitido");
                            } else {
                                out.print("-");
                            }
                        %>
                    </td>

                    <td>
                        <a class="btn-pequeno editar" href="doacoes?acao=editar&id=<%= d.getId() %>">Editar</a>

                        <form action="doacoes" method="post" style="display:inline"
                              onsubmit="return confirm('Confirmar cancelamento?');">
                            <input type="hidden" name="acao" value="cancelar"/>
                            <input type="hidden" name="id" value="<%= d.getId() %>"/>
                            <button class="btn-pequeno excluir" type="submit">Excluir</button>
                        </form>
                    </td>
                </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>

    <%
        Doacao ed = (Doacao) request.getAttribute("doacaoEdicao");
        if (ed != null) {
    %>
        <div class="editar-bloco" style="margin-top:20px;">
            <h2>Editando Doação: ID <%= ed.getId() %></h2>

            <form action="doacoes" method="post">
                <input type="hidden" name="acao" value="atualizar" />
                <input type="hidden" name="id" value="<%= ed.getId() %>" />

                <label>Tipo:</label>
                <input type="text" value="<%= ed.getTipo().name() %>" readonly />

                <button type="submit" class="btn">Salvar Alterações</button>
                <a href="doacoes" class="btn cancelar">Voltar</a>
            </form>
        </div>
    <%
        }
    %>
</div>

<script>
    function toggleObs(id) {
        const el = document.getElementById(id);
        el.style.display = (el.style.display === "none" || el.style.display === "")
            ? "block"
            : "none";
    }
</script>

</body>
</html>
