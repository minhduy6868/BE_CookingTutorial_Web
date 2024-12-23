package com.example.CookingTutorial.controller;

import com.example.CookingTutorial.dto.VoteMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Controller
public class SurveyController {

    private final Map<String, Integer> votes = new HashMap<>() {{
        put("Món ăn chính", 0);
        put("Ăn kèm", 0);
        put("Tráng miệng", 0);
        put("Ăn nhẹ", 0);
        put("Đồ uống", 0);
        put("Ăn vặt", 0);
        put("Khác", 0);
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
