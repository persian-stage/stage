#!/bin/bash

# Variables
BACKUP_DIR="/home/amir/devhost/persian-stage/backups"
BACKUP_FILE="$BACKUP_DIR/postgres_data_backup_$(date +%Y%m%d%H%M%S).tar"
CONTAINER_NAME="postgres_container"

# Create backup directory if it doesn't exist
mkdir -p $BACKUP_DIR

# Run the backup using a temporary container
docker run --rm --volumes-from $CONTAINER_NAME -v $BACKUP_DIR:/backup busybox tar cvf /backup/postgres_data_backup.tar /var/lib/postgresql/data

# Rename the backup file with a timestamp
mv $BACKUP_DIR/postgres_data_backup.tar $BACKUP_FILE

echo "Backup completed: $BACKUP_FILE"