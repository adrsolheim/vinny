#!/bin/sh

git fetch origin
new_commits=$(git rev-list --count HEAD..origin/main)

if [ "$new_commits" -eq 0 ]
then
    exit 1
fi

git pull origin main
docker compose down && docker compose up -d --force-recreate
