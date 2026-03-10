#!/bin/bash

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=========================================="
echo "   PRUEBAS API FOROHUB - CRUD COMPLETO"
echo "=========================================="

BASE_URL="http://localhost:8081"

# Test 1: Autenticación
echo -e "\n${YELLOW}1. POST /login - Autenticación${NC}"
LOGIN_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "login": "admin",
    "clave": "123456"
  }')

HTTP_CODE=$(echo "$LOGIN_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
RESPONSE_BODY=$(echo "$LOGIN_RESPONSE" | sed '/HTTP_CODE/d')

if [ "$HTTP_CODE" = "200" ]; then
  echo -e "${GREEN}✅ Autenticación exitosa (200)${NC}"
  TOKEN=$(echo "$RESPONSE_BODY" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
  echo "Token obtenido: ${TOKEN:0:50}..."
else
  echo -e "${RED}❌ Error en autenticación (HTTP $HTTP_CODE)${NC}"
  echo "$RESPONSE_BODY"
  exit 1
fi

# Test 2: Crear tópico
echo -e "\n${YELLOW}2. POST /topicos - Crear tópico${NC}"
CREATE_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$BASE_URL/topicos" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "titulo": "¿Cómo implementar JWT en Spring Boot?",
    "mensaje": "Necesito ayuda para implementar autenticación con JWT en mi API REST",
    "autor": "Juan Pérez",
    "curso": "Spring Boot Security"
  }')

HTTP_CODE=$(echo "$CREATE_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
RESPONSE_BODY=$(echo "$CREATE_RESPONSE" | sed '/HTTP_CODE/d')

if [ "$HTTP_CODE" = "201" ]; then
  echo -e "${GREEN}✅ Tópico creado (201)${NC}"
  TOPICO_ID=$(echo "$RESPONSE_BODY" | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)
  echo "ID del tópico: $TOPICO_ID"
  echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
else
  echo -e "${RED}❌ Error al crear tópico (HTTP $HTTP_CODE)${NC}"
  echo "$RESPONSE_BODY"
fi

# Test 3: Listar todos los tópicos
echo -e "\n${YELLOW}3. GET /topicos - Listar tópicos (paginado)${NC}"
LIST_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X GET "$BASE_URL/topicos?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN")

HTTP_CODE=$(echo "$LIST_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
RESPONSE_BODY=$(echo "$LIST_RESPONSE" | sed '/HTTP_CODE/d')

if [ "$HTTP_CODE" = "200" ]; then
  echo -e "${GREEN}✅ Listado obtenido (200)${NC}"
  echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
else
  echo -e "${RED}❌ Error al listar (HTTP $HTTP_CODE)${NC}"
  echo "$RESPONSE_BODY"
fi

# Test 4: Obtener detalle de un tópico
if [ -n "$TOPICO_ID" ]; then
  echo -e "\n${YELLOW}4. GET /topicos/{id} - Detalle del tópico${NC}"
  DETAIL_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X GET "$BASE_URL/topicos/$TOPICO_ID" \
    -H "Authorization: Bearer $TOKEN")

  HTTP_CODE=$(echo "$DETAIL_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
  RESPONSE_BODY=$(echo "$DETAIL_RESPONSE" | sed '/HTTP_CODE/d')

  if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Detalle obtenido (200)${NC}"
    echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
  else
    echo -e "${RED}❌ Error al obtener detalle (HTTP $HTTP_CODE)${NC}"
    echo "$RESPONSE_BODY"
  fi

  # Test 5: Actualizar tópico
  echo -e "\n${YELLOW}5. PUT /topicos/{id} - Actualizar tópico${NC}"
  UPDATE_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X PUT "$BASE_URL/topicos/$TOPICO_ID" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{
      "titulo": "¿Cómo implementar JWT en Spring Boot 3?",
      "mensaje": "Necesito ayuda actualizada para Spring Boot 3 con las últimas versiones",
      "curso": "Spring Boot 3 Security"
    }')

  HTTP_CODE=$(echo "$UPDATE_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
  RESPONSE_BODY=$(echo "$UPDATE_RESPONSE" | sed '/HTTP_CODE/d')

  if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✅ Tópico actualizado (200)${NC}"
    echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
  else
    echo -e "${RED}❌ Error al actualizar (HTTP $HTTP_CODE)${NC}"
    echo "$RESPONSE_BODY"
  fi

  # Test 6: Eliminar tópico
  echo -e "\n${YELLOW}6. DELETE /topicos/{id} - Eliminar tópico${NC}"
  DELETE_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X DELETE "$BASE_URL/topicos/$TOPICO_ID" \
    -H "Authorization: Bearer $TOKEN")

  HTTP_CODE=$(echo "$DELETE_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)

  if [ "$HTTP_CODE" = "204" ]; then
    echo -e "${GREEN}✅ Tópico eliminado (204 No Content)${NC}"
  else
    echo -e "${RED}❌ Error al eliminar (HTTP $HTTP_CODE)${NC}"
  fi

  # Test 7: Verificar que el tópico fue eliminado
  echo -e "\n${YELLOW}7. GET /topicos/{id} - Verificar eliminación (debe dar 404)${NC}"
  VERIFY_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X GET "$BASE_URL/topicos/$TOPICO_ID" \
    -H "Authorization: Bearer $TOKEN")

  HTTP_CODE=$(echo "$VERIFY_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)

  if [ "$HTTP_CODE" = "404" ]; then
    echo -e "${GREEN}✅ Tópico no encontrado - eliminación confirmada (404)${NC}"
  else
    echo -e "${RED}❌ El tópico aún existe (HTTP $HTTP_CODE)${NC}"
  fi
fi

# Test 8: Validación - crear tópico duplicado
echo -e "\n${YELLOW}8. POST /topicos - Validación de duplicados${NC}"
curl -s -X POST "$BASE_URL/topicos" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "titulo": "Tópico único",
    "mensaje": "Este es un mensaje único para probar duplicados",
    "autor": "Test",
    "curso": "Testing"
  }' > /dev/null

DUP_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST "$BASE_URL/topicos" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "titulo": "Tópico único",
    "mensaje": "Este es un mensaje único para probar duplicados",
    "autor": "Test",
    "curso": "Testing"
  }')

HTTP_CODE=$(echo "$DUP_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
RESPONSE_BODY=$(echo "$DUP_RESPONSE" | sed '/HTTP_CODE/d')

if [ "$HTTP_CODE" = "400" ]; then
  echo -e "${GREEN}✅ Validación de duplicados funciona (400)${NC}"
  echo "$RESPONSE_BODY"
else
  echo -e "${RED}❌ No se validó el duplicado correctamente (HTTP $HTTP_CODE)${NC}"
fi

# Test 9: Acceso sin token
echo -e "\n${YELLOW}9. GET /topicos - Sin token (debe dar 403)${NC}"
NO_AUTH_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X GET "$BASE_URL/topicos")

HTTP_CODE=$(echo "$NO_AUTH_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)

if [ "$HTTP_CODE" = "403" ]; then
  echo -e "${GREEN}✅ Protección sin token funciona (403 Forbidden)${NC}"
else
  echo -e "${RED}❌ La protección sin token no funciona correctamente (HTTP $HTTP_CODE)${NC}"
fi

echo -e "\n=========================================="
echo -e "   ${GREEN}PRUEBAS COMPLETADAS${NC}"
echo "=========================================="
echo -e "\n📝 Puedes usar también:"
echo "   - Swagger UI: http://localhost:8081/swagger-ui.html"
echo "   - Postman/Insomnia con las credenciales:"
echo "     • Usuario: admin"
echo "     • Contraseña: 123456"
