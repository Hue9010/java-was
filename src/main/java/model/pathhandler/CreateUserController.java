package model.pathhandler;

import db.DataBase;
import model.HttpRequest;
import model.User;
import model.response.HttpResponse;

public class CreateUserController implements PathController {
	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		DataBase.addUser(new User(request.getParameter("userId"), 
										request.getParameter("password"),
										request.getParameter("name"), 
										request.getParameter("email")));
		response.setUrl("/index.html");
	}
}