package com.example.board_game_app.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weapon {

    private String id;

    private String name;

   private int minDamage;

   private int maxDamage;
}
