package com.knightliao.jobflow.rabbitwebdemo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.knightliao.jobflow.rabbitwebdemo.service.Message;

@Controller
public class HomeController {

    @Autowired
    AmqpTemplate amqpTemplate;

    @RequestMapping(value = "/")
    public String home(Model model) {
        model.addAttribute(new Message());
        return "WEB-INF/views/home.jsp";
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public String publish(Model model, Message message) {
        // Send a message to the "messages" queue
        amqpTemplate.convertAndSend("foo.bar", message.getValue());
        model.addAttribute("published", true);
        return home(model);
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public String get(Model model) {
        // Receive a message from the "messages" queue
        String message = (String) amqpTemplate.receiveAndConvert("messages");
        if (message != null) {
            model.addAttribute("got", message);
        } else {
            model.addAttribute("got_queue_empty", true);
        }

        return home(model);
    }
}