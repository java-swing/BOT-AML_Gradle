package com.tpbank.control;


import com.tpbank.GUI.AML_BOTview;

import java.sql.SQLException;

public class main {
    public static void main(String[] arg) throws SQLException, ClassNotFoundException {
        try {
            AML_BOTview view = new AML_BOTview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}