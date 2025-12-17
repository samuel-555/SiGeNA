package sigena.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import sigena.model.dao.OcorrenciaDAO;
import sigena.model.domain.Ocorrencia;
import sigena.model.domain.util.OcorrenciaTipo;
import sigena.model.service.GestaoOcorrenciaService;
import sigena.model.domain.util.StatusOcorrencia;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/ocorrencias")
public class OcorrenciaController extends HttpServlet {

    private GestaoOcorrenciaService service;

    @Override
    public void init() {
        try {
            Connection con = sigena.model.util.ConexaoDB.getConnection();
            service = new GestaoOcorrenciaService(new OcorrenciaDAO(con));

            System.out.println(">>> Conexão criada via ConexaoDB");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao iniciar OcorrenciaController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        String tipo = request.getParameter("tipo");
        String status = request.getParameter("status");
        String texto = request.getParameter("texto");

        if (acao == null) {

            List<Ocorrencia> lista;

            if ((tipo != null && !tipo.isBlank())
                    || (status != null && !status.isBlank())
                    || (texto != null && !texto.isBlank())) {

                lista = service.buscarComFiltro(tipo, status, texto);

            } else {
                lista = service.listar();
            }

            request.setAttribute("ocorrencias", lista);
            request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
            return;
        }

        if ("editar".equals(acao)) {
            Long id = Long.parseLong(request.getParameter("id"));

            Ocorrencia oc = service.buscar(id);

            if (oc == null) {
                response.sendRedirect("ocorrencias");
                return;
            }

            if (oc.getStatus() == StatusOcorrencia.RESOLVIDO) {
                request.setAttribute("mensagemErro",
                        "Ocorrência resolvida não pode ser editada.");
                request.setAttribute("ocorrencias", service.listar());
                request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
                return;
            }

            List<Ocorrencia> lista = service.listar();
            request.setAttribute("ocorrencias", lista);
            request.setAttribute("ocorrenciaEdicao", oc);
            request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("ocorrencias");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {

            if ("cadastrar".equals(acao)) {

                Ocorrencia oc = montarOcorrencia(request);

                System.out.println("DEBUG OCORRENCIA:");
                System.out.println("Descricao: " + oc.getDescricao());
                System.out.println("Tipo: " + oc.getTipo());
                System.out.println("Status: " + oc.getStatus());
                System.out.println("Data: " + oc.getData());

                service.criar(oc);

                response.sendRedirect("ocorrencias");
                return;
            }

            if ("atualizar".equals(acao)) {

                Long id = Long.parseLong(request.getParameter("id"));
                Ocorrencia oc = montarOcorrencia(request);
                oc.setId(id);

                Ocorrencia atual = service.buscar(id);
                if (atual.getStatus() == StatusOcorrencia.RESOLVIDO) {
                    request.setAttribute("mensagemErro",
                            "Ocorrência resolvida não pode ser alterada.");
                    request.setAttribute("ocorrencias", service.listar());
                    request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
                    return;
                }

                service.atualizar(oc);

                response.sendRedirect("ocorrencias");
                return;
            }

            if ("cancelar".equals(acao)) {
                Long id = Long.parseLong(request.getParameter("id"));

                Ocorrencia oc = service.buscar(id);

                if (oc == null) {
                    response.sendRedirect("ocorrencias");
                    return;
                }

                if (oc.getStatus() == StatusOcorrencia.RESOLVIDO) {
                    request.setAttribute("mensagemErro",
                            "Ocorrência resolvida não pode ser cancelada.");
                    request.setAttribute("ocorrencias", service.listar());
                    request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
                    return;
                }

                service.cancelar(id);
                response.sendRedirect("ocorrencias");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("mensagemErro", "Erro ao processar ocorrência.");
            request.getRequestDispatcher("ocorrencias.jsp").forward(request, response);
        }
    }

    private Ocorrencia montarOcorrencia(HttpServletRequest request) {

        String tipoStr = request.getParameter("tipo");
        String descricao = request.getParameter("descricao");
        String dataStr = request.getParameter("data");
        String horaStr = request.getParameter("hora");
        String statusStr = request.getParameter("status");

        if (tipoStr == null || tipoStr.isBlank()) {
            throw new IllegalArgumentException("Tipo da ocorrência é obrigatório.");
        }

        if (dataStr == null || dataStr.isBlank()) {
            throw new IllegalArgumentException("Data da ocorrência é obrigatória.");
        }

        if (horaStr == null || horaStr.isBlank()) {
            throw new IllegalArgumentException("Hora da ocorrência é obrigatória.");
        }

        LocalDate data = LocalDate.parse(dataStr);
        LocalTime hora = LocalTime.parse(horaStr);

        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data da ocorrência não pode ser futura.");
        }

        Ocorrencia oc = new Ocorrencia();
        oc.setTipo(OcorrenciaTipo.valueOf(tipoStr));
        oc.setDescricao(descricao);
        oc.setData(LocalDateTime.of(data, hora));

        if (statusStr != null && !statusStr.isBlank()) {
            oc.setStatus(StatusOcorrencia.valueOf(statusStr));
        } else {
            oc.setStatus(StatusOcorrencia.PENDENTE);
        }

        return oc;
    }

}
