package com.toco_backend.users_backend.modules.item;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.common.utils.GeometryUtil;
import com.toco_backend.users_backend.modules.item.model.ItemEntity;
import com.toco_backend.users_backend.modules.item.model.ItemResponse;
import com.toco_backend.users_backend.modules.item.model.ItemsSearchCriteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final ItemRepository itemRepository;

    public Page<ItemResponse> listItems(ItemsSearchCriteria criteria, Pageable pageable) {

        Point userLocation = GeometryUtil.createPoint(criteria.getLatitude(), criteria.getLongitude());

        Double radioMetros = criteria.getRadius() * 1000;

        Page<ItemEntity> itemsPage = itemRepository.searchNearby(userLocation, radioMetros, criteria.getCategory(),
                criteria.getKeyword(), pageable);

        return itemsPage.map(item -> mapToItemResponse(item, userLocation));
    }

    public ItemResponse mapToItemResponse(ItemEntity item, Point userLocation) {

        Double distance = calculateHaversineDistance(userLocation, item.getOwner().getLocation());

        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .mainImageUrl(item.getImageUrls() != null && !item.getImageUrls().isEmpty()
                        ? (String) item.getImageUrls().get(0)
                        : null)
                .ownerUsername(item.getOwner().getUsername())
                .distanceKm(Math.round(distance * 10.0) / 10.0)
                .build();

    }

    /**
     * Calcula la distancia en Kilómetros entre dos puntos geométricos usando
     * Haversine.
     * * @param p1 Punto A (ej. Usuario)
     * @param p2 Punto B (ej. Item)
     * @return Distancia en km, o null si algún punto no existe.
     */
    public Double calculateHaversineDistance(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            return null; // O devuelve 0.0 si prefieres
        }

        // 1. Obtener coordenadas (JTS usa X=Lon, Y=Lat)
        double lat1 = p1.getY();
        double lon1 = p1.getX();
        double lat2 = p2.getY();
        double lon2 = p2.getX();

        // 2. Convertir diferencias a Radianes (¡Vital para la trigonometría!)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // 3. Fórmula de Haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 4. Calcular distancia final
        return EARTH_RADIUS_KM * c;
    }
}
