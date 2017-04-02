package nl.hanze2017e4.gameclient.controller;

import javafx.application.Platform;
import javafx.*;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Copyright (C) 4/1/2017 By Joris Oosterhuis
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class GuiModeController {

    @FXML GridPane gridpane;

    public GuiModeController() {

    }


    private void addLabelToPane(int xPos, int yPos, Label label){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label();
                label.setText("test");
                //de volgende twee regels aan code is hergebruikt van de boggleOpdracht waar Vincent Luder aan heeft gewerkt
                gridpane.setHalignment(label, HPos.CENTER);
                gridpane.setValignment(label, VPos.CENTER);

                //de volgende twee regels komen van de volgende bron:↓
                //bron: http://stackoverflow.com/questions/28320110/javafx-how-to-get-column-and-row-index-in-gridpane
                gridpane.setRowIndex(label,xPos);
                gridpane.setColumnIndex(label,yPos);
                gridpane.getChildren().add(label);
            }
        });
    }
}
