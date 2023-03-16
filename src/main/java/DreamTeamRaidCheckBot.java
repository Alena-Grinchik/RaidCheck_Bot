import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class DreamTeamRaidCheckBot extends TelegramLongPollingBot {
    private int receivedReply;
    private boolean negativeReply;

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText() && toReact(update.getMessage())) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());
            // System.out.println(update.getMessage().getChatId());

        } else if(update.hasPollAnswer()){
            PollAnswer pollAnswer = update.getPollAnswer();
            List <Integer> optionIds = pollAnswer.getOptionIds();
            if (optionIds.isEmpty()){
                receivedReply = receivedReply - 1;
            } else {
                receivedReply = receivedReply + 1;
                for (Integer option: optionIds) {
                    if (option == 1) {
                        negativeReply = true;
                        break;
                    }
                }
            }
            System.out.println(update.getPollAnswer().getUser().getFirstName());
        }
    }

    @Override
    public String getBotUsername() {
        return "DreamTeamRaidCheck_bot";
    }

    @Override
    public String getBotToken() {
        return "5657681076:AAFeyV7FL9D0k79gUfFr_aYMK0Lwz9hMRVE";
    }

    public boolean toReact(Message message){
        if (message.getChatId()==-938867741 || message.getChatId()== 1328572042) {
            return true;
        }
        return false;
    }

    public void sendPoll(){
        List<String> options = new ArrayList<>();
        options.add("да");
        options.add("нет");

        SendPoll poll = new SendPoll();
        poll.setChatId("-938867741");
        poll.setIsAnonymous(false);
        poll.setQuestion("Сбору быть или не быть, вот в чем вопрос");
        poll.setOptions(options);

        try {
            execute(poll); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendResult(){
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId("-938867741");
        if (receivedReply == 4 & !negativeReply) {
            message.setText("да будет сбор");
        } else {
            message.setText("Сбор отменен, трифекта сама себя не сделает, лентяи");
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        receivedReply = 0;
        negativeReply = false;
    }
}
