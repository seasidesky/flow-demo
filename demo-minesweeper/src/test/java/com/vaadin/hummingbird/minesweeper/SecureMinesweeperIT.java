package com.vaadin.hummingbird.minesweeper;

import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.hummingbird.minesweeper.secure.Minefield;

public class SecureMinesweeperIT extends MinesweeperPageObject {

    @Test
    public void applicationStarts() {
        openUrl("");
        waitForApplicationToStart();
    }

    @Test
    public void clickOnEmptyRevealsCell() {
        openUrl("seed=1");
        assertCellHidden(0, 0);
        getCell(0, 0).click();
        assertCellEmpty(0, 0);
    }

    @Test
    public void clickOnmineShowsNotification() {
        openUrl("seed=1");
        getCell(9, 0).click();
        waitForBoomNotification();
    }

    @Test
    public void solveSeed1Minefield() {
        openUrl("seed=1");
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (!isMineSeed1(row, col)) {
                    assertCellHiddenOrEmpty(
                            row + "," + col + " is revealed or a mine", row,
                            col);
                    getCell(row, col).click();
                }
            }
        }
        waitForSuccessNotification();
    }

    private Minefield minefieldSeed1 = new Minefield();

    {
        minefieldSeed1.init(10, 10, 1, 0.2);
    }

    private boolean isMineSeed1(int row, int col) {
        return minefieldSeed1.isMine(row, col);
    }

    @Override
    protected WebElement getMineField() {
        return findElement(By.className("secure"));
    }

}