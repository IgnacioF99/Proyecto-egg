package com.servicios.egg.controladores;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ErroresControlador implements ErrorController {
    @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView renderErrorPage( HttpServletRequest httpRequest ) {
        ModelAndView errorPage = new ModelAndView("error");// Vista de error definida en"error.html"

        String errorMsg = "";

        int httpErrorCode = getErrorCode(httpRequest); // Obtiene el codigo de error HTTP

        // Switch para determinar el mensaje de error según el código de estado HTTP
        switch (httpErrorCode) {
            case 400 -> {
                errorMsg = "El recurso solicitado no existe.";
            }
            case 403 -> {
                errorMsg = "No tiene permisos para acceder al recurso.";
            }
            case 401 -> {
                errorMsg = "No se encuentra autorizado.";
            }
            case 404 -> {
                errorMsg = "El recurso solicitado no fue encontrado";
            }
            case 500 -> {
                errorMsg = "Ocurrio un error interno";
            }

        }

        // Agrega el código de error y el mensaje de error al modelo para mostrar en la vista
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    // Método para obtener el código de estado HTTP del objeto HttpServletRequest
    private int getErrorCode( HttpServletRequest httpRequest ) {
        Integer errorCode = (Integer) httpRequest.getAttribute("jakarta.servlet.error.status_code");
        return errorCode != null ? errorCode.intValue() : 0;
    }

    // Método que devuelve la ruta a la página de error
    public String getErrorPath() {
        return "/error.html";
    }
}
