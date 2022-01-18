package com.kamelboyz.kameluno.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Opponent{
    private String name;
    private int position;
    private int nrCards;
}