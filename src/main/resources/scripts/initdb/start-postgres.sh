#!/usr/bin/env bash
set -euo pipefail

VOLUME_NAME="volume-misuradb"
CONTAINER_NAME="container-misuradb"
IMAGE="docker.io/library/postgres:16"

# Precisa bater com o database da URL em application.properties (minusculo:
# o Postgres trata identificadores criados com aspas como case-sensitive).
DB_NAME="misuradb"
DB_USER="mscosta"
DB_PASSWORD="mscosta"
HOST_PORT="5432"

# Cria o volume do Podman, se ainda não existir
podman volume inspect "${VOLUME_NAME}" >/dev/null 2>&1 || \
  podman volume create "${VOLUME_NAME}"

# Remove o container anterior, se existir
if podman container inspect "${CONTAINER_NAME}" >/dev/null 2>&1; then
  podman rm -f "${CONTAINER_NAME}"
fi

# Sobe o PostgreSQL
podman run -d \
  --name="${CONTAINER_NAME}" \
  -p "${HOST_PORT}:5432" \
  -e POSTGRES_DB="${DB_NAME}" \
  -e POSTGRES_USER="${DB_USER}" \
  -e POSTGRES_PASSWORD="${DB_PASSWORD}" \
  -v "${VOLUME_NAME}:/var/lib/postgresql/data:Z" \
  "${IMAGE}"

echo "Container '${CONTAINER_NAME}' criado com sucesso."