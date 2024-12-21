package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.VoteMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SurveyController {
    @MessageMapping("/sendMessage") // Nhận message từ client với prefix /app/sendMessage
    @SendTo("/topic/messages") // Gửi message tới tất cả các client lắng nghe tại /topic/messages
    public String sendMessage(String message) {
        return message; // Trả lại message nhận được
    }
    private final Map<String, Integer> votes = new HashMap<>() {{
        put("apple", 0);
        put("banana", 0);
        put("orange", 0);
    }};

    @MessageMapping("/vote") // Xử lý khi client gửi đến /app/vote
    @SendTo("/topic/results") // Gửi kết quả tới tất cả client đang lắng nghe topic /topic/results
    public Map<String, Double> handleVote(VoteMessage message) {
        votes.put(message.getChoice(), votes.getOrDefault(message.getChoice(), 0) + 1);

        int totalVotes = votes.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Double> percentages = new HashMap<>();
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            percentages.put(entry.getKey(), (entry.getValue() * 100.0) / totalVotes);
        }
        return percentages;
    }
}
