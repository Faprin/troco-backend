package com.toco_backend.users_backend.modules.item;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toco_backend.users_backend.common.utils.GeometryUtil;
import com.toco_backend.users_backend.modules.item.model.ItemEntity;
import com.toco_backend.users_backend.modules.item.model.ItemStatus;
import com.toco_backend.users_backend.modules.item.payload.CreateItemRequest;
import com.toco_backend.users_backend.modules.item.payload.ItemDetailResponse;
import com.toco_backend.users_backend.modules.item.payload.ItemResponse;
import com.toco_backend.users_backend.modules.item.payload.ItemUpdateRequest;
import com.toco_backend.users_backend.modules.item.payload.ItemsSearchCriteria;
import com.toco_backend.users_backend.modules.user.UserRepository;
import com.toco_backend.users_backend.modules.user.model.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * Lista todos los items para el feed del usuario según los criterios del mismo
     * 
     * @param criteria criterios sobre los cuales se aplica la búsqueda
     * @param pageable número de página en la que se encuentra el usuario
     * @return pagina con los siguientes elementos
     */
    public Page<ItemResponse> listItems(ItemsSearchCriteria criteria, Pageable pageable, String currentUserUsername) {

        Point userLocation = GeometryUtil.createPoint(criteria.getLatitude(), criteria.getLongitude());

        Double radioMetros = criteria.getRadius() * 1000;

        Page<ItemEntity> itemsPage = itemRepository.searchNearby(userLocation, radioMetros, criteria.getCategory(),
                criteria.getKeyword(), pageable);

        return itemsPage.map(item -> mapToItemResponse(item, userLocation));
    }

    /**
     * Devuelve el detalle de un item existente en la base de datos
     * 
     * @param id identificador del item que se quiere saber el detalle
     * @return Respuesta normalizada sobreel item
     */
    public ItemDetailResponse getItemDetail(Long id) {

        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        return ItemDetailResponse.builder()
                .id(id)
                .title(item.getTitle())
                .imageUrls(item.getImageUrls() != null ? item.getImageUrls() : null)
                .ownerUsername(item.getOwner().getUsername())
                .likes(item.getLikes())
                .status(item.getStatus().toString())
                .isLikeByMe(false) // TODO cambiar cuando se meta el likeRepository
                .build();
    }

    /**
     * Metodo que según si el usuario es el mismo o no, muestra todos los elementos
     * disponibles o todos los elementos sin importar su estado
     * 
     * @param ownerUsername usuario que viene por la url
     * @param requester     usuario que envía la petición
     * @return Lista de items
     */
    public List<ItemResponse> getItemsOfUser(String ownerUsername, String requesterUsername) {

        UserEntity requesterUser = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<ItemEntity> items;

        if (requesterUser.getUsername().equals(ownerUsername)) {
            // enviamos todos, independientemente del estado

            items = itemRepository.findAllByOwnerOrderByCreatedAtDesc(requesterUser);
        } else {

            items = itemRepository.findAllByOwnerAndStatusOrderByCreatedAtDesc(requesterUser, null);
        }

        return items.stream()
                .map(item -> mapToItemResponse(item, null)).toList();

    }

    /**
     * Método que se encarga de subir a la base de datos un nuevo item
     * 
     * @param itemRequest item que se quiere subir
     * @param username    dueño del item que se ha subido
     * @return
     */
    public ItemResponse createItem(CreateItemRequest itemRequest, String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ItemEntity item = ItemEntity.builder()
                .owner(user)
                .title(itemRequest.getTitle())
                .description(itemRequest.getDescription())
                .category(itemRequest.getCategory())
                .imageUrls(itemRequest.getImageUrls())
                .attributes(itemRequest.getAttributes() != null ? itemRequest.getAttributes() : null)
                .status(ItemStatus.AVIABLE)
                .likes(0)
                .build();

        itemRepository.save(item);

        return mapToItemResponse(item, user.getLocation());
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemUpdateRequest request, String requesterUsername) {

        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        // 2. SEGURIDAD: Verificar que quien edita es el dueño
        if (!item.getOwner().getUsername().equals(requesterUsername)) {
            throw new RuntimeException("No tienes permiso para editar este artículo");
        }

        // 3. ACTUALIZACIÓN PARCIAL (Logic Patch)
        // Solo actualizamos si el campo viene en el JSON (no es null)

        if (request.getTitle() != null) {
            item.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }

        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }

        if (request.getImageUrls() != null) {
            item.setImageUrls(request.getImageUrls());
        }

        if (request.getAttributes() != null) {
            item.setAttributes(request.getAttributes());
        }

        if (request.getStatus() != null) {
            try {
                item.setStatus(ItemStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("El estado proporcionado no es válido: " + request.getStatus());
            }
        }

        ItemEntity savedItem = itemRepository.save(item);

        // 6. Devolver respuesta
        // Pasamos null en userLocation porque al editar no necesitamos recalcular la
        // distancia
        return mapToItemResponse(savedItem, null);
    }

    /**
     * Método que elimina de manera lógica el elemento de la base de datos. De esta manera, no se rompen transacciones
     * @param id Identificador del elemento que va a ser eliminado
     * @param username Nombre del usuario que elimina un item
     */
    @Transactional
    public void deleteItem(Long id, String username) {
        
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El artículo no existe"));


        if (!item.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para eliminar este artículo. No es tuyo.");
        }

        // Si el artículo ya fue intercambiado o está en proceso, no deberíamos borrarlo físicamente
        // porque romperíamos la Transacción asociada.
        if (item.getStatus() == ItemStatus.SWAPPED || item.getStatus() == ItemStatus.RESERVED) {
             throw new RuntimeException("No puedes eliminar un artículo que está reservado o ya intercambiado.");
        }

        item.setStatus(ItemStatus.DELETED);
        itemRepository.save(item);
    }

    /**
     * Metodo encargado de cambiar el estado de manera rápida y eficiente de la base de datos
     * @param id item del que se cambia el estado
     * @param newStatusStr El nuevo estado que se adopta en string 
     * @param username del usuario que desea cambiar el estado del item. Importante para la seguridad
     * @return
     */
    @Transactional
    public ItemResponse changeItemStatus(Long id, String newStatusStr, String username) {
        
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El artículo no existe"));

        
        if (!item.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para cambiar el estado de este artículo");
        }

        
        try {
            ItemStatus newStatus = ItemStatus.valueOf(newStatusStr.toUpperCase());
            
            item.setStatus(newStatus);
            
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Estado inválido. Valores permitidos: AVAILABLE, RESERVED, HIDDEN, EXCHANGED");
        }

        ItemEntity savedItem = itemRepository.save(item);

        return mapToItemResponse(savedItem, null);
    }

    /* ################ MÉTODOS AUXILIARES ################ */

    /**
     * 
     * @param item
     * @param userLocation
     * @return
     */
    public ItemResponse mapToItemResponse(ItemEntity item, Point userLocation) {

        // UserEntity currentUser = userRepository.findByUsername(currentUserUsername)
        // .orElseThrow(() -> new RuntimeException("El usuario desde el que se ejecuta
        // este GET no ha sido encontraod"));

        Double distance = calculateHaversineDistance(userLocation, item.getOwner().getLocation());

        String mainImage = null;

        if (item.getImageUrls() != null && !item.getImageUrls().isEmpty()) {
            // 1. Obtenemos todos los valores (las URLs) ignorando cómo se llaman las claves
            // 2. Convertimos a String el primero que encontremos
            Object firstValue = item.getImageUrls().values().iterator().next();

            if (firstValue != null) {
                mainImage = firstValue.toString();
            }
        }

        /// TODO ISLIKE BY ME HACERLO CUANDO SE TENGA EL LIKEREPOSITORY HECHO

        return ItemResponse.builder()
                .title(item.getTitle())
                .mainImageUrl(mainImage)
                .ownerUsername(item.getOwner().getUsername())
                .distanceKm(distance != null ? Math.round(distance * 10.0) / 10.0 : null)
                .likes(item.getLikes())
                .isLikeByMe(false)
                .status(item.getStatus())
                .build();

    }

    /**
     * Calcula la distancia en Kilómetros entre dos puntos geométricos usando
     * Haversine.
     * * @param p1 Punto A (ej. Usuario)
     * 
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
