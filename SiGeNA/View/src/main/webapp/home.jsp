<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%@ page import="java.util.*" %>
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
    String cargoDescricao = cargo != null ? cargo.name().substring(0, 1).toUpperCase() + cargo.name().substring(1).toLowerCase() : "N\u00e3o informado";

    class Feature {
        String id;
        String label;
        String url;
        Feature(String id, String label, String url) {
            this.id = id;
            this.label = label;
            this.url = url;
        }
    }

    List<Feature> features = new ArrayList<>();
    features.add(new Feature("animais", "Gerenciamento de Animais", "AnimalController?acao=listar"));
    features.add(new Feature("especies", "Gerenciamento de Esp\u00e9cies", "EspeciesController"));
    features.add(new Feature("habitat", "Gerenciamento de Habitat", "HabitatController"));
    features.add(new Feature("tratamentos", "Gerenciamento de Tratamentos M\u00e9dicos", "tratamentos.jsp"));
    features.add(new Feature("planos", "Gerenciamento de Planos Alimentares", "PlanosAlimentaresController"));
    features.add(new Feature("produtos", "Gerenciamento de Produtos", "ProdutoController?acao=listar"));
    features.add(new Feature("relatorios", "Gerenciamento de Relat\u00f3rios de Sa\u00fade", "RelatorioSaudeController"));
    features.add(new Feature("enriquecimento", "Gerenciamento de Enriquecimento", "enriquecimento"));
    features.add(new Feature("visitantes", "Gerenciamento de Visitantes", "visitantes"));
    features.add(new Feature("doacoes", "Gest\u00e3o de Doa\u00e7\u00f5es", "doacoes"));
    features.add(new Feature("fornecedores", "Gest\u00e3o de Fornecedores", "FornecedorController?acao=listar"));
    features.add(new Feature("funcionarios", "Gerenciamento de Funcion\u00e1rios", "FuncionarioServlet"));

    Set<String> permitido = new LinkedHashSet<>();
    if (cargo == Cargo.GERENTE) {
        for (Feature f : features) permitido.add(f.id); // todos
    } else if (cargo == Cargo.ZOOTECNISTA) {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "planos", "produtos", "relatorios", "enriquecimento"));
    } else if (cargo == Cargo.VETERINARIO) {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "planos", "relatorios", "enriquecimento"));
    } else if (cargo == Cargo.TRATADOR) {
        permitido.addAll(Arrays.asList("animais", "especies", "habitat", "tratamentos", "visitantes", "planos", "produtos", "relatorios", "enriquecimento"));
    } else {
        for (Feature f : features) permitido.add(f.id); // fallback: mostra tudo
    }

    List<Feature> visiveis = new ArrayList<>();
    for (Feature f : features) {
        if (permitido.contains(f.id)) {
            visiveis.add(f);
        }
    }

    int tamanhoSlide = 4;
    int totalSlides = (int) Math.ceil(visiveis.size() / (double) tamanhoSlide);
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
        <title>SiGeNA</title>
    </head>
    <body>
        <header class="topbar">
            <div class="user-area">
                <div class="user-pill"><%= nomeUsuario %></div>
                <% if (cargoIcon != null) { %>
                    <img src="<%= cargoIcon %>" alt="Identificador do cargo" class="cargo-icon">
                <% } %>
            </div>
            <div class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo SiGeNA" class="brand-logo">
                <span>SiGeNA</span>
            </div>
            <a href="LogoutServlet" class="btn-sair">Sair</a>
        </header>

        <div class="welcome-banner">
            Bem-vindo, <%= nomeUsuario %>!
        </div>

        <div class="layout">
            <aside class="notificacoes">
                <h3>Notificacoes</h3>
                <div class="notif-card">
                    <p class="notif-title">Novo relatorio de saude disponivel</p>
                    <p class="notif-meta">Hoje, 14:30</p>
                </div>
                <div class="perfil-card">
                    <h4>Perfil</h4>
                    <ul>
                        <li><span>Nome:</span> <%= nomeUsuario %></li>
                        <li><span>Cargo:</span> <%= cargoDescricao %></li>
                        <li><span>Documento:</span> <%= sessao.getAttribute("CpfLogado") %></li>
                        <li><span>Turno:</span> Integral</li>
                    </ul>
                </div>
            </aside>

            <main class="home-gerente">
                <section class="hero">
                    <h1>Funcionalidades do Sistema</h1>
                    <% if (visiveis.isEmpty()) { %>
                        <p>Nenhuma funcionalidade dispon\u00edvel para seu perfil.</p>
                    <% } else { %>
                        <div class="carousel">
                            <% if (totalSlides > 1) { %>
                                <button class="nav-btn nav-left" type="button" onclick="mudarPagina(-1)">&#10094;</button>
                            <% } %>
                            <div class="carousel-window">
                                <div class="slides">
                                    <% for (int i = 0; i < totalSlides; i++) { %>
                                        <div class="slide <%= i == 0 ? "active" : "" %>">
                                            <% for (int j = i * tamanhoSlide; j < Math.min(visiveis.size(), (i + 1) * tamanhoSlide); j++) { 
                                                   Feature f = visiveis.get(j); %>
                                                <a href="<%= f.url %>" class="btn"><%= f.label %></a>
                                            <% } %>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                            <% if (totalSlides > 1) { %>
                                <button class="nav-btn nav-right" type="button" onclick="mudarPagina(1)">&#10095;</button>
                            <% } %>
                        </div>
                        <% if (totalSlides > 1) { %>
                        <div class="indicadores">
                            <% for (int i = 0; i < totalSlides; i++) { %>
                                <span class="<%= i == 0 ? "ativo" : "" %>"></span>
                            <% } %>
                        </div>
                        <% } %>
                        <h2 class="tarefas-title">TAREFAS</h2>
                    <% } %>
                </section>
            </main>
        </div>

        <footer class="footer" style="background:#1c1c1c;color:#f5f5f5;padding:16px 24px;margin-top:18px;box-shadow:0 -4px 12px rgba(0,0,0,0.18);">
            <div class="footer-content" style="display:flex;align-items:center;justify-content:flex-start;gap:12px;flex-wrap:wrap;">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo SiGeNA" style="height:48px;width:auto;display:block;">
                <div>
                    <p class="footer-title" style="font-weight:700;margin-bottom:4px;font-size:16px;">SiGeNA - Sistema de Gerenciamento de Animais</p>
                    <p class="footer-text" style="font-size:14px;color:#d0d0d0;">Informacoes institucionais e links uteis ficam disponiveis aqui.</p>
                </div>
                <div class="footer-brand" style="font-weight:700;font-size:15px;letter-spacing:0.3px;margin-left:auto;">CEFET-MG, Curso de Informatica 2° ano, 2025</div>
            </div>
        </footer>

        <script>
            const slides = document.querySelectorAll('.slide');
            const indicadores = document.querySelectorAll('.indicadores span');
            let paginaAtual = 0;

            function atualizarSlide() {
                slides.forEach((slide, index) => {
                    slide.classList.toggle('active', index === paginaAtual);
                    if (indicadores[index]) {
                        indicadores[index].classList.toggle('ativo', index === paginaAtual);
                    }
                });
            }

            function mudarPagina(direcao) {
                paginaAtual = (paginaAtual + direcao + slides.length) % slides.length;
                atualizarSlide();
            }
        </script>
    </body>
</html>
