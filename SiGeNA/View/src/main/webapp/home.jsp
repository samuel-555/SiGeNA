<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.util.Cargo" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Cargo cargo = (Cargo) sessao.getAttribute("cargoUsuario");
    String nomeUsuario = sessao.getAttribute("NomeLogado") != null
            ? sessao.getAttribute("NomeLogado").toString()
            : String.valueOf(sessao.getAttribute("CpfLogado"));

    String cargoIcon = null;
    if (cargo != null) {
        switch (cargo) {
            case GERENTE -> cargoIcon = "IMG's/Identificador-gerente.png";
            case ZOOTECNISTA -> cargoIcon = "IMG's/Identificador-zootecnista.png";
            case VETERINARIO -> cargoIcon = "IMG's/Identificador-veterinario.png";
            case TRATADOR -> cargoIcon = "IMG's/Identificador-tratador.png";
            default -> cargoIcon = null;
        }
    }
    String cargoDescricao = cargo != null ? cargo.name().substring(0, 1).toUpperCase() + cargo.name().substring(1).toLowerCase() : "Não informado";

    class Feature {
        String id; String label; String url;
        Feature(String id, String label, String url) { this.id = id; this.label = label; this.url = url; }
    }

    List<Feature> features = new ArrayList<>();
    features.add(new Feature("animais", "Gerenciamento de Animais", "AnimalController?acao=listar"));
    features.add(new Feature("especies", "Gerenciamento de Espécies", "EspeciesController"));
    features.add(new Feature("habitat", "Gerenciamento de Habitat", "HabitatController"));
    features.add(new Feature("tratamentos", "Gerenciamento de Tratamentos Médicos", "tratamentos.jsp"));
    features.add(new Feature("planos", "Gerenciamento de Planos Alimentares", "PlanosAlimentaresController"));
    features.add(new Feature("produtos", "Gerenciamento de Produtos", "ProdutoController?acao=listar"));
    features.add(new Feature("relatorios", "Gerenciamento de Relatórios de Saúde", "RelatorioSaudeController"));
    features.add(new Feature("enriquecimento", "Gerenciamento de Enriquecimento", "enriquecimento"));
    features.add(new Feature("visitantes", "Gerenciamento de Visitantes", "visitantes"));
    features.add(new Feature("doacoes", "Gestão de Doações", "doacoes"));
    features.add(new Feature("fornecedores", "Gestão de Fornecedores", "FornecedorController?acao=listar"));
    features.add(new Feature("funcionarios", "Gerenciamento de Funcionários", "FuncionarioServlet"));
    features.add(new Feature("agendamentos", "Gestão de Agendamentos", "AgendamentoController?acao=listar"));
    features.add(new Feature("ocorrencias", "Ocorrências", "ocorrencias.jsp"));
    features.add(new Feature("historico", "Histórico", "HistoricoController?acao=buscar"));
    features.add(new Feature("eventos", "Eventos", "EventoController?acao=listar"));

    Set<String> permitido = new LinkedHashSet<>();
    if (cargo == Cargo.GERENTE) {
        for (Feature f : features) permitido.add(f.id); 
    } else if (cargo == Cargo.ZOOTECNISTA) {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "planos", "produtos", "relatorios", "enriquecimento"));
    } else if (cargo == Cargo.VETERINARIO) {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "planos", "relatorios", "enriquecimento"));
    } else if (cargo == Cargo.TRATADOR) {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "visitantes", "planos", "produtos", "relatorios", "enriquecimento", "agendamentos"));
    } else {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "planos", "produtos", "relatorios"));
    }

    List<Feature> visiveis = new ArrayList<>();
    for (Feature f : features) { if (permitido.contains(f.id)) visiveis.add(f); }

    int tamanhoSlide = 4;
    int totalSlides = (int) Math.ceil(visiveis.size() / (double) tamanhoSlide);
%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="CSS/stylehome.css">
    <title>SiGeNA - Painel Administrativo</title>
