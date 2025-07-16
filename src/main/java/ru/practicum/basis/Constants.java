package ru.practicum.basis;

public class Constants {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static final String USER_CREATE_URL = "api/auth/register";
    public static final String USER_LOGIN_URL = "api/auth/login";
    public static final String USER_DELETE_URL = "api/auth/user";
    public static final String ORDER_CREATE_URL = "api/orders";

    //База актуальных хешей ингредиентов, адаптировать в случае изменений
    //Флюоресцентная булка R2-D3
    public static final String ITEM_BUN_R2D3 = "61c0c5a71d1f82001bdaaa6d";
    //Мясо бессмертных моллюсков Protostomia
    public static final String ITEM_MAIN_PROTOSTOMIA = "61c0c5a71d1f82001bdaaa6f";
    //Соус Spicy-X
    public static final String ITEM_SAUCE_SPICY = "61c0c5a71d1f82001bdaaa72";
}
