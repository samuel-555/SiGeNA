package sigena.controller;

import sigena.model.domain.Doacao;
import sigena.model.domain.DoacaoTipo;
import sigena.model.service.GestaoDoacaoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.time.LocalDate;
import sigena.model.domain.StatusDoacao;
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

            // Ação padrão: listar todas as doações
            List<Doacao> lista = service.listarDoacoes();
            req.setAttribute("doacoes", lista);  // manter padrão com "doacoes"
            req.getRequestDispatcher("doacoes.jsp")
                    .forward(req, resp);

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
                Doacao d = construirDoacao(req, false);
                service.registrarDoacao(d);

                // Redireciona para o GET que lista todas
                resp.sendRedirect("doacoes?acao=listar");
                return;
            }

            if ("atualizar".equals(acao)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Doacao doacaoExistente = service.buscarPorId(id);

                Doacao d = construirDoacao(req, true);

                // Atualização depende do tipo real da doação no banco
                if (doacaoExistente.getTipo() == DoacaoTipo.MONETARIA) {
                    // Atualiza valor apenas se for monetária
                    service.atualizarValor(d.getId(), d.getValorMonetario());
                } else if (doacaoExistente.getTipo() == DoacaoTipo.OUTRO) {
                    // Atualiza descrição apenas se for tipo OUTRO
                    String descricao = (d.getDescricaoOutro() == null || d.getDescricaoOutro().isBlank())
                            ? doacaoExistente.getDescricaoOutro()
                            : d.getDescricaoOutro();

                    service.atualizarDescricao(d.getId(), descricao);
                }
                // Outros tipos não têm atualização específica

                // Redireciona para a listagem após atualização
                resp.sendRedirect("doacoes?acao=listar");
                return;
            }

        } catch (Exception e) {
            throw new ServletException("Erro ao processar doação: " + e.getMessage(), e);
        }
    }

    private Doacao construirDoacao(HttpServletRequest req, boolean atualizar) {

        Doacao d = new Doacao();

        if (atualizar) {
            d.setId(Long.parseLong(req.getParameter("id")));
        }

        d.setNomeDoador(req.getParameter("doador"));
        d.setObservacoes(req.getParameter("observacoes"));

        // Tipo
        String tipoStr = req.getParameter("tipoDoacao");
        DoacaoTipo tipo = DoacaoTipo.fromString(tipoStr);
        d.setTipo(tipo);

        // Tipo monetário
        if (tipo == DoacaoTipo.MONETARIA) {
            String v = req.getParameter("valor");
            if (v != null && !v.isBlank()) {
                v = v.replace(".", "").replace(",", ".");
                d.setValorMonetario(Double.parseDouble(v));
            }

            d.setDescricaoOutro(null);
        } else {
            d.setValorMonetario(null);
            d.setDescricaoOutro(req.getParameter("descricaoOutro"));
        }

        String dataStr = req.getParameter("data");
        if (dataStr != null && !dataStr.isBlank()) {
            d.setDataDoacao(LocalDate.parse(dataStr));
        }

        d.setStatus(StatusDoacao.ATIVA);
        d.setReciboEmitido(false);

        return d;
    }
}
