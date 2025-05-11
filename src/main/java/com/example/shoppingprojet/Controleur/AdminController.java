package com.example.shoppingprojet.Controleur;

public class AdminController implements ControlledScreenAdmin{
    private MainAdminController mainAdminController;

    @Override
    public void setMainControllerAdmin(MainAdminController mainAdminController) {
        this.mainAdminController = mainAdminController;
    }

}
