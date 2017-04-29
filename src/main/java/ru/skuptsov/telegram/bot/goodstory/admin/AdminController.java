package ru.skuptsov.telegram.bot.goodstory.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.skuptsov.telegram.bot.goodstory.parser.StorySaver;
import ru.skuptsov.telegram.bot.platform.client.TelegramBotApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final static Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private StorySaver storySaver;

    @Autowired
    private TelegramBotApi telegramBotApi;

    @RequestMapping(value = "/book", method = GET)
    public String book(Model model) throws IOException {
        model.addAttribute("createBookRequest", new CreateBookRequest());

        return "addBookForm";
    }

    @RequestMapping(value = "/book", method = POST)
    public String addBook(@ModelAttribute CreateBookRequest createBookRequest) throws Exception {
        try {
            storySaver.save(
                    new BufferedReader(new StringReader(createBookRequest.getText())),
                    createBookRequest.getName(),
                    createBookRequest.getAuthor(),
                    createBookRequest.getYear());
        } catch (Exception ex) {
            log.error("Error while saving book", ex);
            throw new Exception(ex);
        }

        return "result";
    }

    @RequestMapping(value = "/message", method = POST)
    public void sendTestMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("127255693");
        message.setText("yohhu!");
        telegramBotApi.sendMessage(message).async();
    }
}
