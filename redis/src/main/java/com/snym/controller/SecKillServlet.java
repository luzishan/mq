package com.snym.controller;

import com.snym.service.SecondKillService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * 秒杀servlet
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/25 1:20
 */
public class SecKillServlet extends HttpServlet {

    public static final long serialVersionUID = 1L;

    public SecKillServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String userid = new Random().nextInt(50000) + "";
        String prodid = request.getParameter("prodid");
        SecondKillService service = new SecondKillService();
        service.doSecondKill(userid, prodid);
    }
}