</head>
<body>
    <audio id="clickSound" src="https://assets.mixkit.co/active_storage/sfx/2568/2568-preview.mp3" preload="auto"></audio>
    <audio id="notifSound" src="https://assets.mixkit.co/active_storage/sfx/2357/2357-preview.mp3" preload="auto"></audio>

    <header class="topbar">
        <a href="home.jsp" class="titulo">
            <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
            <span>SiGeNA</span>
        </a>

        <div class="header-info-center">
            <div class="header-box"><span class="h-label">Cargo</span><span class="h-value"><%= cargoDescricao %></span></div>
            <div class="header-box"><span class="h-label">CPF</span><span class="h-value"><%= sessao.getAttribute("CpfLogado") %></span></div>
            <div class="header-box"><span class="h-label">Turno</span><span class="h-value">Integral</span></div>
        </div>

        <div class="user-area">
            <div class="notif-wrapper">
                <button class="notif-button" onclick="toggleNotifs()">
                    <svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor"><path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"/></svg>
                    <c:if test="${not empty tarefas or not empty tarefasAtrasadas}">
                        <span class="notif-badge">!</span>
                    </c:if>
                </button>
                <div class="notif-dropdown" id="notifDropdown">
                    <div class="notif-header">Notificações</div>
                    <div class="notif-body">
                         <c:forEach var="n" items="${notificacoes}">
                 <div class="notificacao ${n.lida ? 'lida' : 'nao-lida'}">

                    <c:if test="${!n.lida}">
                        <form id="form-${n.id}" action="NotificacaoController" method="post">
                            <input type="hidden" name="id" value="${n.id}">
                        </form>
                        <a href="#" onclick="document.getElementById('form-${n.id}').submit(); return false;">
                            ${n.titulo}
                        </a>
                    </c:if>

                    <c:if test="${n.lida}">
                        <p>${n.titulo}</p>
                    </c:if>

                </div>
                </c:forEach>
                    </div>
                </div>
            </div>
            <div class="user-pill"><%= nomeUsuario %></div>
            <% if (cargoIcon != null) { %><img src="<%= cargoIcon %>" alt="Cargo" class="cargo-icon"><% } %>
            <a href="LogoutServlet" class="btn-sair">Sair</a>
        </div>
    </header>

    <section class="welcome-hero">
        <h1 class="hero-text">Bem-vindo de volta, <span><%= nomeUsuario %>!</span></h1>
    </section>

    <div class="carousel-container-full">
        <div class="carousel">
            <% if (totalSlides > 1) { %><button class="nav-btn" onclick="mudarPagina(-1)">&#10094;</button><% } %>
            <div class="carousel-window">
                <div class="slides">
                    <% for (int i = 0; i < totalSlides; i++) { %>
                        <div class="slide <%= i == 0 ? "active" : "" %>">
                            <% for (int j = i * tamanhoSlide; j < Math.min(visiveis.size(), (i + 1) * tamanhoSlide); j++) { 
                                Feature f = visiveis.get(j); %>
                                <a href="<%= f.url %>" class="btn" data-id="<%= f.id %>">
                                    <div class="btn-icon"></div>
                                    <span><%= f.label %></span>
                                </a>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            </div>
            <% if (totalSlides > 1) { %><button class="nav-btn" onclick="mudarPagina(1)">&#10095;</button><% } %>
        </div>
        <% if (totalSlides > 1) { %>
            <div class="indicadores">
                <% for (int i = 0; i < totalSlides; i++) { %>
                    <span class="<%= i == 0 ? "ativo" : "" %>" onclick="irParaPagina(<%= i %>)"></span>
                <% } %>
            </div>
        <% } %>
    </div>

    <main class="content-wrapper">
        <c:if test="${not empty tarefasAtrasadas}">
            <section class="tarefas-section" style="padding-top: 20px;">
                <h2 class="tarefas-title" style="color: #dc2626;">⚠ Tarefas Atrasadas</h2>
                <div class="tarefas-container" style="border-left: 5px solid #dc2626;">
                    <table class="modern-table">
                        <tbody>
                            <c:forEach var="tarefa" items="${tarefasAtrasadas}">
                                <tr>
                                    <td class="task-name" style="color: #dc2626;">${tarefa.nome}</td>
                                    <td>${tarefa.texto}</td>
                                    <td style="color: #dc2626; font-weight: bold;">Vencimento: ${tarefa.dataPConclusao}</td>
                                    <td>
                                        <form method="post" action="TarefaController">
                                            <input type="hidden" name="acao" value="concluir">
                                            <input type="hidden" name="id" value="${tarefa.id}">
                                            <input type="hidden" name="status" value="true">
                                            <input type="checkbox" class="task-checkbox" onchange="confirmarConclusao(this, '${tarefa.nome}')">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:if>

        <section class="tarefas-section">
            <div class="tarefas-header">
                <h2 class="tarefas-title">Tarefas do Dia</h2>
                <c:if test="${sessionScope.cargoUsuario.name() eq 'GERENTE'}">
                    <a href="TarefaController?acao=cadastrar" class="btn-primary">Criar Nova Tarefa</a>
                </c:if>
            </div>
            <div class="tarefas-container">
                <c:choose>
                    <c:when test="${empty tarefas}">
                        <div style="padding: 40px; text-align: center; color: #999;"><p>Não há tarefas para hoje.</p></div>
                    </c:when>
                    <c:otherwise>
                        <table class="modern-table">
                            <thead>
                                <tr><th>Nome</th><th>Descrição</th><th>Prazo</th><th>Status</th><th style="text-align:right">Ações</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="tarefa" items="${tarefas}">
                                    <tr class="${tarefa.concluida ? 'row-done' : ''}">
                                        <td class="task-name">${tarefa.nome}</td>
                                        <td>${tarefa.texto}</td>
                                        <td>${tarefa.dataPConclusao}</td>
                                        <td>
                                            <form method="post" action="TarefaController">
                                                <input type="hidden" name="acao" value="concluir">
                                                <input type="hidden" name="id" value="${tarefa.id}">
                                                <input type="hidden" name="status" value="true">
                                                <input type="checkbox" class="task-checkbox" 
                                                       <c:if test="${tarefa.concluida}">checked disabled</c:if>
                                                       onchange="confirmarConclusao(this, '${tarefa.nome}')">
                                            </form>
                                        </td>
                                        <td style="text-align:right">
                                            <c:choose>
                                                <c:when test="${!tarefa.concluida}">
                                                    <c:if test="${sessionScope.cargoUsuario.name() eq 'GERENTE'}">
                                                        <a href="TarefaController?acao=editar&id=${tarefa.id}" class="btn-action editar">Editar</a>
                                                        <form action="TarefaController" method="POST" style="display:inline;" onsubmit="return confirm('Excluir esta tarefa?')">
                                                            <input type="hidden" name="acao" value="excluir">
                                                            <input type="hidden" name="id" value="${tarefa.id}">
                                                            <button type="submit" class="btn-action excluir" style="border:none; background:none; color:#e03131; cursor:pointer; font-weight:700;">EXCLUIR</button>
                                                        </form>
                                                    </c:if>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="font-size: 10px; color: #2ecc71; font-weight: 800; text-transform: uppercase;">✔ Concluída</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>

    <footer class="footer-dark">
        <img src="IMG's/logoSiGeNA-Branco.png" alt="Logo" class="footer-logo">
        <p>© 2025 SiGeNA - CEFET-MG Informática</p>
    </footer>

    <script>
        const slides = document.querySelectorAll('.slide');
        const indicadores = document.querySelectorAll('.indicadores span');
        let paginaAtual = 0;
        let autoPlayInterval;

        function atualizarSlide() {
            slides.forEach((s, i) => {
                s.classList.toggle('active', i === paginaAtual);
                if (indicadores[i]) indicadores[i].classList.toggle('ativo', i === paginaAtual);
            });
        }
        function mudarPagina(dir) { paginaAtual = (paginaAtual + dir + slides.length) % slides.length; atualizarSlide(); resetTimer(); }
        function irParaPagina(idx) { paginaAtual = idx; atualizarSlide(); resetTimer(); }
        function startTimer() { autoPlayInterval = setInterval(() => { paginaAtual = (paginaAtual + 1) % slides.length; atualizarSlide(); }, 5000); }
        function resetTimer() { clearInterval(autoPlayInterval); startTimer(); }
        
        function confirmarConclusao(checkbox, nomeTarefa) {
            if (checkbox.checked) {
                if (confirm("Deseja concluir '" + nomeTarefa + "'? Após isso, não será possível editar ou excluir.")) {
                    const sound = document.getElementById('clickSound');
                    if(sound) sound.play();
                    setTimeout(() => checkbox.form.submit(), 300);
                } else {
                    checkbox.checked = false;
                }
            }
        }

        function toggleNotifs() { 
            const d = document.getElementById('notifDropdown');
            d.classList.toggle('active');
            if(d.classList.contains('active')) {
                const s = document.getElementById('notifSound');
                if(s) s.play();
            }
        }
        window.onclick = function(event) { if (!event.target.closest('.notif-wrapper')) { document.getElementById('notifDropdown').classList.remove('active'); } }
        startTimer();
    </script>
</body>
</html>
