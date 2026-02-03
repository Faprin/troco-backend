package com.toco_backend.users_backend.modules.item.payload;

import lombok.Data;
@Data
public class ItemsSearchCriteria {

    private Double latitude;
    private Double longitude; 
    private Double radius = 50.0; // unidad: km. Default 40km
    private String category; // filtro opcional
    private String keyword; // palabra para buscar en titulo o descricpion
}
