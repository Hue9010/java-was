package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.HttpRequest;
import model.pathcontroller.Controllers;
import model.pathcontroller.PathController;
import model.response.Http200Response;
import model.response.HttpResponse;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			DataOutputStream dos = new DataOutputStream(out);
			HttpRequest request = new HttpRequest(in);
			PathController controller = Controllers.find(request.getUrl());
			HttpResponse response = controller.getResponse();
			controller.handling(request, response);
			response.writeResponse(dos);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
