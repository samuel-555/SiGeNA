package sigena.controller;

import sigena.model.domain.Doacao;
import sigena.model.domain.util.DoacaoTipo;
import sigena.model.service.GestaoDoacaoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.time.LocalDate;
import sigena.model.domain.util.StatusDoacao;
import java.util.List;

@WebServlet("/doacoes")
public class DoacoesController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GestaoDoacaoService service = new GestaoDoacaoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            if ("editar".equals(acao)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Doacao d = service.buscarPorId(id);

                req.setAttribute("doacao", d);
                req.getRequestDispatcher("cadastrar-doacao.jsp")
                        .forward(req, resp);
                return;
            }

            String doador = req.getParameter("doador");
            String tipoStr = req.getParameter("tipo");
            String dataStr = req.getParameter("data");
            String reciboStr = req.getParameter("recibo");
            String valorDescricao = req.getParameter("valorDescricao");

            List<Doacao> lista = service.listarDoacoes();

            if (doador != null && !doador.isBlank()) {
                lista = lista.stream()
                        .filter(d -> d.getNomeDoador() != null
                        && d.getNomeDoador().toLowerCase().contains(doador.toLowerCase()))
                        .toList();
            }

            if (tipoStr != null && !tipoStr.isBlank()) {
                DoacaoTipo tipo = DoacaoTipo.fromString(tipoStr);
                lista = lista.stream()
                        .filter(d -> d.getTipo() == tipo)
                        .toList();
            }

            if (dataStr != null && !dataStr.isBlank()) {
                LocalDate data = LocalDate.parse(dataStr);
                lista = lista.stream()
                        .filter(d -> data.equals(d.getDataDoacao()))
                        .toList();
            }

            if (reciboStr != null && !reciboStr.isBlank()) {
                boolean temRecibo = reciboStr.equalsIgnoreCase("SIM");

                lista = lista.stream()
                        .filter(d -> d.isReciboEmitido() == temRecibo)
                        .toList();
            }

            if (valorDescricao != null && !valorDescricao.isBlank()) {
                String vd = valorDescricao.toLowerCase();

                lista = lista.stream()
                        .filter(d -> {
                            if (d.isMonetaria() && d.getValorMonetario() != null) {
                                return String.valueOf(d.getValorMonetario()).contains(vd);
                            }
                            if (!d.isMonetaria() && d.getDescricaoOutro() != null) {
                                return d.getDescricaoOutro().toLowerCase().contains(vd);
                            }
                            return false;
                        })
                        .toList();
            }

            req.setAttribute("doacoes", lista);
            req.getRequestDispatcher("doacoes.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException("Erro no controller: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {

            if ("cadastrar".equals(acao)) {
                Doacao d;
                try {
                    d = construirDoacao(req, false);
                } catch (IllegalArgumentException e) {
                    req.setAttribute("erro", e.getMessage());
                    req.getRequestDispatcher("cadastrar-doacao.jsp").forward(req, resp);
                    return;
                }
                service.registrarDoacao(d);
                resp.sendRedirect("doacoes?acao=listar");
                return;
            }

            if ("atualizar".equals(acao)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Doacao doacaoExistente = service.buscarPorId(id);

                Doacao d;
                try {
                    d = construirDoacao(req, true);
                } catch (IllegalArgumentException e) {
                    req.setAttribute("erro", e.getMessage());
                    req.setAttribute("doacao", doacaoExistente);
                    req.getRequestDispatcher("cadastrar-doacao.jsp").forward(req, resp);
                    return;
                }

                if (doacaoExistente.getTipo() == DoacaoTipo.MONETARIA) {
                    service.atualizarValor(d.getId(), d.getValorMonetario());
                } else if (doacaoExistente.getTipo() == DoacaoTipo.OUTRO) {
                    String descricao = (d.getDescricaoOutro() == null || d.getDescricaoOutro().isBlank())
                            ? doacaoExistente.getDescricaoOutro()
                            : d.getDescricaoOutro();
                    service.atualizarDescricao(d.getId(), descricao);
                }

                resp.sendRedirect("doacoes?acao=listar");
                return;
            }

            if ("cancelar".equals(acao)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.cancelarDoacao(id);
                resp.sendRedirect("doacoes?acao=listar");
                return;
            }

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher("cadastrar-doacao.jsp").forward(req, resp);
        }

    }

    private Doacao construirDoacao(HttpServletRequest req, boolean atualizar) {

        Doacao d = new Doacao();

        if (atualizar) {
            d.setId(Long.parseLong(req.getParameter("id")));
        }

        d.setNomeDoador(req.getParameter("doador"));
        d.setObservacoes(req.getParameter("observacoes"));

        String tipoStr = req.getParameter("tipoDoacao");
        DoacaoTipo tipo = DoacaoTipo.fromString(tipoStr);
        d.setTipo(tipo);

        if (tipo == DoacaoTipo.MONETARIA) {
            String v = req.getParameter("valor");
            if (v != null && !v.isBlank()) {
                v = v.replace(".", "").replace(",", ".");

                double valor = Double.parseDouble(v);

                if (valor <= 0) {
                    throw new IllegalArgumentException("O valor da doação deve ser maior que zero.");
                }

                if (valor > 99999999.99) {
                    throw new IllegalArgumentException("Valor da doação excede o limite permitido.");
                }

                d.setValorMonetario(valor);
            }

            d.setDescricaoOutro(null);
        } else {
            d.setValorMonetario(null);
            d.setDescricaoOutro(req.getParameter("descricaoOutro"));
        }

        String dataStr = req.getParameter("data");
        if (dataStr != null && !dataStr.isBlank()) {

            if (!dataStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                throw new IllegalArgumentException("Data inválida: " + dataStr);
            }

            try {
                LocalDate dataDoacao = LocalDate.parse(dataStr);

                int ano = dataDoacao.getYear();
                if (ano < 1900 || ano > LocalDate.now().getYear()) {
                    throw new IllegalArgumentException("Ano da doação inválido: " + dataStr);
                }

                d.setDataDoacao(dataDoacao);
            } catch (Exception e) {
                throw new IllegalArgumentException("Data inválida: " + dataStr, e);
            }
        }

        d.setStatus(StatusDoacao.ATIVA);
        d.setReciboEmitido(false);

        return d;
    }

}