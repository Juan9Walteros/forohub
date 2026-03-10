#!/bin/bash

echo "=== Test 1: Autenticación (POST /login) ==="
TOKEN_RESPONSE=$(curl -s -X POST http://localhost:8081/login \
  -H "Content-Type: application/json" \
  -d '{"login":"admin","clave":"123456"}')

echo "Respuesta: $TOKEN_RESPONSE"

# Extraer el token
TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
  echo -e "\n✅ Token obtenido: ${TOKEN:0:50}..."
  
  echo -e "\n=== Test 2: Crear tópico (POST /topicos) ==="
  curl -s -X POST http://localhost:8081/topicos \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{
      "titulo": "¿Cómo usar Spring Boot?",
      "mensaje": "Necesito ayuda con Spring Boot 3",
      "autor": "admin",
      "curso": "Spring Boot 3"
    }' | python3 -m json.tool || cat
  
  echo -e "\n\n=== Test 3: Listar tópicos (GET /topicos) ==="
  curl -s -X GET "http://localhost:8081/topicos?page=0&size=10" \
    -H "Authorization: Bearer $TOKEN" | python3 -m json.tool || cat
    
else
  echo "❌ No se pudo obtener el token"
fi
