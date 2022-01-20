package com.kamelboyz.kameluno.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Opponent{
    private String name;
    private int position;
    private int nrCards;
}