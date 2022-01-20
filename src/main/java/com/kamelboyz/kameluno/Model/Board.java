package com.kamelboyz.kameluno.Model;

import com.kamelboyz.kameluno.Model.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    Card topCard;
    Map<String, Integer> hands;
}
