package com.sharevar.appstudio.controller;

import com.google.gson.Gson;
import com.sharevar.appstudio.common.serializable.JsonUtil;
import com.sharevar.appstudio.domain.Response;
import com.sharevar.appstudio.object.function.Function;
import com.sharevar.appstudio.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@RestController
public class DataController {

    DataRepository repository=new DataRepository();
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public Object dataPostOp(Function function) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String fun=request.getParameter("function");
        function=JsonUtil.fromJson(fun,Function.class);
        HttpServletResponse response = requestAttributes.getResponse();
        Object object=repository.exexut(function);
//        try {
//            PrintWriter writer=response.getWriter();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return object;
    }
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public Response dataGetOp(Function function) {
        Object object=repository.exexut(function);
        return new Response(object);
    }
}
