package com.etc.HongRui.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.etc.HongRui.entiry.Invatition;
import com.etc.HongRui.entiry.User;
import com.etc.HongRui.service.BbsService;
import com.etc.HongRui.service.LoginService;
import com.etc.HongRui.util.Log;

/**
 * Servlet implementation class BbsMainServ
 */
@WebServlet("/BbsMainServ")
public class BbsMainServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BbsMainServ() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		BbsService service = new BbsService();

		// 使用三目表达式来表示，当当前页面为第一次打开的时候，因为没有当前页面传递，所以赋值为1，否则赋值为获取的页码
		String p = request.getParameter("page") == null ? "1" : request.getParameter("page");
		int page = Integer.parseInt(p);
		// 存储当前页数据
		request.setAttribute("page", page);
		// 存储下一页
		request.setAttribute("next", page + 1);
		// 存储上一页
		request.setAttribute("up", page - 1);
		int count = 0;
		// 获取到总页数
		try {
			count = service.getPage(10);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		request.setAttribute("pageall", count);
		// 获取页面传递过来的参数
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		if (request.getSession().getAttribute("user") != null) {
			// 以上判断条件表示曾经登录过
			// 所以需要获取到Cookie
			Cookie[] cookies = request.getCookies();
			Cookie cookie = null;
			// 遍历cookie对象，找到登录时创建的cookie
			for (int i = 0; i < cookies.length; i++) {
				// 判断获取的cookie是否是需要的cookie
				if (cookies[i].getName().equals("user")) {
					cookie = cookies[i];
					break;
				}
			}
			if (cookie != null) {
				// 将cookie的名字进行拆分
				String[] strings = cookie.getValue().split(",");
				LoginService service2 = new LoginService();
				try {
					User user = service2.login(strings[0], strings[1]);
					request.getSession().setAttribute("user", user);
				} catch (SQLException e) {
					Log.logger.debug(e.getMessage());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.logger.debug(e.getMessage());
				}
			}

		} else {
			request.getRequestDispatcher("login.jsp");
		}
		try {
			// 将从服务器中获取的数据进行存储,第一个参数表示的是自己任意起的名称,第二个参数表示的是存储到第一个参数的数据
			if (type.equals("wrtie")==true) {
				request.getRequestDispatcher("bbs/write.jsp").forward(request, response);
			} else {
				List<Invatition> list = service.getInvatition(page,10);
				service.cUtUname(list);
				request.setAttribute("list", list);
				request.getRequestDispatcher("bbs/index.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			Log.logger.debug(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.logger.debug(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
