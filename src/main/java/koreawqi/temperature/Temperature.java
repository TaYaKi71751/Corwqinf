package koreawqi.temperature;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Temperature extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
  throws ServletException, IOException {
  PrintWriter out;

  res.setContentType("application/json; charset=utf-8");
  out = res.getWriter();

  // out.println("<html><body>");
  out.println(new koreawqi.temperature.Request().getTemperatureJSONString());
  // out.println("</body></html>");
  }
}