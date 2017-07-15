package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.CrudDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.restaurant;

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

	private CrudDao dao;

	public Controller() {
		dao = new CrudDao();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		List<restaurant> restaurantList = new ArrayList<restaurant>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		response.setContentType("application/json");

		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from restaurant Table
					restaurantList = dao.getAllrestaurants();

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", restaurantList);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);

					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					restaurant restaurant = new restaurant();
					if (request.getParameter("Id") != null) {
						int restaurantId = Integer.parseInt(request.getParameter("Id"));
						restaurant.setId(restaurantId);
					}

					if (request.getParameter("name") != null) {
						String name = request.getParameter("name");
						restaurant.setName(name);
					}

					if (request.getParameter("type") != null) {
						String type = request.getParameter("type");
						restaurant.setType(type);
					}

					if (action.equals("create")) {
						// Create new record
						dao.addrestaurant(restaurant);
					} else if (action.equals("update")) {
						// Update existing record
						dao.updaterestaurant(restaurant);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", restaurant);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("Id") != null) {
						int restaurantId = Integer.parseInt(request.getParameter("Id"));
						dao.deleterestaurant(restaurantId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				}
			} catch (Exception ex) {
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
}