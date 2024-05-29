package com.example.board_game_app.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hero {

    private String id;

    private String name;

    private String characterClass;

    private int level;

    private double health;

    private Boolean usedPotion = Boolean.FALSE;

    private Boolean isDefending = Boolean.FALSE;

    private List<Weapon> weapons;

    private List<Armor> armors;

    public Integer getFullArmor() {
        return armors.stream().mapToInt(Armor::getArmor).sum();
    }

    public Damage getFullDamage() {
        int sumMinDamage = weapons.stream().mapToInt(Weapon::getMinDamage).sum();
        int sumMaxDamage = weapons.stream().mapToInt(Weapon::getMaxDamage).sum();

        return Damage.builder()
                .minDamage(sumMinDamage)
                .maxDamage(sumMaxDamage).
                build();
    }

    public void usePotion() {
        setUsedPotion(true);
        setHealth(getHealth() + 35);
    }
}
