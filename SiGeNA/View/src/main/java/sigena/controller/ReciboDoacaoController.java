package sigena.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import sigena.model.dao.ReciboDoacaoDAO;
import sigena.model.service.ReciboDoacaoService;
import sigena.model.domain.ReciboDoacao;

import java.io.IOException;

@WebServlet("/ReciboDoacaoServlet")
public class ReciboDoacaoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        try {
            // DAO agora segue o padrão do projeto (sem Connection no construtor)
            ReciboDoacaoDAO reciboDAO = new ReciboDoacaoDAO();

            // Service recebe apenas o DAO
            ReciboDoacaoService service = new ReciboDoacaoService(reciboDAO);

            if ("emitir".equals(acao)) {
                Long doacaoId = Long.valueOf(req.getParameter("id"));
                ReciboDoacao r = service.emitirRecibo(doacaoId);

                req.setAttribute("recibo", r);
                RequestDispatcher rd = req.getRequestDispatcher("recibo.jsp");
                rd.forward(req, resp);
                return;
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
