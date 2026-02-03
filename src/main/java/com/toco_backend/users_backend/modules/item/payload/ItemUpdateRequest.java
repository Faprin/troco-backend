package com.toco_backend.users_backend.modules.item.payload;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    private String title;
    private String description;
    private String category;
    
    // Al ser PATCH, si envían null, no tocamos la imagen.
    // Si envían mapa vacío o nuevo, reemplazamos.
    private Map<String, Object> imageUrls;
    
    private Map<String, Object> attributes;
    
    // Lo recibimos como String para validar manualmente si es un Enum correcto
    private String status; 
}