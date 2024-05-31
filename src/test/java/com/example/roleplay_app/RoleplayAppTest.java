package com.example.roleplay_app;

import com.example.roleplay_app.model.Armor;
import com.example.roleplay_app.model.Damage;
import com.example.roleplay_app.model.Hero;
import com.example.roleplay_app.model.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
public class RoleplayAppTest {
    private Hero hero;
    private List<Weapon> weapons;
    private List<Armor> armors;

    @BeforeEach
    public void setUp() {
        weapons = new ArrayList<>();
        weapons.add(new Weapon("1", "kard", 10, 15));
        weapons.add(new Weapon("2", "kard2", 5, 10));

        armors = new ArrayList<>();
        armors.add(new Armor("1", "chest",5));
        armors.add(new Armor("2", "boots",10));

        hero = new Hero("1", "TestHero", "Warrior", 1, 50.0, false, false, weapons, armors);
    }

    @Test
    public void testGetFullArmor() {
        int fullArmor = hero.getFullArmor();
        assertEquals(15, fullArmor, "Full armor should be the sum of all armors.");
    }

    @Test
    public void testGetFullDamage() {
        Damage fullDamage = hero.getFullDamage();
        assertEquals(15, fullDamage.getMinDamage(), "Full min damage should be the sum of all weapons' min damage.");
        assertEquals(25, fullDamage.getMaxDamage(), "Full max damage should be the sum of all weapons' max damage.");
    }

    @Test
    public void testUsePotion() {
        hero.usePotion();
        assertTrue(hero.getUsedPotion(), "Hero should have used a potion.");
        assertEquals(85.0, hero.getHealth(), "Hero's health should increase by 35 after using a potion.");
    }

    @Test
    public void testSetIsDefending() {
        hero.setIsDefending(true);
        assertTrue(hero.getIsDefending(), "Hero should be in defending state.");
    }
}


