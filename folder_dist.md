com.swaploop
├── 📁 config           # Configuraciones globales del sistema
│   ├── SecurityConfig.java      # Configuración de JWT y rutas públicas
│   ├── WebSocketConfig.java     # Configuración de STOMP para el chat
│   ├── CorsConfig.java          # Para permitir peticiones desde Flutter
│   └── SwaggerConfig.java       # Documentación de la API
│
├── 📁 common           # Clases compartidas por toda la app
│   ├── 📁 exception             # Manejador global de errores
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   ├── 📁 utils                 # Utilidades (Geometría, Fechas)
│   └── 📁 response              # Wrappers para respuestas JSON estandarizadas
│       └── ApiResponse.java     # Ej: { status: "OK", data: ... }
│
├── 📁 security         # Lógica específica de seguridad
│   ├── JwtService.java          # Generar y validar Tokens
│   ├── UserDetailServiceImpl.java
│   └── JwtAuthenticationFilter.java
│
├── 📁 integration      # Comunicación con servicios externos
│   ├── 📁 ai_client             # Cliente para hablar con FastAPI
│   │   ├── FastApiClient.java   # Usa WebClient para enviar fotos
│   │   └── AnalysisResultDto.java
│   └── 📁 storage               # (Futuro) Para subir fotos a S3/MinIO
│
└── 📁 modules          # AQUÍ VIVE TU NEGOCIO (Dividido por dominios)
    │
    ├── 📁 auth                  # Login y Registro
    │   ├── AuthController.java
    │   ├── AuthService.java
    │   └── dto/                 # LoginRequest, RegisterRequest
    │
    ├── 📁 user                  # Perfil y Reputación
    │   ├── UserController.java
    │   ├── UserService.java
    │   ├── UserRepository.java
    │   └── model/               # UserEntity.java
    │
    ├── 📁 item                  # Productos y Geolocalización
    │   ├── ItemController.java
    │   ├── ItemService.java     # Lógica: Buscar items cercanos (PostGIS)
    │   ├── ItemRepository.java  # Queries espaciales
    │   ├── model/               # ItemEntity.java (con JSONB y Point)
    │   └── dto/                 # ItemCreateRequest, ItemResponse
    │
    ├── 📁 swap                  # La lógica del Intercambio
    │   ├── SwapController.java  # Endpoints: /propose, /accept, /reject
    │   ├── SwapService.java     # Lógica: Validar que los items existen
    │   ├── SwapRepository.java
    │   ├── model/               # SwapProposal.java, SwapOfferedItem.java
    │   └── dto/                 # CreateSwapRequest
    │
    └── 📁 chat                  # Mensajería en tiempo real
        ├── ChatController.java  # Maneja WebSockets y REST
        ├── ChatService.java
        ├── MessageRepository.java
        └── model/               # Conversation.java, Message.java