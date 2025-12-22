<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SiGeNA - Histórico de Atividades</title>
    <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    <style>
        .history-container { padding: 40px 50px; max-width: 1400px; margin: 0 auto; }
        .search-section { margin-bottom: 30px; display: flex; flex-direction: column; gap: 15px; }
        .main-search {
            width: 100%; padding: 15px 25px; border-radius: 30px; border: 1px solid #e0e0e0;
            font-family: 'Montserrat', sans-serif; font-size: 16px; outline: none; transition: 0.3s;
            box-shadow: var(--shadow);
        }
        .main-search:focus { border-color: var(--zoo-dark-green); box-shadow: 0 0 0 4px rgba(0,89,63,0.1); }
        
        .cards-grid { display: grid; grid-template-columns: repeat(auto-fill, min-minmax(450px, 1fr)); gap: 30px; align-items: start; }
        .worker-card { 
            background: white; border-radius: 20px; box-shadow: var(--shadow); padding: 25px; 
            border: 1px solid #f0f0f0; transition: transform 0.3s;
        }
        .worker-card:hover { transform: translateY(-5px); }
        .worker-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 2px solid var(--zoo-mint); padding-bottom: 15px; }
        .worker-info h3 { color: var(--zoo-dark-green); font-size: 18px; font-weight: 800; margin: 0; }
        .worker-info span { font-size: 12px; color: #888; text-transform: uppercase; font-weight: 600; }
        
        .inner-search {
            width: 100%; padding: 8px 15px; border-radius: 10px; border: 1px solid #eee;
            margin-bottom: 15px; font-size: 13px; outline: none;
        }

        .table-wrapper { max-height: 400px; overflow-y: auto; border-radius: 10px; }
        .mini-table { width: 100%; border-collapse: collapse; font-size: 13px; }
        .mini-table th { position: sticky; top: 0; background: #f9f9f9; padding: 12px; text-align: left; font-size: 11px; color: #999; text-transform: uppercase; }
        .mini-table td { padding: 12px; border-bottom: 1px solid #f0f0f0; color: var(--text-gray); }
        .type-tag { padding: 3px 8px; border-radius: 5px; font-size: 10px; font-weight: 700; text-transform: uppercase; }
        .tag-update { background: #e3f2fd; color: #1976d2; }
        .tag-create { background: #e8f5e9; color: #2e7d32; }
        .tag-delete { background: #ffebee; color: #c62828; }
    </style>
</head>
<body>
    <header class="topbar">
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

    <div class="history-container">
        <section class="search-section">
            <h2 style="font-weight: 800; color: var(--zoo-dark-green);">Histórico de Atividades</h2>
            <form id="formBusca" action="HistoricoController" method="get">
                <input type="hidden" name="acao" value="buscar">
                <input type="text" name="q" class="main-search" placeholder="Pesquisar funcionário ou cargo no banco de dados..." value="${param.q}">
            </form>
        </section>

        <div class="cards-grid">
            <c:forEach var="entry" items="${historicoMap}">
                <c:set var="lista" value="${entry.value}" />
                <c:set var="primeiro" value="${lista[0]}" />
                
                <div class="worker-card" data-worker-name="${primeiro.nomeFuncionario.toLowerCase()}">
                    <div class="worker-header">
                        <div class="worker-info">
                            <h3>${primeiro.nomeFuncionario}</h3>
                            <span>${primeiro.cargoFuncionario}</span>
                        </div>
                        <div style="background: var(--zoo-mint); padding: 8px; border-radius: 50%;">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="var(--zoo-dark-green)"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
                        </div>
                    </div>

                    <input type="text" class="inner-search" placeholder="Filtrar atividades deste funcionário..." onkeyup="filtrarTabela(this)">

                    <div class="table-wrapper">
                        <table class="mini-table">
                            <thead>
                                <tr>
                                    <th>Tipo</th>
                                    <th>Descrição</th>
                                    <th>Data</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="hist" items="${lista}">
                                    <tr>
                                        <td>
                                            <span class="type-tag ${hist.tipo == 'ALTERACAO' ? 'tag-update' : (hist.tipo == 'INSERCAO' ? 'tag-create' : 'tag-delete')}">
                                                ${hist.tipo}
                                            </span>
                                        </td>
                                        <td class="desc-cell">${hist.descricao}</td>
                                        <td style="white-space: nowrap;">
                                            <fmt:formatDate value="${hist.data}" pattern="dd/MM HH:mm"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <script>
        function filtrarTabela(input) {
            const filter = input.value.toLowerCase();
            const table = input.nextElementSibling.querySelector(".mini-table");
            const tr = table.getElementsByTagName("tr");

            for (let i = 1; i < tr.length; i++) {
                const tdDesc = tr[i].getElementsByClassName("desc-cell")[0];
                if (tdDesc) {
                    const txtValue = tdDesc.textContent || tdDesc.innerText;
                    tr[i].style.display = txtValue.toLowerCase().indexOf(filter) > -1 ? "" : "none";
                }
            }
        }

        let timer;
        document.querySelector('.main-search').addEventListener('keyup', function() {
            clearTimeout(timer);
            timer = setTimeout(() => {
                document.getElementById("formBusca").submit();
            }, 800);
        });
    </script>
</body>
</html>