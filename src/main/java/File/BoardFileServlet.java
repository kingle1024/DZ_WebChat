package File;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "BoardFileServlet", value = "/boardFile/*")
public class BoardFileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doGet > " + requestURI);

        switch (requestURI) {
            case "/boardFile/download":{
                String fileName = request.getParameter("filename");
                System.out.println("fileName:"+fileName);

                String path = "/Users/ejy1024/Documents/upload/";
                File file = new File(path + fileName);
                if(!file.exists()){
                    response.getWriter().append("파일이 존재하지 않음 > ")
                            .append(file.getAbsolutePath());
                    return;
                }
                OutputStream out = response.getOutputStream();
                response.setContentType(fileName.endsWith(".png") ? "image/png" : "application/octec-stream");
                response.setHeader("Cache-Control", "no-cache");
                response.addHeader("Content-disposition", "attachment; fileName="+fileName);

                InputStream in = new FileInputStream(file);
                byte[] data = new byte[4096];
                while(true){
                    int readCount = in.read(data);
                    if(readCount <= 0) break;
                    out.write(data, 0, readCount);
                }
                in.close();
                out.close();
                break;
            }

            default:
                System.out.println("requestURI : " + requestURI);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doPost > " + requestURI);

    }
}
